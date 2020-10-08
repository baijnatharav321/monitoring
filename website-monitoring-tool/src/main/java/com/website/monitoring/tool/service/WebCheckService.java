package com.website.monitoring.tool.service;

import java.util.List;
import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.User;

public interface WebCheckService {

	public void saveCheck(Check check) throws NoSuchMethodException;

	public Check findCheckById(String userId, long id);

	public List<Check> getCheckByFreqInterval(String userId, int frequencyinterval, boolean active);

	public List<Check> getCheckByNameWithAPI(String userId, String name, boolean active);

	public void updateCheckStatusInActive(String userId, Check check) throws Exception;

	public void updateCheckStatusActive(String userId, Check check) throws Exception;

	public Check findCheckByWebsiteurl(User user, Check check, boolean active);

	public Check findCheckByIdStatus(String userId, long id);
}
