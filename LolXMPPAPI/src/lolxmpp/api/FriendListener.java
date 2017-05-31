package lolxmpp.api;

public interface FriendListener {

	void onMessage(FriendMessage message);

	void onFriendStateChange(Friend friend);
	
}
