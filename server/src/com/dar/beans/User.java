package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dar.metier.DBHandler;

public class User {
	private String login;	
	private String password;
	Connection conn;

	public User(String login, String password){
		this.login = login;
		this.password = password;
		conn = DBHandler.getInstance();
	}
	
	public User(){
		this.login = "";
		this.password = "";
		conn = DBHandler.getInstance();
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public boolean register(){
		boolean res = true;
		PreparedStatement prestmt;
		try{
			prestmt = this.conn.prepareStatement("insert into user values(?, ?)");
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
	
	public boolean load(){
		return this.load(login, password);
	}

	public boolean load(String login, String password){
		PreparedStatement prestmt;
		boolean res;
		try {
			prestmt = this.conn
					.prepareStatement("select * from user where login = ? and password = ?");
			prestmt.setString(1, login);
			prestmt.setString(2, password);
			res = prestmt.executeQuery().next();
			this.login = login;
			this.password = password;
		} catch (SQLException e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
}
