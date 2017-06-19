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
package lolxmpp.api.enums;

/**
 * @author wirlie
 *
 */
public enum GameStatus {
	TEAM_SELECT("teamSelect"),
	HOSTING_NORMAL_GAME("hostingNormalGame"),
	HOSTING_PRACTICE_GAME("hostingPracticeGame"),
	HOSTING_RANKED_GAME("hostingRankedGame"),
	HOSTING_COOP_VS_AI_GAME("hostingCoopVsAIGame"),
	IN_QUEUE("inQueue"),
	SPECTATING("spectating"),
	OUT_OF_GAME("outOfGame"),
	CHAMPION_SELECT("championSelect"),
	IN_GAME("inGame"),
	MOBILE("mobile"),
	TUTORIAL("tutorial"),
	HOSTING_NIGHTMARE_BOT_GAME("hosting_NIGHTMARE_BOT"),
	HOSTING_BOT_3x3("hosting_BOT_3x3")
	;
	
	private String xmlValue;
	
	GameStatus(String xmlValue) {
		this.xmlValue = xmlValue;
	}
	
	public String xmlValue() {
		return xmlValue;
	}
	
	public static GameStatus fromXmlValue(String xmlValue) {
		for(GameStatus state : values()) {
			if(state.xmlValue.equalsIgnoreCase(xmlValue)) {
				return state;
			}
		}
		
		System.err.println("Unknown game status: " + xmlValue);
		
		return GameStatus.OUT_OF_GAME;
	}
}
