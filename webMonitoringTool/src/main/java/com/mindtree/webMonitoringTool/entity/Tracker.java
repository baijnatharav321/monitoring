package com.mindtree.webMonitoringTool.entity;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity

@Data

public class Tracker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long trackerId;

	private Availability availability;

	private ZonedDateTime time;

	@ManyToOne(cascade = CascadeType.ALL)
	private CheckWeb check;


}
