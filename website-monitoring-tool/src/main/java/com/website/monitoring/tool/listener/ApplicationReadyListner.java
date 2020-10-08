package com.website.monitoring.tool.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.website.monitoring.tool.service.JobServiceImpl;

@Component
public class ApplicationReadyListner implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger log = LoggerFactory.getLogger(ApplicationReadyListner.class);

	@Autowired
	private JobServiceImpl serviceImpl;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("in onApplicationEvent() method....");
		serviceImpl.findCheckId();
	}
}