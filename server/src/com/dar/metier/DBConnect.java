package com.dar.metier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnect {
	private String dbPath = "";
	private Connection connection = null;  

	public DBConnect(String path){
		this.dbPath = path;		
	}
	
	public void connect(){
		try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (ClassNotFoundException notFoundException) {
            notFoundException.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
	}
	
	public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public boolean registerUser(String login, String password){
		boolean res = true;
		PreparedStatement prestmt;
		try{
			prestmt = this.connection
				.prepareStatement("insert into user values(?, ?)");
			prestmt.setString(1, login);
			prestmt.setString(2, password);
			prestmt.execute();
		}
		catch(SQLException e){
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public boolean isUserRegistered(String login, String password){
		PreparedStatement prestmt;
		boolean res;
		try {
			prestmt = this.connection
					.prepareStatement("select * from user where login = ?");
			prestmt.setString(1, login);
			res = prestmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	
}
