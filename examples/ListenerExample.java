	
	...
  
	// Assuming that you already have an "api" variable and you already logged in (see Login Example)

	api.onReady(() -> {
		api.addFriendJoinListener(friend -> {
			//Friend Login Event
			System.out.println("Friend Join (" + friend.getName() + ")");
		});
		
		api.addFriendLeaveListener(friend -> {
			//Friend Logout Event
			System.out.println("Friend Leave (" + friend.getName() + ")");
		});
		
		api.addFriendStatusChangeListener(event -> {
			//Friend Status Change Event
			Friend f = event.getFriend();
			
			System.out.println("Friend Status Changed! (" + f.getName() + ")");
			
			if(event.gameStatusChanged()) { //Optional check
				System.out.println("GameStatus has changed! New GameStatus: " + f.getGameStatus());
			}
			
			if(event.statusMessageChanged()) { //Optional check
				System.out.println("StatusMessage has changed! New StatusMessage: " + f.getStatusMessage());
			}
			
			if(event.chatStatusChanged()) { //Optional check
				System.out.println("ChatStatus has changed! New ChatStatus: " + f.getChatStatus());
			}
			
			if(event.profileIconChanged()) { //Optional check
				System.out.println("ProfileIcon has changed! New ProfileIcon ID: " + f.getProfileIcon().getId());
			}
		});
    
		api.addMessageListener(event -> {
 			//Message Event
			Friend f = event.getFriend();
			String message = event.getMessage();
			
			System.out.println("Message from " + f.getName() + " !! [" + message + "]");
		});
	});
