/*
 *	XMPP API to chat and interact with the League of Legends ChatServers. 
 *
 *  Copyright (C) 2017  Josue Acevedo
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lolxmpp.api;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import lolxmpp.api.enums.ChatRegion;
import lolxmpp.api.enums.ChatStatus;
import lolxmpp.api.enums.GameStatus;
import lolxmpp.api.enums.LoginResult;
import lolxmpp.api.listeners.FriendJoinListener;
import lolxmpp.api.listeners.FriendLeaveListener;
import lolxmpp.api.listeners.FriendStatusListener;
import lolxmpp.api.listeners.MessageListener;
import lolxmpp.api.listeners.object.FriendStatusEvent;
import lolxmpp.api.listeners.object.MessageEvent;
import lolxmpp.api.util.SimpleAction;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSessionConfiguration;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stanza.model.Presence.Show;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;

public class LoLXMPPAPI {
	
	private LoginResult loginResult = LoginResult.NOT_LOGGED;
	private ChatRegion region;
	private XmppClient xmppClient;
	private ExecutorService  eventsThreadPool = null;
	private Map<String, Friend> friends = new HashMap<String, Friend>();
	private boolean ready = false;
	private boolean delaying = false;
	private RiotAPI riotAPI;
	private List<SimpleAction> readyListeners = new ArrayList<SimpleAction>();
	private List<MessageListener> messageListeners = new ArrayList<MessageListener>();
	private List<FriendStatusListener> friendStatusListeners = new ArrayList<FriendStatusListener>();
	private List<FriendJoinListener> friendJoinListeners = new ArrayList<FriendJoinListener>();
	private List<FriendLeaveListener> friendLeaveListeners = new ArrayList<FriendLeaveListener>();
	private UserPresence selfPresence;
	
	public LoLXMPPAPI(ChatRegion region, RiotAPI riotAPI) {
		this.region = region;
		this.riotAPI = riotAPI;
		this.riotAPI.setRegion(region);
		
		//load profile icons
		ProfileIcon.loadProfileIcons(riotAPI);
	}
	
	public boolean login(String username, String password) {
		try {
			TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
				    .hostname(region.serverHost())
				    .sslContext(SSLContext.getInstance("TLS"))
				    .port(5223)
				    .socketFactory(SSLSocketFactory.getDefault())
				    .build();
			
			XmppSessionConfiguration configuration = XmppSessionConfiguration.builder()
                    //.debugger(ConsoleDebugger.class)
                    .build();
			
			xmppClient = XmppClient.create("pvp.net", configuration, tcpConfiguration);
			
			//Try ChatServer connection
			try {
				xmppClient.connect();
			} catch (XmppException e) {
				e.printStackTrace();
				loginResult = LoginResult.XMPP_CLIENT_ERROR;
				return false;
			}
			
			//Try auth
			try {
				xmppClient.login(username, "AIR_" + password, "xiff");
				//ok, show as available for chat (probably this will be changed after)
				xmppClient.send(new Presence(Presence.Show.CHAT));
				loginResult = LoginResult.AUTH_SUCCESS;
				//API setup
				setupAPI();
				return true;
			} catch (XmppException e) {
				//Auth failed
				e.printStackTrace();
				loginResult = LoginResult.AUTH_FAILED;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			loginResult = LoginResult.SSL_CONTEXT_ERROR;
		}
		
		return false;
	}
	
	public LoginResult getLoginResult() {
		return loginResult;
	}
	
	private void setupAPI() {
		selfPresence = new UserPresence(xmppClient.getConnectedResource().getLocal());
		
		eventsThreadPool = Executors.newFixedThreadPool(1);
		eventsThreadPool.execute(() -> {
			RosterManager roster = xmppClient.getManager(RosterManager.class);
			
			//Add Incoming Presence Listener
			xmppClient.addInboundPresenceListener(e -> {
				if(!delaying) {
					notifyApiReady();
					//Read roster contacts
					for(Contact contact : roster.getContacts()) {
						friends.put(contact.getJid().getLocal(), new Friend(this, contact));
					}
				}
				
				Presence presence = e.getPresence();
				Contact contact = roster.getContact(presence.getFrom());
				
				if(contact != null) {
					if(friends.containsKey(contact.getJid().getLocal())) {
						Friend f = friends.get(contact.getJid().getLocal());
						LoLStatus oldStatus = f.updatePresence(e.getPresence());
						
						synchronized(friendStatusListeners) {
							friendStatusListeners.forEach(listener -> {
								listener.onFriendStatusChange(new FriendStatusEvent(f, oldStatus));
							});
						}
					} else {
						Friend friend = new Friend(this, contact);
						friend.updatePresence(e.getPresence());
						friends.put(contact.getJid().getLocal(), friend);
					}
				}
			});
			
			//Add Incoming Message Listener
			xmppClient.addInboundMessageListener(e -> {
				synchronized(messageListeners) {
					messageListeners.forEach(listener -> {
						Message msg = e.getMessage();
						Friend f = getFriendByJid(msg.getFrom());
						
						if(f == null) {
							System.err.println("Something is wrong! Incoming message from unknown friend.");
						} else {
							listener.onMessage(new MessageEvent(f, msg.getBodies()));
						}
					});
				}
			});
		});
	}
	
	private void notifyApiReady() {
		if(delaying || ready) {
			return;
		}
		
		delaying = true;
		
		//enviar presencia personalizada
		
		new Thread() {
			public void run() {
				try {
					sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				for(SimpleAction c : readyListeners) {
					c.doAction();
				}
				
				readyListeners.clear();
				ready = true;
			};
		}.start();
	}
	
	public void addMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}
	
	public void addFriendStatusChangeListener(FriendStatusListener listener) {
		friendStatusListeners.add(listener);
	}
	
	public void addFriendJoinListener(FriendJoinListener listener) {
		friendJoinListeners.add(listener);
	}
	
	public void addFriendLeaveListener(FriendLeaveListener listener) {
		friendLeaveListeners.add(listener);
	}
	
	private Friend getFriendByJid(Jid jid) {
		return friends.get(jid.getLocal());
	}
	
	public Friend getFriendByName(String name) {
		for(Friend f : friends.values()) {
			if(f.getName().equalsIgnoreCase(name)) {
				return f;
			}
		}
		
		return null;
	}
	
	public Friend getFriendById(String friendId) {
		for(Friend f : friends.values()) {
			if(f.getId().equalsIgnoreCase(friendId)) {
				return f;
			}
		}
		
		return null;
	}
	
	public boolean isReady() {
		return ready;
	}

	public void onReady(SimpleAction e) {
		if(isReady()) {
			e.doAction();
		} else {
			readyListeners.add(e);
		}
	}

	public Collection<Friend> getAllFriends() {
		return new ArrayList<Friend>(friends.values());
	}
	
	public Collection<Friend> getOnlineFriends() {
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend : getAllFriends()) {
			if(friend.isOnline()) {
				list.add(friend);
			}
		}
		
		return list;
	}
	
	public Collection<Friend> getOfflineFriends() {
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend : getAllFriends()) {
			if(!friend.isOnline()) {
				list.add(friend);
			}
		}
		
		return list;
	}
	
	public Collection<Friend> getFriends(ChatStatus chatStatus) {
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend : getAllFriends()) {
			if(friend.getChatStatus() == chatStatus) {
				list.add(friend);
			}
		}
		
		return list;
	}
	
	public Collection<Friend> getFriends(GameStatus gameStatus) {
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend : getAllFriends()) {
			if(friend.getLoLStatus().getGameStatus() == gameStatus) {
				list.add(friend);
			}
		}
		
		return list;
	}
	
	public XmppClient getXMPPClient() {
		return xmppClient;
	}
	
	protected void handleFriendJoinEvent(Friend friend) {
		for(FriendJoinListener listener : friendJoinListeners) {
			listener.onFriendJoin(friend);
		}
	}
	
	protected void handleFriendLeaveEvent(Friend friend) {
		for(FriendLeaveListener listener : friendLeaveListeners) {
			listener.onFriendLeave(friend);
		}
	}

	public RiotAPI getRiotAPI() {
		return riotAPI;
	}
	
	public UserPresence getPresence() {
		return selfPresence;
	}
	
	public void setPresence(UserPresence presence) {
		this.selfPresence = presence;
		
		Presence toSend = new Presence();
		
		ChatStatus chatStatus = presence.getChatStatus();
		switch(chatStatus) {
			case AWAY:
				toSend.setShow(Show.AWAY);
				break;
			case CHAT:
			case MOBILE:
				toSend.setShow(Show.CHAT);
				break;
			case DO_NOT_DISTURB:
				toSend.setShow(Show.DND);
				break;
			case OFFLINE:
				toSend.setType(Presence.Type.UNAVAILABLE);
				break;
		}
		
		toSend.setStatus(presence.getLoLStatus().toXML());
		
		xmppClient.sendPresence(toSend);
	}

}
