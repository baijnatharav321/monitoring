package com.mindtree.webMonitoringTool.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;


@Entity
@Data
public class CheckWeb {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int checkId;
	@NotNull(message = "Website Name Is Required")
	private String websiteName;
	@NotNull(message = "Website URL  Is Required")
	private String websiteUrl;
	@NotNull(message = "Frequency Type  Is Required")
	private String frequencytype;
	@Column(name = "frequencyinterval", nullable = false)
	@Range(min = 1, max = 60, message = "Frequency Range Should Be Between 1-60")
	private int frequencyInterval;

	private Status status;
	@ManyToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private User user;
	@OneToMany(mappedBy="check",cascade = CascadeType.ALL)
	private Set<Tracker> tracker;

}
