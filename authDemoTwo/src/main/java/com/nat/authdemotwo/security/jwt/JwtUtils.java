package com.nat.authdemotwo.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import com.nat.authdemotwo.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JwtUtils {
	
	@Value("authdemo.app.jwtSecret")
	private String jwtSecret;
	
	@Value("authdemo.app.jwtExpirationMs")
	private int jwtExpirationMs;
	public String generateJwtToken(Authentication authentication) {
		// userName
		
		// authenticated username and password will be provided by 
		UserDetailsImpl userDetailsImpl =(UserDetailsImpl)authentication.getPrincipal();
		// we need to build token
		return Jwts.builder().setSubject(userDetailsImpl.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
		// we need current time to provide 
		// we need expiration time.
		// a log for generationg token
		// compress/compact the token
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}catch(SignatureException e){
			return false;
		}catch(MalformedJwtException e) {
			return false;
		}
	}
}
