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
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import lolxmpp.api.enums.GameStatus;

/**
 * @author wirlie
 *
 */
public class LoLStatus implements Cloneable {
	
	private GameStatus gameStatus = GameStatus.OUT_OF_GAME;
	//tier
	private int rankedWins = 0;
	private int rankedLosses = 0;
	private String rankedLeagueName = null;
	//ranked league division
	//ranked league tier
	//ranker league queue
	//game queue type
	//is observable
	private long timeStamp = 0L;
	//map id
	//skinname
	//championid
	private int profileIconId = 0;
	private String statusMessage = null;
	//skin variant
	private int level = 0;
	
	private static enum XMLProperties {
		GAME_STATUS("gameStatus"),
		TIER("tier"),
		RANKED_WINS("rankedWins"),
		RANKED_LOSSES("rankedLosses"),
		RANKED_LEAGUE_NAME("rankedLeagueName"),
		RANKED_LEAGUE_DIVISION("rankedLeagueDivision"),
		RANKED_LEAGUE_TIER("rankedLeagueTier"),
		RANKED_LEAGUE_QUEUE("rankedLeagueQueue"),
		GAME_QUEUE_TYPE("gameQueueType"),
		IS_OBSERVABLE("isObservable"),
		TIMESTAMP("timeStamp"),
		MAP_ID("mapId"),
		SKINNAME("skinname"),
		CHAMPION_ID("championId"),
		PROFILE_ICON("profileIcon"),
		STATUS_MSG("statusMsg"),
		SKIN_VARIANT("skinVariant"),
		LEVEL("level"),
		CLUBS_DATA("clubsData"),
		NOT_IMPLEMENTED(null)
		;
		
		String xmlTag;
		
		XMLProperties(String xmlTag) {
			this.xmlTag = xmlTag;
		}
		
		public String getTag() {
			return xmlTag;
		}
		
		@Override
		public String toString() {
			return xmlTag;
		}
		
		public static XMLProperties fromXmlNode(String nodeName) {
			for(XMLProperties value : values()) {
				if(value.getTag() != null && value.getTag().equalsIgnoreCase(nodeName)) {
					return value;
				}
			}
			
			return XMLProperties.NOT_IMPLEMENTED;
		}
	}
	
	public LoLStatus(String status) {
		if(status != null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(status)));
				
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath path = xPathfactory.newXPath();
				
				try {
					XPathExpression expr = path.compile("//body/*");
					NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
					for(int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						XMLProperties property = XMLProperties.fromXmlNode(node.getNodeName());
						
						if(property == XMLProperties.NOT_IMPLEMENTED) {
							System.err.println("Status XMLNode not implemented: " + node.getNodeName());
							continue;
						}
						
						String nodeText = node.getTextContent();
						if(nodeText == null || nodeText.isEmpty()) {
							continue;
						}
						
						try {
							switch(property) {
								case CHAMPION_ID:
									break;
								case GAME_QUEUE_TYPE:
									break;
								case GAME_STATUS:
									this.gameStatus = GameStatus.fromXmlValue(nodeText);
									break;
								case IS_OBSERVABLE:
									break;
								case LEVEL:
									this.level = Integer.parseInt(nodeText);
									break;
								case MAP_ID:
									break;
								case NOT_IMPLEMENTED:
									break;
								case PROFILE_ICON:
									this.profileIconId = Integer.parseInt(nodeText);
									break;
								case RANKED_LEAGUE_DIVISION:
									break;
								case RANKED_LEAGUE_NAME:
									this.rankedLeagueName = nodeText;
									break;
								case RANKED_LEAGUE_QUEUE:
									break;
								case RANKED_LEAGUE_TIER:
									break;
								case RANKED_LOSSES:
									this.rankedLosses = Integer.parseInt(nodeText);
									break;
								case RANKED_WINS:
									this.rankedWins = Integer.parseInt(nodeText);
									break;
								case SKINNAME:
									break;
								case SKIN_VARIANT:
									break;
								case STATUS_MSG:
									this.statusMessage = nodeText;
									break;
								case TIER:
									break;
								case TIMESTAMP:
									this.timeStamp = Long.parseLong(nodeText);
									break;
								case CLUBS_DATA:
									break;
								default:
									System.err.println("Status XMLProperty not implemented: " + property);
									break;
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public LoLStatus() {
		
	}

	public String toXML() {
		String tagFormatStr = "<%1$s>%2$s</%1$s>";
		String tagFormatInt = "<%1$s>%2$d</%1$s>";
		StringBuilder sb = new StringBuilder("<body>");
		
		sb.append(String.format(tagFormatInt, XMLProperties.RANKED_WINS.getTag(), rankedWins));
		sb.append(String.format(tagFormatInt, XMLProperties.RANKED_LOSSES.getTag(), rankedLosses));
		
		if(rankedLeagueName != null) {
			sb.append(String.format(tagFormatStr, XMLProperties.RANKED_LEAGUE_NAME.getTag(), rankedLeagueName));
		}
		
		if(timeStamp != 0L) {
			sb.append(String.format(tagFormatInt, XMLProperties.TIMESTAMP.getTag(), timeStamp));
		}
		
		sb.append(String.format(tagFormatInt, XMLProperties.PROFILE_ICON.getTag(), profileIconId));
		
		if(statusMessage != null) {
			sb.append(String.format(tagFormatStr, XMLProperties.STATUS_MSG.getTag(), statusMessage));
		}
		
		if(gameStatus != null) {
			sb.append(String.format(tagFormatStr, XMLProperties.GAME_STATUS.getTag(), gameStatus.xmlValue()));
		}
		
		sb.append(String.format(tagFormatInt, XMLProperties.LEVEL.getTag(), level));
		
		sb.append("</body>");
		return sb.toString();
	}
	
	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	
	public void setRankedLeagueName(String rankedLeagueName) {
		this.rankedLeagueName = rankedLeagueName;
	}
	
	public String getRankedLeagueName() {
		return rankedLeagueName;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setProfileIconId(int profileIconId) {
		this.profileIconId = profileIconId;
	}
	
	public int getProfileIconId() {
		return profileIconId;
	}
	
	public ProfileIcon getProfileIcon() {
		return new ProfileIcon(profileIconId);
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
