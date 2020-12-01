package com.nat.authdemotwo.security.jwt;

import org.springframework.security.core.Authentication;

public class JwtUtils {
	public String generateJwtToken(Authentication authentication) {
		// userName
		
		// authenticated username and password will be provided by 
		authentication.getPrincipal();
		// we need to build token
		// we need current time to provide 
		// we need expiration time.
		// a log for generationg token
		// compress/compact the token
	}
	
	public String getUserNameFromJwtToken(String token) {
		
	}
	
	public boolean validateJwtToken(String authToken) {
		
	}
}
