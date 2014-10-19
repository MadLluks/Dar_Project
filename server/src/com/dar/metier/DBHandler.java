package com.dar.metier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dar.beans.User;

public class DBHandler {
	//private static final String dbPath = getServletContext().getRealPath("/") + "/WEB-INF/dar.db";
	private static Connection connection = null;
	private static String uri = "jdbc:sqlite:";
	private static String dbPath;

	/*
	 * Called by InitContextListener
	 */
	public static void setPath(String path){
		dbPath = path;
	}
	
	public static String getPath(){
		return dbPath;
	}
	
	public static Connection getInstance(){
		if(connection == null){
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection(uri+dbPath);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}		
		return connection;	
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getSeenMovies(User user){
		// TODO
		return "";
	}
	
	public String getInfosOnMovie(User user, int movie_id){
		// TODO
		return "";
	}
	
	public boolean addSeenMovie(User user, String title){
		// TODO
		// if no movie with title "title" exists in db.movie, create an entry
		// 
		// add entry (user.login, movie.id, cine.id) in db.movie_seen  
		return false;
	}
	
//	public boolean saveUserLocation(User user, Location loc){
//		// TODO
//		return false;
//	}
}
