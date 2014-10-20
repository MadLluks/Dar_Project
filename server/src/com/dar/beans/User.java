package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dar.metier.DBHandler;

public class User extends AbstractBean{
	private String login;	
	private String password;
	private Connection conn;
	private ArrayList<Movie> seenMovies = null;
	
	public User(String login, String password){
		this.login = login;
		this.password = password;		
		this.modified = true;
		init();
	}
	
	public User(){
		this.login = "";
		this.password = "";
		init();
	}
	
	private void init(){
		seenMovies = new ArrayList<Movie>();
		conn = DBHandler.getInstance();
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.modified = true;
		this.password = password;
	}
	
	
	public boolean load(){
		return this.load(login, password);
	}

	public boolean load(String login, String password){
		PreparedStatement prestmt;
		try {
			prestmt = this.conn.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
			prestmt.setString(1, login);
			prestmt.setString(2, password);
			this.verified  = prestmt.executeQuery().next();
			this.login = login;
			this.password = password;
		} catch (SQLException e) {
			e.printStackTrace();
			this.verified = false;
		}
		return this.verified;
	}
	
	public ArrayList<Movie> getSeenMovies(){
		if(seenMovies.size() == 0){			
			PreparedStatement prestmt;
			try {
				String query = "SELECT m.movie_id id,m.title title,ms.cine_id cine_id FROM movie m, movie_seen ms, user u ";
				query += "WHERE m.movie_id = ms.movie_id AND ms.login = u.login ";
				query += "AND login = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, login);
				ResultSet res = prestmt.executeQuery();
				while(res.next()){
					String id = res.getString("movie_id");
					String title = res.getString("title");
					int cine_id = res.getInt("cine_id");
					seenMovies.add(new Movie(id, title, new Cinema(cine_id)));
				}
				res.close();
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seenMovies;
	}
	
	public ArrayList<Cinema> getCinemaVisited(){
		ArrayList<Cinema> result = new ArrayList<Cinema>();
		PreparedStatement prestmt;
		try {
			String query = "SELECT c.cine_name cine_name, c.cine_id cine_id, l.lat lat, l.lon lon FROM movie_seen ms, location l ";
			query += "WHERE ms.cine_id = c.cine_id AND l.loc_id = c.loc_id AND ms.login = ? ";
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, login);
			ResultSet res = prestmt.executeQuery();
			while(res.next()){
				String name = res.getString("cine_name");
				float lat = Float.valueOf(res.getString("lat"));
				float lon = Float.valueOf(res.getString("lon"));
				int cine_id = res.getInt("cine_id");
				result.add(new Cinema(cine_id, name, lat, lon));
			}
			res.close();
			prestmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean addSeenMovie(Movie m){
		this.seenMovies.add(m);
		return m.save();
	}	

	@Override
	protected boolean update() {
		boolean success = false;
		saveSeenMovies();
		String query = "UPDATE user SET password = ? WHERE login = ?";
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, login);
			prestmt.executeQuery();		
			prestmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	@Override
	protected boolean insert() {
		boolean res = true;
		saveSeenMovies();
		PreparedStatement prestmt;
		try{
			prestmt = this.conn.prepareStatement("insert into user values(?, ?)");
			prestmt.setString(1, login);
			prestmt.setString(2, password);
			prestmt.execute();
			verified = true;
		}
		catch(SQLException e){
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public void saveSeenMovies(){
		for(Movie m : this.seenMovies)
			m.save();
	}
	
}
