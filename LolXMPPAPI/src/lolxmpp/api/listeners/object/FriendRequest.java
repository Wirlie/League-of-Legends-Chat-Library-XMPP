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

import lolxmpp.api.LoLXMPPAPI;
import lolxmpp.api.presence.BasicPresence;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.im.subscription.PresenceManager;

/**
 * @author wirlie
 *
 */
public class FriendRequest {

	private BasicPresence	from;
	private LoLXMPPAPI		api;

	private boolean			processed	= false;
	
	public FriendRequest(LoLXMPPAPI api, BasicPresence from) {
		this.from = from;
		this.api = api;
	}
	
	public BasicPresence getRequester() {
		return from;
	}
	
	public void accept() {
		if(processed) {
			throw new IllegalStateException("This request already was accepted/rejected!");
		}
		
		processed = true;
		
		XmppClient client = api.getXMPPClient();
		PresenceManager presenceManager = client.getManager(PresenceManager.class);
		presenceManager.approveSubscription(from.getXMPPJid());
		
		api.removeRequest(this);
	}
	
	public void deny() {
		if(processed) {
			throw new IllegalStateException("This request already was accepted/rejected!");
		}
		
		processed = true;
		
		XmppClient client = api.getXMPPClient();
		PresenceManager presenceManager = client.getManager(PresenceManager.class);
		presenceManager.denySubscription(from.getXMPPJid());
		
		api.removeRequest(this);
	}

}
