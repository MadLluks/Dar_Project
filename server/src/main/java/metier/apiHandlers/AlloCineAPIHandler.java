package main.java.metier.apiHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AlloCineAPIHandler {
	private static final String PARTNER = "YW5kcm9pZC12M3M";
	private static final String ALLOCINE_URL_QUERY = "http://api.allocine.fr/rest/v3/";
	private static final String URL_CHARSET = "UTF-8";
	private static AlloCineAPIHandler handler = null;
	
	private AlloCineAPIHandler() {
		
	}
	
	public static AlloCineAPIHandler getInstance(){
		if(handler == null){
			handler = new AlloCineAPIHandler();
		}
		return handler;
	}
	
	public String doQuery(String query){
		URLConnection connection;
		String response = "";
		try {
			connection = new URL(ALLOCINE_URL_QUERY+query+"partner="+PARTNER+"&format=json").openConnection();
			connection.setRequestProperty("Accept-Charset", URL_CHARSET);
			InputStream res = connection.getInputStream();
			
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(res, URL_CHARSET))) {
		        for (String line; (line = reader.readLine()) != null;) {
		        	response+=line;
		        }
		    }
			response = "{\"success\": true, \"result\": "+response+"}";
		} catch (MalformedURLException e) {
			response = "{\"success\": false, \"error\": \"malformed_url\"}";
			e.printStackTrace();
		} catch (IOException e) {
			response = "{\"success\": false, \"error\": \"io_exception\"}";
			e.printStackTrace();
		}
		
		return response;
	}
}
