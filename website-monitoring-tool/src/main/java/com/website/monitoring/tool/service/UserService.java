package com.website.monitoring.tool.service;

import com.website.monitoring.tool.model.User;

public interface UserService {

	public User findbyUserId(String userId) throws Exception;

}
