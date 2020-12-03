package com.nat.authdemotwo.security.jwt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String email;
	public JwtResponse(String token,long id, String email, String username, List<String> roles) {
		super();
		this.token = token;
		this.email = email;
		this.id = id;
		this.username = username;
		this.roles = roles;
	}
	private String token;
	private String type = "Bearer";
	private long id;
	private String username;
	private List<String> roles;
}
