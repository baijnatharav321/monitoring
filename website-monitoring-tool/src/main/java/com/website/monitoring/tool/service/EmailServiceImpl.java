package com.website.monitoring.tool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	;
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendEmail(String websiteName, String websiteurl, String userEmail, int counter) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userEmail);
		mailMessage.setSubject("Website Down Time Tracker");
		mailMessage.setText("Your Website " + websiteName + " with URL " + websiteurl + " is down with " + counter
				+ " consecutive checks ");
		log.info("Your Website " + websiteName + " with URL " + websiteurl + " is down with " + counter
				+ " consecutive checks ");
		mailSender.send(mailMessage);
	}

}
