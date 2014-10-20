package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dar.metier.DBHandler;

public class Movie extends AbstractBean{
	
	private String movie_id; // Allocine key looks like P12345 
	private String title;
	private Cinema cinema;
	private Connection conn;
		
	public Movie(String movie_id, String title, Cinema cinema) {
		conn = DBHandler.getInstance();
		this.setMovie_id(movie_id);
		this.setTitle(title);
		this.cinema = cinema;
	}

	public String toJson(){
		return "{title: "+this.getTitle()+", id: "+this.getMovie_id()+"}";
	}
				
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.modified = true;
	}

	public String getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(String movie_id) {
		this.movie_id = movie_id;
		this.modified = true;
	}
	
	public boolean load(String movie_id){
		PreparedStatement prestmt;
		try {
			prestmt = this.conn
					.prepareStatement("SELECT * FROM movie WHERE movie_id = ?");
			prestmt.setString(1, movie_id);			
			ResultSet res = prestmt.executeQuery();
			this.verified= res.next();
			this.movie_id = movie_id;
			this.title = res.getString("movie_title");
			prestmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			this.verified = false;
		}
		return this.verified;
	}
	
	@Override
	protected boolean insert(){
		boolean success = false;
		modified = false;
		cinema.save();
		String query = "INSERT INTO movie(title) VALUES(?)";
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, title);
			prestmt.executeQuery();		
			prestmt.close();
			this.verified = true;
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	@Override
	protected boolean update(){
		boolean success = false;
		modified = false;
		cinema.save();
		String query = "UPDATE movie SET title= ? WHERE movie_id = ?";			
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, title);
			prestmt.setString(2, movie_id);
			prestmt.executeQuery();		
			prestmt.close();
			this.verified = true;
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

}
