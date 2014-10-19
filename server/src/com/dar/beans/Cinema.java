package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dar.metier.DBHandler;

public class Cinema {
	private Location location;
	private Integer cine_id = null;
	private boolean modified;
	private Connection conn;
	
	public Cinema(float lat, float lon){
		this.location = new Location(lat, lon);
		conn = DBHandler.getInstance();
		this.load();
	}
	
	public Cinema(int cine_id){
		this.cine_id = cine_id;
		conn = DBHandler.getInstance();
		this.load();
	}
	
	private void load() {
		if(this.cine_id != null){
			// here we want to load our location
			PreparedStatement prestmt;
			try {
				String query = "SELECT loc_id FROM cinema WHERE cine_id = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setInt(1, cine_id);
				ResultSet res = prestmt.executeQuery();
				res.next();
				int loc_id = res.getInt("loc_id");
				this.location = new Location(loc_id);
				res.close();
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(this.location != null){
			// here we have our location, we can get the cine_id
			PreparedStatement prestmt;
			try {
				String query = "SELECT cine_id FROM cinema c, location l WHERE c.loc_id = l.loc_id ";
				query += "AND l.loc_id = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setInt(1, this.location.getLoc_id());
				ResultSet res = prestmt.executeQuery();
				res.next();
				int loc_id = res.getInt("cine_id");
				this.location = new Location(loc_id);
				res.close();
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public void setLocation(Location loc){
		this.location = loc;
	}
	
	public void save() {
		if(modified){
			modified = false;
			// TODO
		}
	}
}
