package com.dar.metier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {
	
	private static Connection connection = null;
	private static String uri = "jdbc:sqlite:";
	private static String dbPath;
	
	private DBHandler(){}

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
}
