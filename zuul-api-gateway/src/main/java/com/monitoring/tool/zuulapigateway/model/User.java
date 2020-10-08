package com.monitoring.tool.zuulapigateway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "loginuser")
public class User {

	@Id
	private String userid;
	@Column(name = "password")
	@NotNull(message = "Password should not be null....")
	@NotBlank(message = "Password should not be Blank....")
	private String password;
	@Column(name = "useremail")
	@NotNull(message = "User Email should not be null....")
	@NotBlank(message = "User Email should not be Blank....")
	@Email
	private String useremail;

}
