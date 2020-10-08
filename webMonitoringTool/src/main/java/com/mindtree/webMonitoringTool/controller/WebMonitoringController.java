package com.mindtree.webMonitoringTool.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.webMonitoringTool.entity.CheckWeb;
import com.mindtree.webMonitoringTool.service.MonitoringToolService;



@RestController
@RequestMapping("/webMonitoring")
public class WebMonitoringController {
	private Logger logger = LoggerFactory.getLogger(WebMonitoringController .class);
	
	@Autowired
	private MonitoringToolService monitoringToolService;

	@PostMapping("/createNewCheck/{userId}")
	public String createNewCheck(@Valid @RequestBody CheckWeb check,@PathVariable int userId) {
		System.out.println("user id"+userId);
		System.out.println("check"+check);
		String message= monitoringToolService.createNewCheck(check,userId);
		return message;
	}
	

	@GetMapping(value = "/getAllCheck/{userId}")
	public Set<CheckWeb> getAllChecks(@PathVariable int userId) {
		Set<CheckWeb> checkWebs = monitoringToolService.getAllCheck(userId);
		return checkWebs;
	}
}
