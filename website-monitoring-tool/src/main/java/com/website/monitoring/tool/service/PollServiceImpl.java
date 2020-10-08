package com.website.monitoring.tool.service;

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.website.monitoring.tool.CustomScheduler;
import com.website.monitoring.tool.exception.InvalidUserNameException;
import com.website.monitoring.tool.exception.WebCheckNotFoundException;
import com.website.monitoring.tool.model.Availability;
import com.website.monitoring.tool.model.AvailableStatus;
import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.DownResponse;
import com.website.monitoring.tool.model.Poll;
import com.website.monitoring.tool.model.StatusResponse;
import com.website.monitoring.tool.model.UpResponse;
import com.website.monitoring.tool.model.User;
import com.website.monitoring.tool.repository.PollRepository;
import com.website.monitoring.tool.repository.UserRepository;
import com.website.monitoring.tool.repository.WebCheckRepository;
import com.website.monitoring.tool.utility.CheckUtil;

@Service
public class PollServiceImpl implements PollService {

	private static final Logger log = LoggerFactory.getLogger(PollService.class);

	private final int TIMEOUT = 5000;

	private Map<String, Integer> downCountMap = new HashMap<String, Integer>();
	private Map<String, Map<String, Integer>> checkMap = new HashMap<String, Map<String, Integer>>();

	private PollRepository pollRepository;
	private CustomScheduler customScheduler;
	private WebCheckRepository repo;
	private EmailService emailService;
	private UserRepository userRepository;

	public PollServiceImpl(PollRepository pollRepository, CustomScheduler customScheduler, WebCheckRepository repo,
			EmailService emailService, UserRepository userRepository) {
		this.pollRepository = pollRepository;
		this.customScheduler = customScheduler;
		this.repo = repo;
		this.emailService = emailService;
		this.userRepository = userRepository;
	}

	@Override
	public void pollGivenCheck(Check chk) {
		Availability availability = CheckUtil.pingURL(chk.getWebsiteurl(), TIMEOUT);
		String checkId = chk.getId().toString();

		if (availability == Availability.UP) {
			downCountMap = new HashMap<String, Integer>();
			checkMap.put(checkId, downCountMap);
		}

		if (!CollectionUtils.isEmpty(checkMap.get(checkId)) && checkMap.containsKey(checkId)) {
			Map<String, Integer> downCountMap = checkMap.get(checkId);
			int count = downCountMap.get(availability.toString());
			Map<String, Integer> newdownCountMap = new HashMap<String, Integer>();
			newdownCountMap.put(availability.toString(), count + 1);
			checkMap.replace(checkId, downCountMap, newdownCountMap);

		} else if (availability.equals(Availability.DOWN)) {
			downCountMap.put(availability.toString(), 1);
			checkMap.put(checkId, downCountMap);

		}

		ScheduledFuture<?> lock = customScheduler.getScheduledPoller(Long.toString(chk.getId()));
		if (lock == null)
			return;
		synchronized (lock) {

			if (customScheduler.getScheduledPoller(Long.toString(chk.getId())) == null)
				return;
			Poll poll = new Poll(availability, ZonedDateTime.now(Clock.systemUTC()));

			poll.setChk(chk);
			pollRepository.save(poll);
			if (!CollectionUtils.isEmpty(checkMap) && checkMap.containsKey(checkId)) {
				Map<String, Integer> downCountMap = checkMap.get(checkId);

				if (downCountMap.containsKey(Availability.DOWN.toString())
						&& downCountMap.get(Availability.DOWN.toString()) >= 3) {
					Optional<User> user = userRepository.findUserByUserid(chk.getUser().getUserid());
					user.orElseThrow(() -> new InvalidUserNameException("Invalid Username/password"));
					log.info("Sending Mail......Webcheck ID::::" + checkId);

					emailService.sendEmail(chk.getWebsitename(), chk.getWebsiteurl(), user.get().getUseremail(),
							downCountMap.get(Availability.DOWN.toString()));

				}
			}

		}
	}

	@Override
	public StatusResponse getStatus(String websitename, String userId) {
		Optional<Check> check = repo.findCheckByWebsiteName(userId, websitename, true);
		check.orElseThrow(() -> new WebCheckNotFoundException(
				"Web Check with name  " + websitename + " not found with userId " + userId));
		int checkId = new Long(check.get().getId()).intValue();
		long upTime = 0;
		long downTime = 0;
		AvailableStatus status = pollRepository.findStatus(checkId);
		List<Integer> availabilities = pollRepository.findAvailability(checkId);

		if (availabilities.contains(0)) {
			UpResponse upRes = pollRepository.findUpPollByTime(Availability.UP, checkId).orElse(null);
			if (upRes != null) {
				upTime = TimeUnit.SECONDS
						.toMinutes(Duration.between(upRes.getMinUpTime(), upRes.getMaxUptime()).getSeconds());
			}
		}

		if (availabilities.contains(1)) {
			DownResponse downRes = pollRepository.findDownPollByTime(Availability.DOWN, checkId).orElse(null);
			if (downRes != null) {
				downTime = TimeUnit.SECONDS
						.toMinutes(Duration.between(downRes.getMinDownTime(), downRes.getMaxDowntime()).getSeconds());
			}
		}
		long totalTime = upTime + downTime;

		log.info("upTime::::" + upTime + " Down Time ::::" + downTime + " TotalTime ::: " + totalTime);

		DecimalFormat df = new DecimalFormat("0.00");
		String upPercentage = "0.00%";
		String downPercentage = "0.00%";
		if (totalTime != 0) {
			upPercentage = df.format(upTime * 100 / (double) totalTime) + "%";
			downPercentage = df.format(downTime * 100 / (double) totalTime) + "%";

		}
		return new StatusResponse(status.getAvilability().toString(), upPercentage, downPercentage);
	}

}
