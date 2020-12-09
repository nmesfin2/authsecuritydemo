package com.nat.authdemotwo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nat.authdemotwo.model.ERole;
import com.nat.authdemotwo.model.Role;
import com.nat.authdemotwo.model.User;
import com.nat.authdemotwo.playload.request.LoginRequest;
import com.nat.authdemotwo.playload.request.SignupRequest;
import com.nat.authdemotwo.playload.response.MessageResponse;
import com.nat.authdemotwo.repository.RoleRepository;
import com.nat.authdemotwo.repository.UserRepository;
import com.nat.authdemotwo.security.jwt.JwtResponse;
import com.nat.authdemotwo.security.jwt.JwtUtils;
import com.nat.authdemotwo.security.services.UserDetailsImpl;


@CrossOrigin("*")
@Controller
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AuthenticationManager authenticationmanager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetailsImpl.getAuthorities().stream().map(i->i.getAuthority()).collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,userDetailsImpl.getId(),userDetailsImpl.getEmail(),userDetailsImpl.getUsername(),roles));
		
	}
	@PostMapping("/signup")
	
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest ) {
		
		if(userRepository.existsByUsername(signUpRequest.getUsername())) {
			
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
		}
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
		}
		
		User user = new User(signUpRequest.getUsername(), 
				 signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		
		if(strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(()-> new RuntimeException("Error : Role is not available"));
			roles.add(userRole);
		}
		else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}

