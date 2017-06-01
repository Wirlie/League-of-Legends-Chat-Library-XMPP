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

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author wirlie
 *
 */
public class ProfileIcon {
	
	private static String iconImagesUrl = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/%d.png";
	private int id;
	
	public ProfileIcon(int id) {
		if(id < 0) {
			id = 0;
		}
		
		this.id = id;
	}
	
	public Image getImage() {
		try {
			URL tryUrl = new URL(String.format(iconImagesUrl, id));
			try{
				return ImageIO.read(tryUrl);
			} catch (IOException e) {
				tryUrl = new URL(String.format(iconImagesUrl, 0));
				try {
					return ImageIO.read(tryUrl);
				} catch (IOException e2) {
					//Cannot retrieve default profile icon
					e2.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getId() {
		return id;
	}
}
