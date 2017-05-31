
package lolxmpp.api;

import java.util.List;

import rocks.xmpp.core.stanza.model.Text;

public class FriendMessage {
	
	private Friend f;
	private String fullMessage = "";

	public FriendMessage(Friend f, List<Text> bodies) {
		bodies.forEach(text -> {
			fullMessage += text.getText() + " ";
		});
		
		this.f = f;
	}
	
	public Friend getFriend() {
		return f;
	}
	
	public String getMessage() {
		return fullMessage;
	}

}
