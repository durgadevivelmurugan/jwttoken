package com.jwt.service;

import java.util.List;

import com.jwt.entity.User;

public interface UserService {

	List<User> getByRole(String Role);

}
