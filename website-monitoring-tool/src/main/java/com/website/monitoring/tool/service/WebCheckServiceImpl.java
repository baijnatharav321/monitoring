package com.website.monitoring.tool.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.website.monitoring.tool.CustomScheduler;
import com.website.monitoring.tool.PollTask;
import com.website.monitoring.tool.exception.InvalidUserNameException;
import com.website.monitoring.tool.exception.WebCheckNotFoundException;
import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.User;
import com.website.monitoring.tool.repository.PollRepository;
import com.website.monitoring.tool.repository.WebCheckRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WebCheckServiceImpl implements WebCheckService {

	private WebCheckRepository repo;
	private CustomScheduler customScheduler;
	private TaskScheduler taskScheduler;
	private PollRepository pollRepository;
	private PollService pollService;

	@Override
	public void saveCheck(Check check) throws NoSuchMethodException {
		try {
			repo.save(check);
			taskScheduler.scheduleAtFixedRate(
					new ScheduledMethodRunnable(new PollTask(check, pollService), "startPolling"),
					TimeUnit.MINUTES.toMillis(check.getFrequencyinterval()));
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getRootCause().getMessage());
		}

	}

	@Override
	public Check findCheckById(String userId, long id) {
		Optional<Check> check = repo.findByUserId(userId, id, true);
		check.orElseThrow(
				() -> new WebCheckNotFoundException("Web Check with id " + id + " not found with userId " + userId));
		return check.get();
	}

	@Override
	public Check findCheckByIdStatus(String userId, long id) {
		Optional<Check> check = repo.findByUserId(userId, id, false);
		check.orElseThrow(
				() -> new WebCheckNotFoundException("Web Check with id " + id + " not found with userId " + userId));
		return check.get();
	}

	@Override
	public List<Check> getCheckByFreqInterval(String userId, int frequencyinterval, boolean active) {
		List<Check> checks = Optional.ofNullable(repo.findByFrequencyinterval(userId, frequencyinterval, active))
				.orElse(Collections.emptyList());

		return checks;
	}

	@Override
	public List<Check> getCheckByNameWithAPI(String userId, String name, boolean active) {
		List<Check> checks = Optional.ofNullable(repo.findByWebsiteName(userId, name, active))
				.orElse(Collections.emptyList());
		return checks;
	}

	@Override
	@Transactional
	public void updateCheckStatusInActive(String userId, Check check) throws Exception {
		if (check.getUser().getUserid().equals(userId)) {
			if (check.isActive()) {
				check.setActive(false);

				synchronized (customScheduler.getScheduledPoller(check.getId().toString())) {
					customScheduler.cancelScheduledPolling(check.getId().toString());

					pollRepository.deleteAllByChk(check);
					repo.save(check);

				}
			} else {
				throw new InvalidUserNameException("Invalid Username/password");
			}
		}
	}

	@Override
	public void updateCheckStatusActive(String userId, Check check) throws Exception {
		if (check.getUser().getUserid().equals(userId)) {
			if (!check.isActive()) {
				check.setActive(true);
				repo.save(check);
				taskScheduler.scheduleAtFixedRate(
						new ScheduledMethodRunnable(new PollTask(check, pollService), "startPolling"),
						TimeUnit.MINUTES.toMillis(check.getFrequencyinterval()));

			}
		} else {
			throw new InvalidUserNameException("Invalid Username/password");
		}

	}

	@Override
	public Check findCheckByWebsiteurl(User user, Check check, boolean active) {
		Set<Check> checks = user.getChecks();
		Check ch = checks.stream().filter(c -> c.getWebsiteurl().trim().equals(check.getWebsiteurl().trim())).findAny()
				.orElse(null);

		return ch;

	}

}
