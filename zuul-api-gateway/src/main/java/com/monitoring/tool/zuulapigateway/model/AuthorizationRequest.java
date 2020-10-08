package com.monitoring.tool.zuulapigateway.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AuthorizationRequest {

	@NotNull(message = "Username should not be null....")
	@NotBlank(message = "Username should not be Blank....")
	private String username;
	@NotNull(message = "Password should not be null....")
	@NotBlank(message = "Password should not be Blank....")
	private String password;

	public AuthorizationRequest() {
		super();
	}

	public AuthorizationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
