Pending documentation ...

**Important:** Library in alpha stage, this means that the API may not work properly, or as expected, please be patient.

#### Downloads:
Latest Build: https://ci.appveyor.com/project/Wirlie/league-of-legends-chat-library-xmpp/build/artifacts
#### Usage:
**Login:**
```JAVA
LoLXMPPAPI api = new LoLXMPPAPI(ChatRegion.NA, new RiotAPI("RGAPI-XXX-XXX-XXX-XXX-XXX"));
String lolUsername = "Wirlie";
String lolPassword = "xxxxxxxxx";
if(api.login(lolUsername, lolPassword)) {
  api.onReady(() -> {
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
**Change Presence**
```JAVA
... //Login
api.onReady(() -> {
    UserPresence presence = api.getPresence();
    /*
     * OFFLINE = Invisible
     * CHAT = Online
     * AWAY = Away
     * DO_NOT_DISTURB = Do not disturb
     */
    presence.setChatStatus(ChatStatus.CHAT);
    /*
     * Change LoLStatus
     */
    LoLStatus lolStatus = presence.getLoLStatus();
    lolStatus.setGameStatus(GameStatus.IN_GAME);
    ...
    /*
     * Update presence
     */
    api.setPresence(presence);
}
```
**Listening events**
```JAVA
... //Login
api.onReady(() -> {
    api.addFriendStatusChangeListener(event -> {
        LoLStatus oldStatus = event.oldStatus(); //get old status, before event
        Friend friend = event.getFriend(); //get friend implied on this event
        LoLStatus newStatus = friend.getLoLStatus(); //get new status
    });
    
    api.addFriendLeaveListener(friend -> {
        ...
    });
    
    api.addFriendJoinListener(friend -> {
        ...
    });
    
    api.addMessageListener(event -> {
        Friend fromFriend = event.getFriend();
        String message = event.getMessage();
    });
});
```
**Get Friends**
```JAVA
... //Login
api.onReady(() -> {
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
     * Retrieve friend by ID (getId())
     */
    Friend friend = api.getFriendById(...);
});
```
**Send Message**
```JAVA
... //Login
api.onReady(() -> {
     Friend friend = ...;
     friend.sendMessage("Hello!");
});
```
**Get Friend Profile Icon Image**
```JAVA
... //Login
api.onReady(() -> {
    Friend friend = ...;
    friend.getLoLStatus().getProfileIcon().getImage(width, height); //Retrieve image
});
```
More examples: https://github.com/Wirlie/League-of-Legends-Chat-Library-XMPP/tree/master/examples
