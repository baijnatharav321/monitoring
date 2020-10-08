package com.website.monitoring.tool.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckEmailStatus {

	private int checkId;
	private Map<Availability, Integer> downtimeCountMap;

}
