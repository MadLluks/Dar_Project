package com.dar.metier.apiHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class GmapsAPIHandler {
	private static final String GMAPS_API_KEY = "AIzaSyAMDGS_wjPjHSGSKPJfM2cIwkWAfoUMXds";
	private static final String GMAPS_URL_QUERY = "https://maps.googleapis.com/maps/api/directions/json?";
	private static final String URL_CHARSET = "UTF-8";
	private static GmapsAPIHandler gmaps = null;
	private GmapsAPIHandler(){}
	
	public static GmapsAPIHandler getInstance(){
		if(gmaps == null){
			gmaps = new GmapsAPIHandler();
		}
		return gmaps;
	}
	
	public String doQuery(String origin, String destination, String mode) throws MalformedURLException, IOException{
		String query = String.format("mode=%s&key=%s&origin=%s&destination=%s", 
		URLEncoder.encode(mode, URL_CHARSET),
		URLEncoder.encode(GMAPS_API_KEY, URL_CHARSET),
		URLEncoder.encode(origin, URL_CHARSET),
		URLEncoder.encode(destination, URL_CHARSET));
		return executeQuery(query);	
	}
	
	/*
	 * iterate over all modes (driving, walking, etc.) 
	 * return the fastest complete direction
	 */
	public String findFastestDirection(String origin, String destination){
		String response = "";
		try {
			String query_driving = doQuery(origin, destination, "driving");
			String query_walking = doQuery(origin, destination, "walking");
			String query_bicycling= doQuery(origin, destination, "bicycling");
			JSONObject json = new JSONObject(query_driving);
			int d_driving = Integer.MAX_VALUE;
			if(json.get("status") == "OK")
				d_driving = json.getJSONArray("routes").getJSONObject(0)
								.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getInt("value");			
			json = new JSONObject(query_walking);
			int d_walking = Integer.MAX_VALUE;
			if(json.get("status") == "OK")
				d_walking = json.getJSONArray("routes").getJSONObject(0)
					.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getInt("value");
			
			json = new JSONObject(query_bicycling);
			int d_bicycling = Integer.MAX_VALUE;
			if(json.get("status") == "OK")
				d_bicycling = json.getJSONArray("routes").getJSONObject(0)
					.getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getInt("value");
			
			if(d_driving < d_walking && d_driving < d_bicycling)
				response = query_driving;
			if(d_walking <= d_driving && d_walking < d_bicycling)
				response = query_walking;
			if(d_bicycling <= d_walking && d_bicycling <= d_driving)
				response = query_bicycling;
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
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
