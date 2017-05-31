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
