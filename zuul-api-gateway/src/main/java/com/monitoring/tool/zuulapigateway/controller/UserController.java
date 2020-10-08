package com.monitoring.tool.zuulapigateway.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.monitoring.tool.zuulapigateway.exception.InvalidCredentialException;
import com.monitoring.tool.zuulapigateway.model.AuthorizationRequest;
import com.monitoring.tool.zuulapigateway.model.AuthorizationResponse;
import com.monitoring.tool.zuulapigateway.util.JwtUtil;

@RestController
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid AuthorizationRequest authorizationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authorizationRequest.getUsername(), authorizationRequest.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialException("Invalid Username or password ");
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authorizationRequest.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthorizationResponse(jwt));

	}

}
