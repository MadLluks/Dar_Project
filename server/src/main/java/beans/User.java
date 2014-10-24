package main.java.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.beans.AbstractBean;
import main.java.beans.Cinema;
import main.java.beans.Movie;
import main.java.metier.DBHandler;

public class User extends AbstractBean{
	private String login;	
	private String password;
	private Connection conn;
	private ArrayList<SeenMovie> seenMovies = null;
	private ArrayList<SeenMovie> newlyAddedMovies = null;	
	
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
		seenMovies = new ArrayList<SeenMovie>();
		newlyAddedMovies = new ArrayList<SeenMovie>();
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
			ResultSet res = prestmt.executeQuery();
			if(res.next()){
				this.exists  = true;
				this.login = login;
				this.password = password;
			}
			res.close();
			prestmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
			this.exists = false;
		}
		return this.exists;
	}
	
	public ArrayList<Movie> getSeenMovies(){
		if(seenMovies.size() == 0){			
			PreparedStatement prestmt;
			try {
				String query = "SELECT m.movie_id id,m.title title,ms.cine_id cine_id FROM movie m, seen_movie ms, user u ";
				query += "WHERE m.movie_id = ms.movie_id AND ms.login = u.login ";
				query += "AND login = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, login);
				ResultSet res = prestmt.executeQuery();
				while(res.next()){
					String id = res.getString("movie_id");
					String title = res.getString("title");
					int cine_id = res.getInt("cine_id");
					seenMovies.add(new SeenMovie(new Movie(id, title), new Cinema(cine_id), this));
				}
				res.close();
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Movie> result = new ArrayList<Movie>();
		for(SeenMovie sm : seenMovies)
			result.add(sm.getMovie());
		return result;
	}
	
	public ArrayList<Cinema> getCinemaVisited(){
		ArrayList<Cinema> result = new ArrayList<Cinema>();
		PreparedStatement prestmt=null;
		ResultSet res = null;
		try {
			String query = "SELECT c.cine_name cine_name, c.cine_id cine_id, l.lat lat, l.lon lon FROM movie_seen ms, location l ";
			query += "WHERE ms.cine_id = c.cine_id AND l.loc_id = c.loc_id AND ms.login = ? ";
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, login);
			res = prestmt.executeQuery();
			while(res.next()){
				String name = res.getString("cine_name");
				float lat = Float.valueOf(res.getString("lat"));
				float lon = Float.valueOf(res.getString("lon"));
				int cine_id = res.getInt("cine_id");
				result.add(new Cinema(cine_id, name, lat, lon));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				res.close();
				prestmt.close();					
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		return result;
	}
	
	public boolean addSeenMovie(Movie m, Cinema c){
		
		boolean res = c.save();
		res &= m.save();
		SeenMovie sm = new SeenMovie(m,c,this);
		newlyAddedMovies.add(sm);
		this.seenMovies.add(sm);
		return res;
	}	

	@Override
	protected boolean exists() {
		if(!exists){
			PreparedStatement prestmt;
			try {
				prestmt = this.conn
						.prepareStatement("SELECT login FROM user WHERE login = ?");
				prestmt.setString(1, login);
				ResultSet res = prestmt.executeQuery();
				//this.movie_id = movie_id;
				if(res.next())
					exists = true;
				res.close();
				prestmt.close();
			}catch(SQLException e){}
		}
		return exists;
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
			prestmt.executeUpdate();		
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
			prestmt.executeUpdate();
			this.exists = true;
			prestmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			res = false;
		}
		return res;
	}
	
	public void saveSeenMovies(){
		for(SeenMovie sm : this.newlyAddedMovies){
			sm.save();			
		}
		this.newlyAddedMovies.clear();
	}
		
	protected class SeenMovie extends AbstractBean{
		private Cinema cinema;
		private Movie movie;
		private User user;
		private Connection conn;
		
		public SeenMovie(Movie movie, Cinema cinema, User u) {
			this.movie = movie;
			this.setCinema(cinema);			
			this.conn = DBHandler.getInstance();
			this.user = u;
			this.save();
		}

		public Movie getMovie() {
			return movie;
		}
		
		public User getUser(){
			return user;
		}
		
		@Override
		protected boolean update() {
			boolean success = this.cinema.save();
			success = this.movie.save() && success;		
			modified = false;
			return success;
		}

		@Override
		protected boolean insert() {
			boolean success = false;
			modified = false;
			this.cinema.save();
			this.movie.save();
			String query = "INSERT INTO seen_movie (movie_id, cine_id, login) VALUES(?,?,?)";
			PreparedStatement prestmt;
			try {				
				prestmt = this.conn.prepareStatement(query);
				prestmt.setString(1, movie.getMovie_id());
				prestmt.setInt(2, cinema.getCine_Id());
				prestmt.setString(3, this.user.getLogin());
				prestmt.executeUpdate();		
				prestmt.close();
				this.exists = true;
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
			return success;
		}

		@Override
		protected boolean exists() {
			if(!exists){
				PreparedStatement prestmt;
				try {
					prestmt = this.conn
							.prepareStatement("SELECT * FROM seen_movie "
									+"WHERE cine_id = ? AND movie_id = ? AND login = ?");
					prestmt.setFloat(1, this.getCinema().getCine_Id());
					prestmt.setString(2, this.getMovie().getMovie_id());
					prestmt.setString(3, this.getUser().getLogin());					
					ResultSet res = prestmt.executeQuery();
					if(res.next())
						exists = true;
					res.close();
					prestmt.close();
				}catch(SQLException e){}
			}
			return exists;
		}

		public Cinema getCinema() {
			return cinema;
		}

		public void setCinema(Cinema cinema) {
			this.cinema = cinema;
		}		
	}
}
