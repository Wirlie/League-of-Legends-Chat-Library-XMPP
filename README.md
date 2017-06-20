Pending documentation ...

**Important:** Library in beta stage. Please report any bug.

#### Downloads:
Latest Build: https://ci.appveyor.com/project/Wirlie/league-of-legends-chat-library-xmpp/build/artifacts
#### Usage:
**Login:**
```JAVA
LoLXMPPAPI api = new LoLXMPPAPI(ChatRegion.NA, new RiotAPI("RGAPI-XXX-XXX-XXX-XXX-XXX"));
String lolUsername = "Wirlie";
String lolPassword = "xxxxxxxxx";
if(api.login(lolUsername, lolPassword)) {
  api.ready(() -> {
    UserPresence selfPresence = api.getPresence(); //(optional) get presence
    System.out.println(String.format("Welcome %s!!", selfPresence.getName());
    ...
  }
} else {
  if(api.getLoginResult() == LoginResult.AUTH_FAILED) {
    //wrong username or password
  } else {
    //internal API error
  }
}
```
**Update Presence:**
```JAVA
... //Login
api.ready(() -> {
    UserPresence presence = api.getPresence();
    /*
     * Set Chat Status
     */
    presence.setChatStatus(ChatStatus.AWAY);
    /*
     * Change LoLStatus
     */
    LoLStatus lolStatus = presence.getLoLStatus();
    lolStatus.setGameStatus(GameStatus.IN_GAME);
    ...
    ...
    /*
     * Send modified presence
     */
    api.setPresence(presence);
}
```
**Listening Events:**
```JAVA
... //Login
api.ready(() -> {
    /*
     * FriendStatusChangeListener is triggered when a some Friend change his/her LoL or Chat status.
     */
    api.addFriendStatusChangeListener(event -> {
        LoLStatus oldStatus = event.oldStatus(); //get old status, before event
        Friend friend = event.getFriend(); //the friend implied on this event
        LoLStatus newStatus = friend.getLoLStatus(); //new status
    });
    
    /*
     * FriendLeaveListener is triggered when a Friend change his/her status from online to offline
     */
    api.addFriendLeaveListener(friend -> {
        ...
    });
    
    /*
     * FriendJoinListener is triggered when a Friend change his/her status from offline to online
     */
    api.addFriendJoinListener(friend -> {
        ...
    });
    
    /*
     * MessageListener is triggered when a some friend sends a message to you.
     */
    api.addMessageListener(event -> {
        Friend from = event.getFriend(); //author
        String message = event.getMessage(); //message
    });
    
    /*
     * FriendAddListener is triggered after you has accepted a friend request of some summoner (or in other words,
     * when a Friend is added to your contacts).
     */
    api.addFriendAddListener(friend -> {
        
    });
    
    /*
     * FriendRemoveListener is triggered when a some Friend was removed from your contacts, because of:
     *      a) you have removed a Friend
     *      b) your Friend has removed you from his/her contacts
     */
    api.addFriendRemoveListener(friend -> {
        
    });
    
    /*
     * FriendRequestListener is triggered when a some summoner sends a Friend Request to you.
     *
     * IMPORTANT: This listener isn't triggered for Friend Requests that you already has before the API initialization.
     *            In such case, you can use: api.getPendingApproveRequests() for get all pending requests that you has.
     */
    api.addFriendRequestListener(request -> {
        BasicPresence requester = request.getRequester();
        ...
        request.accept(); //Accept request (FriendAddListener is fired if you accept a request)
        //request.deny(); //Deny request
    });
});
```
**Friends:**
```JAVA
... //Login
api.ready(() -> {
    for(Friend f : api.getAllFriends()) { //all friends
        ...
    }
    
    for(Friend f : api.getOnlineFriends()) { //online friends
        ...
    }
    
    for(Friend f : api.getOfflineFriends()) { //offline friends
        ...
    }
    
    for(Friend f : api.getFriends(ChatStatus.AWAY) { //get friends with away status
        ...
    }
    
    for(Friend f : api.getFriends(GameStatus.IN_GAME) { //get friends in game
        ...
    }
    
    /*
     * Retrieve friend by name
     */
    Friend friend = api.getFriendByName(...);
    
    /*
     * Retrieve friend by ID (Friend#getId())
     */
    Friend friend = api.getFriendById(...);
});
```
**Send Message to Friend**
```JAVA
... //Login
api.ready(() -> {
     Friend friend = ...;
     friend.sendMessage("Hello!");
});
```
**Get Profile Icon of any Friend**
```JAVA
... //Login
api.ready(() -> {
    Friend friend = ...;
    ProfileIcon icon = friend.getLoLStatus().getProfileIcon(); //Retrieve Profile Icon
    icon.getImage(width, height); //Retrieve Image with the specified size
});
```
**Send Friend Request to Friend**
```JAVA
... //Login
api.ready(() -> {
    long summonerID = 166196; //in this example, 166196 = Wirlie's Summoner ID
    api.sendRequest(166196);
});
```
More examples: https://github.com/Wirlie/League-of-Legends-Chat-Library-XMPP/tree/master/examples
