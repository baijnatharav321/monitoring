package com.monitoring.tool.zuulapigateway.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.monitoring.tool.zuulapigateway.model.LoginUserDetails;
import com.monitoring.tool.zuulapigateway.model.User;
import com.monitoring.tool.zuulapigateway.repository.UserRepository;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = repository.findUserByUserid(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found::::" + username));
		return user.map(LoginUserDetails::new).get();

		// return new org.springframework.security.core.userdetails.User("foo", "foo",
		// new ArrayList());
	}

}
