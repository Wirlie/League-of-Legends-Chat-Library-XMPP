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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lolxmpp.api.RiotAPI;
import lolxmpp.api.exceptions.APIException;

/**
 * @author wirlie
 *
 */
public class Mastery {
	
	transient private static Map<Long, BufferedImage> images = new HashMap<Long, BufferedImage>();
	transient private static Map<Long, String> names = new HashMap<Long, String>();
	transient private static Map<Long, String> masteryTree = new HashMap<Long, String>();
	transient private static Map<Long, Integer> ranks = new HashMap<Long, Integer>();
	transient private static Map<Long, String[]> descriptions = new HashMap<Long, String[]>();
	
	private long masteryId;
	private int rank;
	
	/** @return The ID of the mastery */
	public long getMasteryId() {
		return masteryId;
	}
	
	/** @return The number of points put into this mastery by the user */
	public int getRank() {
		return rank;
	}
	
	/** @return The max number of points that can be assigned by the user */
	public int getMaxRank() {
		return ranks.get(masteryId);
	}
	
	/** @return The name of this mastery */
	public String getName() {
		return names.get(masteryId);
	}
	
	/** @return The mastery tree of this mastery */
	public String getMasteryTree() {
		return masteryTree.get(masteryId);
	}
	
	/** 
	 * @param rank Rank of the mastery
	 * @return The description associated to the provided rank 
	 * */
	public String getDescription(int rank) {
		return descriptions.get(masteryId)[rank];
	}
	
	/** @return All descriptions of all ranks associated to this mastery */
	public String[] getDescriptions() {
		return descriptions.get(masteryId);
	}
	
	/** @return Get a BufferedImage that contains the icon of this mastery, default size: 48x48px. Use
	 * {@link #getImage(int, int)} if you want a image with another size.
	 */
	public BufferedImage getImage() {
		return images.get(masteryId);
	}
	
	/** 
	 * @param w Width in pixels
	 * @param h Height in pixels
	 * @return Retrieve this mastery icon with the specified size. <br>
	 * <b>note: </b> this method returns an Image instead of BufferedImage, use
	 * {@link #getImage()} if you need a BufferedImage. */
	public Image getImage(int w, int h) {
		return images.get(masteryId).getScaledInstance(w, h, Image.SCALE_SMOOTH);
	}
	
	/**
	 * @deprecated Only for API initialization.
	 * @param api RiotAPI Key
	 */
	public static void loadMasteryIcons(RiotAPI rapi) {
		try {
			JsonObject jobject = rapi.makeRequest(" /lol/static-data/v3/masteries?tags=all");
			if(jobject.has("version")) {
				String currentVersion = jobject.get("version").getAsString();
				String spriteUrl = "http://ddragon.leagueoflegends.com/cdn/" + currentVersion + "/img/sprite/mastery0.png";
				BufferedImage image = ImageIO.read(new URL(spriteUrl));
				
				jobject.getAsJsonArray("data").forEach(element -> {
					JsonObject arrObj = element.getAsJsonObject();
					long id = arrObj.get("id").getAsLong();
					
					String name = arrObj.get("name").getAsString();
					names.put(id, name);
					
					String masteryTreeStr = arrObj.get("masteryTree").getAsString();
					masteryTree.put(id, masteryTreeStr);
					
					int rank = arrObj.get("ranks").getAsInt();
					ranks.put(id, rank);
					
					String[] descriptionsr = new String[rank];
					
					Iterator<JsonElement> it = arrObj.getAsJsonArray("description").iterator();
					
					int i = 0;
					while(it.hasNext()) {
						descriptionsr[i] = it.next().getAsString();
						i++;
					}
					
					descriptions.put(id, descriptionsr);
					
					JsonObject imageObject = arrObj.get("image").getAsJsonObject();
					int w = imageObject.get("w").getAsInt();
					int h = imageObject.get("h").getAsInt();
					int x = imageObject.get("x").getAsInt();
					int y = imageObject.get("y").getAsInt();
					
					images.put(id, image.getSubimage(x, y, w, h));
				});
			}
		} catch (APIException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
