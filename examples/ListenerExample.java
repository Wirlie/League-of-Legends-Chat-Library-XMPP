	
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
			
			LoLStatus oldStatus = event.getOldStatus(); //get the old friend status before event
			LoLStatus newStatus = f.getLoLStatus(); //get the current friend status
		});
    
		api.addMessageListener(event -> {
 			//Message Event
			Friend f = event.getFriend();
			String message = event.getMessage();
			
			System.out.println("Message from " + f.getName() + " !! [" + message + "]");
		});
	});
