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
public enum QueueType {
	NONE,
	NORMAL,
	NORMAL_3x3,
	ODIN_UNRANKED,
	ARAM_UNRANKED_5x5,
	BOT,
	BOT_3x3,
	RANKED_SOLO_5x5,
	RANKED_TEAM_3x3,
	RANKED_TEAM_5x5,
	ONEFORALL_5x5,
	FIRSTBLOOD_1x1,
	FIRSTBLOOD_2x2,
	SR_6x6,
	CAP_5x5,
	URF,
	URF_BOT,
	NIGHTMARE_BOT,
	ASCENSION,
	HEXAKILL,
	KING_PORO,
	COUNTER_PICK,
	BILGEWATER,
	SIEGE,
	RANKED_FLEX_TT,
	RANKED_FLEX_SR,
	ASSASSINATE,
	DARKSTAR;
}
