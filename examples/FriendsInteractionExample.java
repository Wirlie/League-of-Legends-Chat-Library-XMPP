		...
  
		// Assuming that you already have an "api" variable and you already logged in (see Login Example)
  
		api.onReady(() -> {
			for(Friend f : api.getAllFriends()) {
				//Loop all friends
			}
			
			for(Friend f : api.getOfflineFriends()) {
				//Loop all offline friends
			}
			
			for(Friend f : api.getOnlineFriends()) {
				//Loop all online friends
			}
			
			for(Friend f : api.getFriends(ChatStatus.CHAT)) {
				//Loop all friends with a ChatStatus "CHAT"
			}
			
			for(Friend f : api.getFriends(GameStatus.IN_GAME)) {
				//Loop all friends with a GameStatus "IN GAME"
			}
			
			//Get Friend by name
			Friend f = api.getFriendByName("Wirlie");
			
			if(f != null) {
				f.getName(); //get name
				f.getGameStatus(); //get game status
				f.getChatStatus(); //get chat status
				f.getId(); //get summoner id
				f.getStatusMessage(); //get status message
				f.getSummonerLevel(); //get summoner level
				f.getProfileIcon().getImage(50, 50); //get profile icon image (50px x 50px)
				
				f.sendMessage("Hello!"); //send message
			}
		});
