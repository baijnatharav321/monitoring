package com.website.monitoring.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Configuration
public class CustomScheduler {

	private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	public void cancelScheduledPolling(String checkId) {
		ScheduledFuture<?> scheduledFuture = scheduledTasks.remove(checkId);
		if (scheduledFuture != null) {
			scheduledFuture.cancel(false);
		}
	}

	public ScheduledFuture<?> getScheduledPoller(String checkId) {
		return scheduledTasks.get(checkId);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new CustomThreadPoolTaskScheduler();
	}

	class CustomThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {

		@Override
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
			ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

			ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
			System.out.println("in scheduleAtFixedRate()  method...");
			if (scheduledTasks.containsKey(Long.toString(((PollTask) runnable.getTarget()).getCheck().getId()))) {
				cancelScheduledPolling(Long.toString(((PollTask) runnable.getTarget()).getCheck().getId()));
			}
			// else {
			scheduledTasks.put(Long.toString(((PollTask) runnable.getTarget()).getCheck().getId()), future);
			// }

			return future;
		}

		@Override
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
			ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

			ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
			scheduledTasks.put(Long.toString(((PollTask) runnable.getTarget()).getCheck().getId()), future);

			return future;
		}
	}
}
