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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stanza.model.Presence.Show;
import rocks.xmpp.im.roster.model.Contact;

public class Friend {
	
	private Contact contact;
	private LoLXMPPAPI api;
	private boolean isOnline = false;
	private ChatStatus show = ChatStatus.OFFLINE;
	private GameStatus gameStatus = null;
	private ProfileIcon profileIcon = new ProfileIcon(0);

	protected Friend(LoLXMPPAPI api, Contact contact) {
		this.contact = contact;
		this.api = api;
	}

	protected void updatePresence(Presence presence) {
		if(presence.isAvailable()) {
			Show sw = presence.getShow();
			if(sw != null) {
				show = ChatStatus.from(sw);
			} else {
				show = ChatStatus.MOBILE;
				gameStatus = GameStatus.MOBILE;
			}
			
			String status = presence.getStatus();
			if(status != null) {
				
				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(new InputSource(new StringReader(status)));
					
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath path = xPathfactory.newXPath();
					
					try {
						XPathExpression expr = path.compile("body/gameStatus");
						gameStatus = GameStatus.fromXmlValue(expr.evaluate(document));
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
					
					try {
						XPathExpression expr = path.compile("body/profileIcon");
						profileIcon = new ProfileIcon(((Double) expr.evaluate(document, XPathConstants.NUMBER)).intValue());
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
				} catch (ParserConfigurationException | SAXException | IOException e) {
					e.printStackTrace();
					gameStatus = GameStatus.OUT_OF_GAME;
				}
			}
			
			if(!isOnline) {
				isOnline = true;
				api.handleFriendJoinEvent(this);
			}
		} else {
			show = ChatStatus.OFFLINE;

			if(isOnline) {
				isOnline = false;
				api.handleFriendLeaveEvent(this);
			}
		}
	}
	
	public boolean sendMessage(String message) {
		XmppClient client = api.getXMPPClient();
		
		if(client.isConnected()) {
			Message xmppMsg = new Message(contact.getJid(), Message.Type.CHAT, message);
			client.sendMessage(xmppMsg);
			return true;
		}
		
		return false;
	}
	
	public String getName() {
		return contact.getName();
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
	public ChatStatus getChatStatus() {
		return show;
	}
	
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	
	public ProfileIcon getProfileIcon() {
		return profileIcon;
	}
	
	public String getId() {
		return contact.getJid().getLocal();
	}

}
