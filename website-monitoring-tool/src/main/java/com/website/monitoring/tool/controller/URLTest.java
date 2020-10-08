package com.website.monitoring.tool.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLTest {
	enum Availability {
		UP, DOWN;
	}

	private static Logger log = LoggerFactory.getLogger(URLTest.class);
	static boolean available = false;
	static String status = "Down";

	public static void main(String[] args) {

		// String url = "https://www.linkedin.com/";
		String url = "http://www.google.com";
		try {

			final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			System.out.println(Availability.DOWN.toString());

			connection.connect();
			if (connection.getResponseCode() == 200) {
				status = "UP";
			} else {
				status = "Down";
			}

			log.info("Service " + url + " available, yeah!" + " Status :::" + status);

			available = true;

			// String d1 = "2020-09-28 16:11:45";
			// ZonedDateTime d1 = ZonedDateTime.

		} catch (final MalformedURLException e) {

			throw new IllegalStateException("Bad URL: " + url, e);

		} catch (final IOException e) {

			log.info("Service " + url + " unavailable, oh no!", e);

			available = false;

		}
	}
}
