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

import java.util.List;

import rocks.xmpp.core.stanza.model.Text;

public class FriendMessage {
	
	private Friend f;
	private String fullMessage = "";

	public FriendMessage(Friend f, List<Text> bodies) {
		bodies.forEach(text -> {
			fullMessage += text.getText() + " ";
		});
		
		this.f = f;
	}
	
	public Friend getFriend() {
		return f;
	}
	
	public String getMessage() {
		return fullMessage;
	}

}
