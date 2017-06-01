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

import rocks.xmpp.core.stanza.model.Presence.Show;

public enum ChatState {
	CHAT,
	DO_NOT_DISTURB,
	AWAY,
	MOBILE,
	OFFLINE
	;

	public static ChatState from(Show sw) {
		switch(sw) {
			case AWAY:
				return ChatState.AWAY;
			case CHAT:
				return ChatState.CHAT;
			case XA:
				return ChatState.AWAY;
			case DND:
				return ChatState.DO_NOT_DISTURB;
			default:
				//this shouldn't never happen
				return ChatState.MOBILE;
		}
	}

}
