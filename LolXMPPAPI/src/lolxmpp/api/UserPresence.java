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
package lolxmpp.api;

import lolxmpp.api.enums.ChatStatus;

/**
 * @author wirlie
 *
 */
public class UserPresence {

	private ChatStatus show = ChatStatus.OFFLINE;
	private LoLStatus lolStatus = new LoLStatus();
	private String presenceId;
	
	public UserPresence(String id) {
		this.presenceId = id;
	}
	
	public String getId() {
		return presenceId;
	}
	
	public long getSummonerId() {
		String parse = presenceId.replace("sum", "");
		try {
			return Long.parseLong(parse);
		} catch (NumberFormatException e) {
			System.err.println("Error parsing summoner ID, invalid long: " + parse + " from full id: " + presenceId);
			e.printStackTrace();
		}
		
		return -1;
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
