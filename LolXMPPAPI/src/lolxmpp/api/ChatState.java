package lolxmpp.api;

import rocks.xmpp.core.stanza.model.Presence.Show;

public enum ChatState {
	CHAT,
	DO_NOT_DISTURB,
	AWAY,
	MOBILE,
	OFFLINE
	;

	public static ChatState from(Show sw) {
		switch(sw) {
			case AWAY:
				return ChatState.AWAY;
			case CHAT:
				return ChatState.CHAT;
			case XA:
				return ChatState.AWAY;
			case DND:
				return ChatState.DO_NOT_DISTURB;
			default:
				//this shouldn't never happen
				return ChatState.MOBILE;
		}
	}

}
