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

import com.google.gson.JsonObject;

import lolxmpp.api.data.GameInfo;
import lolxmpp.api.enums.ChatStatus;
import lolxmpp.api.enums.GameStatus;
import lolxmpp.api.exceptions.APIException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stanza.model.Presence.Show;
import rocks.xmpp.im.roster.model.Contact;

public class Friend extends UserPresence {
	
	private Contact contact;
	private LoLXMPPAPI api;
	private boolean isOnline = false;

	protected Friend(LoLXMPPAPI api, Contact contact) {
		super(contact.getJid().getLocal());
		this.contact = contact;
		this.api = api;
	}

	protected LoLStatus updatePresence(Presence presence) {
		LoLStatus oldLoLStatus = (LoLStatus) getLoLStatus().clone();
		
		if(presence.isAvailable()) {
			Show sw = presence.getShow();
			
			if(sw != null) {
				setChatStatus(ChatStatus.from(sw));
			} else {
				setChatStatus(ChatStatus.MOBILE);
				
				GameStatus gameStatus = GameStatus.MOBILE;
				getLoLStatus().setGameStatus(gameStatus);
			}
			
			String status = presence.getStatus();
			if(status != null) {
				setLoLStatus(new LoLStatus(status));
			}
			
			if(!isOnline) {
				isOnline = true;
				api.handleFriendJoinEvent(this);
			}
		} else {
			setChatStatus(ChatStatus.OFFLINE);

			if(isOnline) {
				isOnline = false;
				setLoLStatus(new LoLStatus());
				api.handleFriendLeaveEvent(this);
			}
		}
		
		return oldLoLStatus;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Friend[name=" + getName() + ",id=" + getId() + ",isOnline=" + isOnline() + ",chatStatus=" + getChatStatus().toString() + ",lolStatus=" + getLoLStatus().toString() + "]";
	}
	
	public GameInfo getCurrentGameInfo() {
		if(getLoLStatus().getGameStatus() == GameStatus.IN_GAME) {
			try {
				JsonObject jobject = api.getRiotAPI().makeRequest("/lol/spectator/v3/active-games/by-summoner/" + getSummonerId());
				return GameInfo.fromJson(jobject.toString());
			} catch (APIException e) {
				e.printStackTrace();
			};
		}
		
		return null;
	}
}
