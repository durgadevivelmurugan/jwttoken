package com.jwt.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	
	@Autowired
	UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return repository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("user not found"));
	}

}
