public class LoginExample {
	
	public static void main(String[] arguments) {
		LoLXMPPAPI api = new LoLXMPPAPI(ChatRegion.NA, new RiotAPI("RGAPI-XXX-XXX-XXX-XXX-XXX"));
		
		String lolUsername = "Wirlie";
		String lolPassword = "xxxxxxxxx";
		
		if(api.login(lolUsername, lolPassword)) {
			//Wait the API for the "it's ready for use" status
			api.ready(() -> {
				...
			});
		} else {
			if(api.getLoginResult() == LoginResult.AUTH_FAILED) {
				//wrong username or password
				...
			} else {
				//internal API error
				...
			}
		}
	}

}
