package lolxmpp.api;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import rocks.xmpp.core.session.debug.ConsoleDebugger;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;

public class LoLXMPPAPI {
	
	private LoginResult loginResult = LoginResult.NOT_LOGGED;
	private ChatRegion region;
	private XmppClient xmppClient;
	private List<MessageListener> messageListeners = new ArrayList<MessageListener>();
	private ExecutorService  eventsThreadPool = null;
	private Map<Jid, Friend> friends = new HashMap<Jid, Friend>();
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
                    .debugger(ConsoleDebugger.class)
                    .build();
			
			xmppClient = XmppClient.create("pvp.net", configuration, tcpConfiguration);
			
			try {
				//falló la conexión con el cliente
				xmppClient.connect();
			} catch (XmppException e) {
				e.printStackTrace();
				loginResult = LoginResult.XMPP_CLIENT_ERROR;
				return false;
			}
			
			try {
				xmppClient.login(username, "AIR_" + password, "xiff");
				xmppClient.send(new Presence(Presence.Show.CHAT));
				loginResult = LoginResult.AUTH_SUCCESS;
				
				addXmppClientListeners();
				
				return true;
			} catch (XmppException e) {
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
	
	private void addXmppClientListeners() {
		eventsThreadPool = Executors.newFixedThreadPool(1);
		eventsThreadPool.execute(() -> {
			RosterManager roster = xmppClient.getManager(RosterManager.class);
			
			//Cargar contactos
			for(Contact contact : roster.getContacts()) {
				friends.put(contact.getJid(), new Friend(xmppClient, contact));
			}
			
			//Listador de presencias
			xmppClient.addInboundPresenceListener(e -> {
				Presence presence = e.getPresence();
				Contact contact = roster.getContact(presence.getFrom());
				
				if(contact != null) {
					if(friends.containsKey(contact.getJid())) {
						friends.get(contact.getJid()).updatePresence(e.getPresence());
					} else {
						Friend friend = new Friend(xmppClient, contact);
						friend.updatePresence(e.getPresence());
						friends.put(contact.getJid(), friend);
					}
				}
			});
			
			//Listador de mensajes
			xmppClient.addInboundMessageListener(e -> {
				synchronized(messageListeners) {
					for(MessageListener listener : messageListeners) {
						
					}
				}
			});
			
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
	
	public void addMessageListener(MessageListener listener) {
		messageListeners.add(listener);
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

}
