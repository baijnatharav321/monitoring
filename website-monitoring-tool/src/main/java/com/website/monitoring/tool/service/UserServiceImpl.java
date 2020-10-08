package com.website.monitoring.tool.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.monitoring.tool.exception.UserNotFoundException;
import com.website.monitoring.tool.model.User;
import com.website.monitoring.tool.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findbyUserId(String userId) throws Exception {
		Optional<User> user = userRepository.findUserByUserid(userId);
		user.orElseThrow(() -> new UserNotFoundException("User with name " + userId + " not found"));
		return user.get();

	}
}
