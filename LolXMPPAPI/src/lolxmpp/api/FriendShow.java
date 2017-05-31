package lolxmpp.api;

import rocks.xmpp.core.stanza.model.Presence.Show;

public enum FriendShow {
	CHAT,
	DO_NOT_DISTURB,
	AWAY,
	MOBILE,
	OFFLINE
	;

	public static FriendShow from(Show sw) {
		switch(sw) {
			case AWAY:
				return FriendShow.AWAY;
			case CHAT:
				return FriendShow.CHAT;
			case XA:
				return FriendShow.AWAY;
			case DND:
				return FriendShow.DO_NOT_DISTURB;
			default:
				//this shouldn't never happen
				return FriendShow.MOBILE;
		}
	}

}
