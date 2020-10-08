package com.website.monitoring.tool;

import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.service.PollService;

public class PollTask {

	private final Check check;
	private final PollService pollService;

	public PollTask(Check check, PollService pollService) {
		this.check = check;
		this.pollService = pollService;
	}

	public Check getCheck() {
		return check;
	}

	public void startPolling() {
		pollService.pollGivenCheck(check);
	}
}
