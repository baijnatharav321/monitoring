package com.website.monitoring.tool.controller;

import java.net.URI;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.StatusResponse;
import com.website.monitoring.tool.model.User;
import com.website.monitoring.tool.service.PollService;
import com.website.monitoring.tool.service.UserService;
import com.website.monitoring.tool.service.WebCheckService;
import com.website.monitoring.tool.utility.CheckUtil;

@RestController
public class WebCheckController {

	private Logger log = LoggerFactory.getLogger(WebCheckController.class);

	@Autowired
	public WebCheckService webCheckService;

	@Autowired
	public UserService userService;

	@Autowired
	public PollService pollService;

	@PostMapping(value = "/addCheck", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addCheck(HttpServletRequest request, @Valid @RequestBody Check check)
			throws Exception {
		String userId = request.getHeader("x-user-id");
		
		log.info("UserId:::" + userId);
		User user = userService.findbyUserId(userId);
		System.out.println("user id"+userId);
		log.info("List of all checks ::::" + user.getChecks().size());
		String url = check.getWebsiteurl().trim();
		String webname = check.getWebsitename().trim();
		CheckUtil.isURL(url);
		Check ch = webCheckService.findCheckByWebsiteurl(user, check, true);
		if (ch != null) {
			check.setId(ch.getId());
		}
		check.setUser(user);
		check.setActive(true);
		check.setWebsiteurl(url);
		check.setWebsitename(webname);
		webCheckService.saveCheck(check);

		log.info("Webb check with Id " + check.getId() + " saved successfuuly...");
		UriComponents uriComponent = UriComponentsBuilder.fromUriString("/getCheckById").path("/{id}")
				.buildAndExpand(113);
		URI location = uriComponent.toUri();

		return ResponseEntity.created(location).build();

		// return ResponseEntity.ok("Website url " + check.getWebsiteurl() + " already
		// exists.....");

	}

	@GetMapping(value = "/getAllCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Check>> getAllChecks(HttpServletRequest request) throws Exception {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		User user = userService.findbyUserId(userId);
		return ResponseEntity.ok(user.getChecks());
	}

	@GetMapping(value = "/getCheckById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Check> getCheckById(HttpServletRequest request, @PathVariable long id) {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		Check check = webCheckService.findCheckById(userId, id);
		return ResponseEntity.ok(check);
	}

	@GetMapping(value = "/getCheckByFreqInterval/{interval}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Check>> getCheckByFreqInterval(HttpServletRequest request, @PathVariable int interval) {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		List<Check> check = webCheckService.getCheckByFreqInterval(userId, interval, true);
		return ResponseEntity.ok(check);
	}

	@GetMapping(value = "/getCheckByNameWithAPI/{name}")
	public ResponseEntity<List<Check>> getCheckByNameWithAPI(HttpServletRequest request, @PathVariable String name) {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		List<Check> check = webCheckService.getCheckByNameWithAPI(userId, name, true);
		return ResponseEntity.ok(check);
	}

	@PutMapping(value = "/deactiveCheck/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Check> updateCheckStatusInActive(HttpServletRequest request, @PathVariable int id)
			throws Exception {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		Check check = webCheckService.findCheckById(userId, id);
		webCheckService.updateCheckStatusInActive(userId, check);
		return ResponseEntity.ok(check);

	}

	@PutMapping(value = "/activateCheck/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Check> updateCheckStatusActive(HttpServletRequest request, @PathVariable int id)
			throws Exception {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		Check check = webCheckService.findCheckByIdStatus(userId, id);
		webCheckService.updateCheckStatusActive(userId, check);
		return ResponseEntity.ok(check);

	}

	@GetMapping("/getCheckStatus/{websitename}")
	public ResponseEntity<StatusResponse> getStatus(HttpServletRequest request,
			@PathVariable("websitename") String websitename) {
		String userId = request.getHeader("x-user-id");
		log.info("UserId:::" + userId);
		return ResponseEntity.ok(pollService.getStatus(websitename, userId));
	}

}
