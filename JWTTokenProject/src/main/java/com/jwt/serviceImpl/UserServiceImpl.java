package com.jwt.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.entity.User;
import com.jwt.repository.UserRepository;
import com.jwt.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;
	@Override
	public List<User> getByRole(String Role) {
		
		List<User>list=repository.findByRole(Role);
		List<User>emp=new ArrayList<User>();
		for(User n:list) {
			emp.add(n);
		}
		return emp;
//		User user=opt.get();
//		return user;
	}

}






