package com.jwt.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.util.ReactiveWrappers.ReactiveLibrary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.entity.AuthenticationResponse;
import com.jwt.entity.User;
import com.jwt.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;
	
	//to register
	public AuthenticationResponse register(User register) {
		User user=new User();
		user.setId(register.getId());
		user.setUsername(register.getUsername());
		user.setPassword(encoder.encode(register.getPassword()));
		user.setSalary(register.getSalary());
		user.setRole(register.getRole());
		repository.save(user);
		
		String token=jwtService.generateToken(user);
		return new AuthenticationResponse(token); 
	}
	
	//to login
	public AuthenticationResponse authenticate(User user) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		repository.findByUsername(user.getUsername()).orElseThrow();
		String token =jwtService.generateToken(user);
		return new AuthenticationResponse(token);
	} 
}
