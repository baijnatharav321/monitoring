package com.website.monitoring.tool.service;

import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.StatusResponse;

public interface PollService {

	void pollGivenCheck(Check check);

	StatusResponse getStatus(String websitename, String userId);
}
