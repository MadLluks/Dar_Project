package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dar.metier.DBHandler;

public class Movie {
	
	private String movie_id; // Allocine key looks like P12345 
	private String title;
	private Cinema cinema;
	private Connection conn;
	private boolean modified;
	private boolean loaded=false;
	
		
	public Movie(String movie_id, String title) {
		conn = DBHandler.getInstance();
		this.setMovie_id(movie_id);
		this.setTitle(title);
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

	public void setCine(float cine_lat, float cine_lon) {
		this.cinema = new Cinema(cine_lat, cine_lon);
		this.cinema.save();
		this.modified = true;
	}
	
	public boolean load(String movie_id, String title){
		PreparedStatement prestmt;
		try {
			prestmt = this.conn
					.prepareStatement("SELECT * FROM movie WHERE movie_id = ?");
			prestmt.setString(1, movie_id);			
			this.loaded = prestmt.executeQuery().next();
			this.movie_id = movie_id;
			this.title = title;
		} catch (SQLException e) {
			e.printStackTrace();
			this.loaded = false;
		}
		return this.loaded;
	}

	public boolean save() {
		boolean success = false;
		if(modified){
			modified = false;
			cinema.save();
			// TODO
		}
		return success;
	}

}
