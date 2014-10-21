package com.dar.metier.apiHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GmapsAPIHandler {
	private static final String GMAPS_API_KEY = "AIzaSyAMDGS_wjPjHSGSKPJfM2cIwkWAfoUMXds";
	private static final String GMAPS_URL_QUERY = "https://maps.googleapis.com/maps/api/directions/json?";
	private static final String URL_CHARSET = "UTF-8";
	
	public String doQuery(String origin, String destination, String mode) throws MalformedURLException, IOException{
		String query = String.format("mode=%s&key=%s&origin=%s&destination=%s", 
		URLEncoder.encode(mode, URL_CHARSET),
		URLEncoder.encode(GMAPS_API_KEY, URL_CHARSET),
		URLEncoder.encode(origin, URL_CHARSET),
		URLEncoder.encode(destination, URL_CHARSET));
		return executeQuery(query);	
	}
	
	/*
	 * TODO iterate over all modes (driving, walking, etc.) 
	 * return the fastest complete direction
	 */
	public String findFastestDirection(String origin, String destination){
		String response = "";
		return response;
	}
	
	private String executeQuery(String query) throws MalformedURLException, IOException{
		URLConnection connection = new URL(GMAPS_URL_QUERY+query).openConnection();
		connection.setRequestProperty("Accept-Charset", URL_CHARSET);
		InputStream res = connection.getInputStream();
		String response = "";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(res, URL_CHARSET))) {
	        for (String line; (line = reader.readLine()) != null;) {
	        	response+=line;
	        }
	    }
		return response;
	}
	
}
