package com.jwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.entity.AuthenticationResponse;
import com.jwt.entity.User;
import com.jwt.repository.UserRepository;
import com.jwt.service.UserService;
import com.jwt.serviceImpl.AuthenticationService;

@RestController
public class UserController {

	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserService service;
	
	@PostMapping("/register")
	//ResponseEntity is a class and used to represents an Http response including header,status code and body.
	public ResponseEntity<AuthenticationResponse> register(@RequestBody User user){
		return ResponseEntity.ok(authenticationService.register(user));
	} 
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse>login(@RequestBody User user){
		return ResponseEntity.ok(authenticationService.authenticate(user));
	}
	@GetMapping("/get")
	public ResponseEntity<List<User>> getall(){
		return ResponseEntity.ok(repository.findAll());
	}
	@GetMapping("/admin")
	public ResponseEntity<String> getadmin(){
		return ResponseEntity.ok("Hello,Admin");
	}
	@GetMapping("/user")
	public ResponseEntity<String> getUser(){
		return ResponseEntity.ok("Hello,User");
	}
	@GetMapping("/onlyuser")
	public ResponseEntity<String>getUserData(){
		return ResponseEntity.ok("Hi,this is User");
	}

	@GetMapping("/role/{role}")
	public List<User>getrole(@PathVariable String Role){
	return service.getByRole(Role);
}
}
