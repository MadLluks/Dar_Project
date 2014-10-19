package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dar.metier.DBHandler;

public class User {
	private String login;	
	private String password;
	private Connection conn;
	private ArrayList<Movie> seenMovies = null;
	private boolean movieAdded = false;
	private boolean passwordModified = false;
	private boolean registered = false;
	
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
			registered = true;
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
					.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
			prestmt.setString(1, login);
			prestmt.setString(2, password);
			res = prestmt.executeQuery().next();
			this.login = login;
			this.password = password;
			this.registered = true;
		} catch (SQLException e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public ArrayList<Movie> getSeenMovies(){
		if(seenMovies == null || movieAdded){
			movieAdded = false;
			seenMovies = new ArrayList<Movie>();
			PreparedStatement prestmt;
			try {
				String query = "SELECT m.movie_id id,m.title title FROM movie m, movie_seen ms, user u ";
				query += "WHERE m.movie_id = ms.movie_id AND ms.login = u.login ";
				query += "AND login = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, login);
				ResultSet res = prestmt.executeQuery();
				while(res.next()){
					String id = res.getString("movie_id");
					String title = res.getString("title");
					seenMovies.add(new Movie(id, title));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seenMovies;
	}
	
	public boolean addSeenMovie(Movie m){
		this.movieAdded = true;
		this.seenMovies.add(m);
		return m.save();
	}
	
	public boolean save(){
		boolean success = false;
		if(!this.registered)
			success = this.register();
		if(movieAdded){
			for(Movie m : this.seenMovies){
				success &= m.save();
			}
		}
		if(passwordModified){
			String query = "UPDATE user SET password = ? WHERE login = ?";
			PreparedStatement prestmt;
			try {				
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, login);
				prestmt.executeQuery();				
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
		}		
		return success;
	}
	
}
