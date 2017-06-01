Pending documentation ...

**Important:** Library in alpha stage, this means that the API may not work properly, or as expected, please be patient.

#### Downloads:
Latest Build: https://ci.appveyor.com/project/Wirlie/league-of-legends-chat-library-xmpp/build/artifacts
#### Usage:
```java
LoLXMPPAPI api = new LoLXMPPAPI(ChatRegion.LAN);
if(api.login("USERNAME", "PASSWORD")) {
  //success
  api.onReady(e -> {
    //Add Friend Listener
    api.addFriendListener(new FriendListener() {
      @Override
      public void onMessage(FriendMessage msg) {
        //Incoming message ...
      };
      
      @Override
      public void onFriendStateChange(Friend friend) {
        //Check new state
      };
      
      //Coming soon ...
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
