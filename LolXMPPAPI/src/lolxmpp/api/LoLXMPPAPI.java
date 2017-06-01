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
import java.util.function.Consumer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSessionConfiguration;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;

public class LoLXMPPAPI {
	
	private LoginResult loginResult = LoginResult.NOT_LOGGED;
	private ChatRegion region;
	private XmppClient xmppClient;
	private List<FriendListener> friendListeners = new ArrayList<FriendListener>();
	private ExecutorService  eventsThreadPool = null;
	private Map<String, Friend> friends = new HashMap<String, Friend>();
	private boolean ready = false;
	private List<Consumer<Void>> readyListeners = new ArrayList<Consumer<Void>>();
	
	public LoLXMPPAPI(ChatRegion region) {
		this.region = region;
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
		eventsThreadPool = Executors.newFixedThreadPool(1);
		eventsThreadPool.execute(() -> {
			RosterManager roster = xmppClient.getManager(RosterManager.class);
			
			//Read roster contacts
			for(Contact contact : roster.getContacts()) {
				friends.put(contact.getJid().getLocal(), new Friend(xmppClient, contact));
			}
			
			//Add Incoming Presence Listener
			xmppClient.addInboundPresenceListener(e -> {
				Presence presence = e.getPresence();
				Contact contact = roster.getContact(presence.getFrom());
				
				if(contact != null) {
					if(friends.containsKey(contact.getJid().getLocal())) {
						Friend f = friends.get(contact.getJid().getLocal());
						f.updatePresence(e.getPresence());
						
						synchronized(friendListeners) {
							friendListeners.forEach(listener -> {
								listener.onFriendStateChange(f);
							});
						}
					} else {
						Friend friend = new Friend(xmppClient, contact);
						friend.updatePresence(e.getPresence());
						friends.put(contact.getJid().getLocal(), friend);
					}
				}
			});
			
			//Add Incoming Message Listener
			xmppClient.addInboundMessageListener(e -> {
				synchronized(friendListeners) {
					friendListeners.forEach(listener -> {
						Message msg = e.getMessage();
						Friend f = getFriendByJid(msg.getFrom());
						if(f == null) {
							System.err.println("Something is wrong! Incoming message from unknown friend.");
						} else {
							listener.onMessage(new FriendMessage(f, msg.getBodies()));
						}
					});
				}
			});
			
			//Notify API is ready for use
			ready = true;
			notifyApiReady();
		});
	}
	
	private void notifyApiReady() {
		for(Consumer<Void> c : readyListeners) {
			c.accept(null);
		}
		
		readyListeners.clear();
	}
	
	public void addFriendListener(FriendListener listener) {
		friendListeners.add(listener);
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
	
	public boolean isReady() {
		return ready;
	}

	public void onReady(Consumer<Void> e) {
		if(isReady()) {
			e.accept(null);
		} else {
			readyListeners.add(e);
		}
	}

	public Collection<Friend> getAllFriends() {
		return new ArrayList<Friend>(friends.values());
	}

}
