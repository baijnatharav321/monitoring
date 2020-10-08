package com.monitoring.tool.zuulapigateway.model;

public class AuthorizationResponse {

	private String jwt;

	public AuthorizationResponse() {
		super();
	}

	public AuthorizationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

}
