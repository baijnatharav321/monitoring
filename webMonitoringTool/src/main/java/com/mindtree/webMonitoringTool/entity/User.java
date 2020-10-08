package com.mindtree.webMonitoringTool.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity

public @Data @RequiredArgsConstructor class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@NotNull(message = "User Name Is Required")
	private String userName;
	@NotNull(message = "User Email Is Required")
	@Email
	private String userEmail;
	@NotNull(message = "Password Is Required")
	private String password;
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
	private Set<CheckWeb> check;

}
