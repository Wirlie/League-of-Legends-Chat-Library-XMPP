Pending documentation ...

**Important:** Library in alpha stage, this means that the API may not work properly, or as expected, please be patient.

#### Downloads:
Latest Build: https://ci.appveyor.com/project/Wirlie/league-of-legends-chat-library-xmpp/build/artifacts
#### Usage:
```java
LoLXMPPAPI api = new LoLXMPPAPI(ChatRegion.LAN);
if(api.login("USERNAME", "PASSWORD")) {
  //success
  api.onReady(() -> {
    api.addFriendJoinListener(friend -> {
      //Friend Login Event
    });
    
    api.addFriendLeaveListener(friend -> {
      //Friend Logout Event
    });
    
    api.addFriendStatusChangeListener(event -> {
      //Friend Status Change Event
    });
    
    api.addMessageListener(event -> {
      //Incoming Message Event
    });
  });
} else {
  //auth failed
  
  //OPTIONAL: Check login result
  if(api.getLoginResult() == LoginResult.AUTH_FAILED) {
    //Please check your username or password ...
  } else {
    //Something's wrong
  }
}
```
