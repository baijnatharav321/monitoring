package com.website.monitoring.tool.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

import com.website.monitoring.tool.PollTask;
import com.website.monitoring.tool.exception.WebCheckNotFoundException;
import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.repository.PollRepository;
import com.website.monitoring.tool.repository.WebCheckRepository;

@Service
public class JobServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private PollRepository pollRepo;
	@Autowired
	private TaskScheduler taskScheduler;
	@Autowired
	private PollService pollService;

	@Autowired
	private WebCheckRepository checkRepo;

	public void findCheckId() {
		log.info("in findCheckId method.....");
		List<Long> checkIds = pollRepo.findCheckId(true);
		log.info("checkIds size...." + checkIds.size());
		for (int i = 0; i < checkIds.size(); i++) {
			Optional<Check> check = checkRepo.findById(checkIds.get(i));
			Long checkId = check.get().getId();
			check.orElseThrow(() -> new WebCheckNotFoundException("Web Check with id " + checkId + " not found "));

			try {
				taskScheduler.scheduleAtFixedRate(
						new ScheduledMethodRunnable(new PollTask(check.get(), pollService), "startPolling"),
						TimeUnit.MINUTES.toMillis(check.get().getFrequencyinterval()));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

}
