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
api.onReady(() -> {
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
api.onReady(() -> {
    api.addFriendStatusChangeListener(event -> {
        LoLStatus oldStatus = event.oldStatus(); //get old status, before event
        Friend friend = event.getFriend(); //the friend implied on this event
        LoLStatus newStatus = friend.getLoLStatus(); //new status
    });
    
    api.addFriendLeaveListener(friend -> {
        ...
    });
    
    api.addFriendJoinListener(friend -> {
        ...
    });
    
    api.addMessageListener(event -> {
        Friend from = event.getFriend(); //author
        String message = event.getMessage(); //message
    });
});
```
**Friends:**
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
     * Retrieve friend by ID (Friend#getId())
     */
    Friend friend = api.getFriendById(...);
});
```
**Send Message to Friend**
```JAVA
... //Login
api.onReady(() -> {
     Friend friend = ...;
     friend.sendMessage("Hello!");
});
```
**Get Profile Icon of any Friend**
```JAVA
... //Login
api.onReady(() -> {
    Friend friend = ...;
    ProfileIcon icon = friend.getLoLStatus().getProfileIcon(); //Retrieve Profile Icon
    icon.getImage(width, height); //Retrieve Image with the specified size
});
```
More examples: https://github.com/Wirlie/League-of-Legends-Chat-Library-XMPP/tree/master/examples
