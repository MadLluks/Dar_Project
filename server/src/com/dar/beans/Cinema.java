package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dar.metier.DBHandler;

public class Cinema extends AbstractBean{
	private Location location;
	private Integer cine_id = null;
	private String cine_name;
	private Connection conn;
	
	public Cinema(String name, float lat, float lon){
		this.location = new Location(lat, lon);
		conn = DBHandler.getInstance();
		this.load();
	}
	
	public Cinema(int cine_id, String name, float lat, float lon){
		this.location = new Location(lat, lon);
		conn = DBHandler.getInstance();
		this.cine_id = cine_id;
	}
	
	public Cinema(int cine_id){
		this.cine_id = cine_id;
		conn = DBHandler.getInstance();
		this.load();
	}
	
	public String toJson(){
		return "{name: "+", cine_id: "+cine_id+", location: "+this.location.toJson()+"}";
	}
	
	private void load() {
		if(this.cine_id != null){
			// here we want to load our location
			PreparedStatement prestmt;
			try {
				String query = "SELECT loc_id, name FROM cinema WHERE cine_id = ?";
				prestmt = this.conn.prepareStatement(query);
				prestmt.setInt(1, cine_id);
				ResultSet res = prestmt.executeQuery();
				res.next();
				int loc_id = res.getInt("loc_id");
				this.setName(res.getString("name"));
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

	@Override
	protected boolean insert(){
		boolean success = false;
		modified = false;
		location.save();
		String query = "INSERT INTO cinema(cine_name) VALUES(?)";
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, cine_name);
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
		location.save();
		String query = "UPDATE cinema SET cine_name = ? WHERE cine_id = ?";			
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setString(1, cine_name);
			prestmt.setInt(2, cine_id);
			prestmt.executeQuery();		
			prestmt.close();
			this.verified = true;
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public String getName() {
		return cine_name;
	}

	public void setName(String name) {
		this.cine_name = name;
	}
}
