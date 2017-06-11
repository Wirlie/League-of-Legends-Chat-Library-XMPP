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
package lolxmpp.api.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lolxmpp.api.enums.GameQueueConfig;

/**
 * @author wirlie
 *
 */
public class GameInfo {
	/** The ID of the game  **/
	private long gameId;
	/** The game start time represented in epoch milliseconds  **/
	private long gameStartTime;
	/** The ID of the platform on which the game is being played  **/
	private String platformId;
	/** The game mode  **/
	private String gameMode;
	/** The ID of the map **/
	private long mapId;
	/** The game type  **/
	private String gameType;
	/** The queue type. **/
	private long gameQueueConfigId;
	/** The participant information  **/
	private List<GameParticipant> participants = new ArrayList<GameParticipant>();
	/** Banned champion information  **/
	private List<BannedChampionInfo> bannedChampions = new ArrayList<BannedChampionInfo>();
	
	private GameInfo() { }
	
	/** 
	 * @return The ID of the game  
	 */
	public long getGameId() {
		return gameId;
	}
	
	/**
	 * @return The game start time represented in epoch milliseconds
	 */
	public long getGameStartTime() {
		return gameStartTime;
	}
	
	/**
	 * @return The ID of the platform on which the game is being played
	 */
	public String getPlatformId() {
		return platformId;
	}
	
	/**
	 * @return The game mode
	 */
	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * @return The game mode
	 */
	public long getMapId() {
		return mapId;
	}
	
	/**
	 * @return The game type
	 */
	public String getGameType() {
		return gameType;
	}
	
	/** 
	 * @return The queue type
	 */
	public GameQueueConfig getGameQueueConfig() {
		return GameQueueConfig.fromId(gameQueueConfigId);
	}
	
	/**
	 * @return The participant information
	 */
	public List<GameParticipant> getParticipants() {
		return participants;
	}
	
	/**
	 * @return Banned champion information
	 */
	public List<BannedChampionInfo> getBannedChampions() {
		return bannedChampions;
	}
	
	@SuppressWarnings("javadoc")
	public static GameInfo fromJson(String json) {
		try {
			Gson gson = new GsonBuilder().create();
			return gson.fromJson(json, GameInfo.class);
		} catch (JsonSyntaxException e) {
			System.err.println("Error parsing json object.");
			System.err.println("json: " + json);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static class GameParticipant {
		private long profileIconId;
		private long championId;
		private String summonerName;
		private boolean bot;
		private long teamId;
		private long spell2Id;
		private long spell1Id;
		private long summonerId;
		private List<Rune> runes = new ArrayList<Rune>();
		private List<Mastery> masteries = new ArrayList<Mastery>();
		
		/** @return The ID of the profile icon used by this participant  */
		public ProfileIcon getProfileIcon() {
			return new ProfileIcon((int) profileIconId);
		}
		
		/** @return The ID of the champion played by this participant  */
		public long getChampionId() {
			return championId;
		}
		
		/** @return The summoner name of this participant */
		public String getSummonerName() {
			return summonerName;
		}
		
		/** @return Flag indicating whether or not this participant is a bot */
		public boolean botParticipant() {
			return bot;
		}
		
		/** @return The team ID of this participant, indicating the participant's team  */
		public long teamId() {
			return teamId;
		}
		
		/** @return The ID of the second summoner spell used by this participant */
		public long spell2Id() {
			return spell2Id;
		}
		
		/** @return The ID of the first summoner spell used by this participant */
		public long spell1Id() {
			return spell1Id;
		}
		
		/** @return The summoner ID of this participant  */
		public long summonerId() {
			return summonerId;
		}
		
		/** @return The runes used by this participant */
		public List<Rune> getRunes() {
			return runes;
		}
		
		/** @return The masteries used by this participant */
		public List<Mastery> getMasteries() {
			return masteries;
		}
	}
	
	public static class BannedChampionInfo {
		private int pickTurn;
		private long championId;
		private long teamId;
		
		/** @return The turn during which the champion was banned  */
		public int getPickTurn() {
			return pickTurn;
		}
		
		/** @return The ID of the banned champion */
		public long getChampionId() {
			return championId;
		}
		
		/** @return The ID of the team that banned the champion  */
		public long getTeamId() {
			return teamId;
		}
	}

}
