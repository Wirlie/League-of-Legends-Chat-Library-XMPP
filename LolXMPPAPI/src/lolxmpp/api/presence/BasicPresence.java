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

import rocks.xmpp.addr.Jid;

/**
 * @author wirlie
 *
 */
public class BasicPresence {
	
	private String	name;
	private Jid		jid;
	
	public BasicPresence(Jid jid) {
		this.jid = jid;
	}
	
	public BasicPresence(Jid jid, String name) {
		this.jid = jid;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @deprecated Only for API purposes.
	 */
	@Deprecated
	public void setName(String name) {
		this.name = name;
	}
	
	public long getSummonerId() {
		String parse = jid.getLocal().replace("sum", "");
		try {
			return Long.parseLong(parse);
		} catch (NumberFormatException e) {
			System.err.println("Error parsing summoner ID, invalid long: " + parse + " from full id: " + jid);
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public String getId() {
		return jid.getLocal();
	}
	
	public Jid getXMPPJid() {
		return jid;
	}

}
