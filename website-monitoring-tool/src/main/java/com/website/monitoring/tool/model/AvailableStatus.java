package com.website.monitoring.tool.model;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableStatus {

	private Availability avilability;
	private ZonedDateTime currentTime;

}
