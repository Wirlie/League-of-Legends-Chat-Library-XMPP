package lolxmpp.api;

public enum ChatRegion {
	
	LAN("chat.la1.lol.riotgames.com"),
	;
	
	private String serverHost;
	
	ChatRegion(String serverHost) {
		this.serverHost = serverHost;
	}
	
	public String serverHost() {
		return serverHost;
	}

}
