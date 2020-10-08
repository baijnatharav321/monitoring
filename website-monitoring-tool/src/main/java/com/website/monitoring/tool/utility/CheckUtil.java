package com.website.monitoring.tool.utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.website.monitoring.tool.exception.URINotCorrectException;
import com.website.monitoring.tool.model.Availability;

public class CheckUtil {
	public static boolean isURL(String url) {
		try {
			(new java.net.URL(url)).toURI();// openStream().close();
			return true;
		} catch (Exception ex) {
			throw new URINotCorrectException("URL is not correct::" + url);
		}
	}

	public static Availability pingURL(String url, int timeout) {
		url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (200 <= responseCode && responseCode <= 399)
				return Availability.UP;
		} catch (IOException exception) {
			return Availability.DOWN;
		}

		return Availability.DOWN;
	}
}
