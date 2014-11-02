package main.java.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.beans.AbstractBean;
import main.java.metier.DBHandler;

public class Movie extends AbstractBean{

	private String movie_id; // Allocine key looks like P12345 
	private String title;
	private Connection conn;

	public Movie(String movie_id, String title) {
		conn = DBHandler.getInstance();
		this.setTitle(title);
		this.setMovie_id(movie_id);
		exists();
	}

	public Movie(String movie_id) {
		conn = DBHandler.getInstance();
		this.setMovie_id(movie_id);
		exists();
		this.load();
	}

	public String toJson(){
		return "{\"title\": \""+this.getTitle()+"\", \"id\": \""+this.getMovie_id()+"\"}";
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
	}

	public boolean exists(){
		if(!exists){
			PreparedStatement prestmt = null;
			ResultSet res = null;
			exists = false;
			try {
				String query = "SELECT title FROM movie WHERE movie_id = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, movie_id);
				res = prestmt.executeQuery();
				if(res.next()){
					this.title = res.getString("title");
					exists = true;
				}			
			}catch(SQLException e){
				System.err.println(e.getMessage());
			} finally{
				try {
					if(res != null)
						res.close();
					if(res != null)
						prestmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
		}
		return exists;		
	}

	public boolean load(){
		PreparedStatement prestmt;
		try {
			prestmt = this.conn
					.prepareStatement("SELECT title FROM movie WHERE movie_id = ?");
			prestmt.setString(1, movie_id);
			ResultSet res = prestmt.executeQuery();
			if(res.next()){
				this.title = res.getString("title");
				this.exists = true;
			}
			res.close();
			prestmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
			this.exists = false;
		}
		return this.exists;
	}

	@Override
	protected boolean insert(){
		modified = false;
		String query = "INSERT INTO movie(movie_id,title) VALUES(?,?)";
		PreparedStatement prestmt;
		try {
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, movie_id);
			prestmt.setString(2, title);
			prestmt.executeUpdate();
			prestmt.close();
			this.exists = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return this.exists;
	}

	@Override
	protected boolean update(){
		boolean success = false;
		modified = false;
		String query = "UPDATE movie SET title= ? WHERE movie_id = ?";			
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, title);
			prestmt.setString(2, movie_id);
			prestmt.executeUpdate();		
			prestmt.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}
}
