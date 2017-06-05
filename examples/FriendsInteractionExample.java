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
				LoLStatus lolstatus = f.getLoLStatus(); //get the friend status (GameStatus, SummonerLevel, ProfileIconID, etc)
				
				f.sendMessage("Hello!"); //send message
			}
		});
