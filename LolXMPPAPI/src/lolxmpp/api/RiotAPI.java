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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * @author wirlie
 *
 */
public class RiotAPI {
	
	private String key;
	private ChatRegion region;
	
	public RiotAPI(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	protected void setRegion(ChatRegion region) {
		this.region = region;
	}
	
	public JsonObject makeRequest(String apiPath) {
		String apiHost = region.apiHost();
		String tryUrl = "https://" + apiHost + apiPath;
		try {
			URL url = new URL(tryUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("X-Riot-Token", key);
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("[RAPI] Request Failed. HTTP Error Code: " + connection.getResponseCode() + ", URL: " + tryUrl);
			}
			
			JsonElement element = new JsonParser().parse(new JsonReader(new InputStreamReader(connection.getInputStream())));
			if(!element.isJsonNull()) {
				return element.getAsJsonObject();
			}
		} catch (MalformedURLException e) {
			System.err.println("[RAPI] Something's wrong!");
			System.err.println("[RAPI] Malformed URL: " + tryUrl);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("[RAPI] Something's wrong!");
			System.err.println("[RAPI] IOException");
			e.printStackTrace();
		}
		
		return new JsonObject();
	}

}
