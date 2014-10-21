package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dar.metier.DBHandler;

public class Location extends AbstractBean{
	private Integer loc_id;
	private String address;
	private String postal_code;
	private String city;
	private Float lat,lon;
	private Connection conn;
	
	public Location(float lat, float lon) {
		conn = DBHandler.getInstance();
		this.lat = lat;
		this.lon = lon;
		// find loc_id for lat and lon args
		// create entry if none exists
		save();
	}
	
	
	public Location(int loc_id) {
		conn = DBHandler.getInstance();
		load();
	}
	
	public int getLoc_id() {
		return loc_id;
	}
	public void setLoc_id(int loc_id) {
		this.loc_id = loc_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}

	public String toJson() {
		return "{\"loc_id\": "+loc_id+", \"lon\": "+lon+", \"lat\":"+lat+"}";
	}
	
	@Override
	protected boolean exists(){
		if(!exists){
			PreparedStatement prestmt;
			try {
				prestmt = this.conn
						.prepareStatement("SELECT loc_id FROM location WHERE lat = ? AND lon = ?");
				prestmt.setFloat(1, lat);
				prestmt.setFloat(2, lon);
				ResultSet res = prestmt.executeQuery();
				if(res.next()){
					this.loc_id = res.getInt("loc_id");
					exists = true;
				}
				prestmt.close();
			}catch(SQLException e){}
		}
		return exists;
	}

	public boolean load(){
		PreparedStatement prestmt;
		try {
			if(this.loc_id != null){
				prestmt = this.conn
						.prepareStatement("SELECT lat,lon FROM location WHERE loc_id = ?");
				prestmt.setInt(1,loc_id);
				ResultSet res = prestmt.executeQuery();
				if(res.next()){
					this.lat = res.getFloat("lat");
					this.lon = res.getFloat("lon");
					this.exists = true;
				}
				res.close();
			}
			else{
				prestmt = this.conn
						.prepareStatement("SELECT loc_id FROM location WHERE lat = ? AND lon = ?");
				prestmt.setFloat(1, lat);
				prestmt.setFloat(2, lon);
				ResultSet res = prestmt.executeQuery();
				if(res.next()){
					this.loc_id = res.getInt("loc_id");
					this.exists = true;
				}
				res.close();
			}			
			prestmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.exists;
	}
	
	@Override
	protected boolean update() {
		boolean success = false;
		modified = false;
		String query = "UPDATE location SET lat = ?, lon = ? WHERE loc_id = ?";			
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query);
			prestmt.setFloat(1, lat);
			prestmt.setFloat(2, lon);
			prestmt.setInt(3, loc_id);
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
	protected boolean insert() {
		boolean success = false;
		modified = false;		
		String query = "INSERT INTO location(lat, lon) VALUES(?, ?)";
		PreparedStatement prestmt;
		try {				
			prestmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			System.out.println("lat,lon = "+lat+","+lon);
			prestmt.setFloat(1, lat);
			prestmt.setFloat(2, lon);
			prestmt.executeUpdate();
			ResultSet res = prestmt.getGeneratedKeys();
			this.loc_id = res.getInt(1);
			System.out.println("loc_id : "+this.loc_id);
			res.close();
			prestmt.close();
			this.exists = true;
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	

}
