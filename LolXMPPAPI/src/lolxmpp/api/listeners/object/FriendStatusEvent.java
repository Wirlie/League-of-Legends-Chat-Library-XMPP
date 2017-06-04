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
package lolxmpp.api.listeners.object;

import lolxmpp.api.Friend;

/**
 * @author wirlie
 *
 */
public class FriendStatusEvent {
	
	private boolean chatStatusChanged;
	private boolean gameStatusChanged;
	private boolean statusMessageChanged;
	private boolean profileIconChanged;
	private Friend f;
	
	public FriendStatusEvent(Friend f, boolean[] changed) {
		/*
		 * boolean[] changed:
		 * [0] = chatStatusChanged
		 * [1] = gameStatusChanged
		 * [2] = statusMessageChanged
		 * [3] = profileIconChanged
		 */
		
		this.chatStatusChanged = changed[0];
		this.gameStatusChanged = changed[1];
		this.statusMessageChanged = changed[2];
		this.profileIconChanged = changed[3];
		this.f = f;
	}
	
	public boolean chatStatusChanged() {
		return chatStatusChanged;
	}
	
	public boolean gameStatusChanged() {
		return gameStatusChanged;
	}
	
	public boolean statusMessageChanged() {
		return statusMessageChanged;
	}
	
	public boolean profileIconChanged() {
		return profileIconChanged;
	}
	
	public Friend getFriend() {
		return f;
	}

}
