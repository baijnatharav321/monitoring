package com.mindtree.webMonitoringTool.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindtree.webMonitoringTool.entity.CheckWeb;
import com.mindtree.webMonitoringTool.entity.User;
import com.mindtree.webMonitoringTool.repository.CheckRepository;
import com.mindtree.webMonitoringTool.repository.UserRepository;

@Service
public class MonitoringToolService {
	@Autowired
	private CheckRepository checkRepository;
	
	@Autowired
	private UserRepository userRepository;

	public String createNewCheck(CheckWeb check, int userId) {
		User user = userRepository.getOne(userId);
		System.out.println("user"+user);
		check.setUser(user);
		checkRepository.save(check);
		return "Check Saved Successfully";
	}

	public Set<CheckWeb> getAllCheck(int userId) {
		User user = userRepository.getOne(userId);
		Set<CheckWeb> checks = new HashSet<CheckWeb>();
		checks= user.getCheck();
		return checks;
	}

}
