/*
 *  XMPP API to chat and interact with the League of Legends ChatServers. 
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
package lolxmpp.api.presence;

import lolxmpp.api.LoLStatus;
import lolxmpp.api.enums.ChatStatus;
import rocks.xmpp.addr.Jid;

/**
 * @author wirlie
 *
 */
public class UserPresence extends BasicPresence {

	private ChatStatus	show		= ChatStatus.OFFLINE;
	private LoLStatus	lolStatus	= new LoLStatus();

	public UserPresence(Jid id) {
		super(id, "Unknown Name");
	}
	
	public UserPresence(Jid id, String name) {
		super(id, name);
	}
	
	public ChatStatus getChatStatus() {
		return show;
	}
	
	public LoLStatus getLoLStatus() {
		return lolStatus;
	}
	
	public void setChatStatus(ChatStatus status) {
		this.show = status;
	}
	
	public void setLoLStatus(LoLStatus status) {
		this.lolStatus = status;
	}
	
}
