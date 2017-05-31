package lolxmpp.api;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.im.roster.model.Contact;

public class Friend {
	
	private Contact contact;
	private XmppClient client;
	private boolean isOnline = false;

	protected Friend(XmppClient client, Contact contact) {
		this.contact = contact;
		this.client = client;
	}

	protected void updatePresence(Presence presence) {
		System.out.println(contact.getJid());
		System.out.println(contact.getName());
		System.out.println(presence.getStatus());
		System.out.println(presence.getShow());
		
		String status = presence.getStatus();
		
		if(status != null) {
			org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
			
			try {
			    org.jdom.Document doc = saxBuilder.build(new StringReader(presence.getStatus()));
			    String message = doc.getRootElement().getText();
			    System.out.println("STATUS:" + message);
			} catch (JDOMException e) {
				System.out.println("JDOM Invalido: Estado Invalido:");
				System.out.println(presence.getStatus());
			} catch (IOException e) {
			    // handle IOException
			}
		}
	}
	
	public boolean sendMessage(String message) {
		if(client.isConnected()) {
			Message xmppMsg = new Message(contact.getJid(), Message.Type.CHAT, message);
			client.sendMessage(xmppMsg);
			return true;
		}
		
		System.err.println("ERROR AL ENVIAR MENSAJE: Cliente XMPP no conectado.");
		return false;
	}
	
	public String getName() {
		return contact.getName();
	}

}
