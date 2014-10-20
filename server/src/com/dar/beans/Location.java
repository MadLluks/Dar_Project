package com.dar.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dar.metier.DBHandler;

public class Location extends AbstractBean{
	private int loc_id;
	private String address;
	private String postal_code;
	private String city;
	private float lat,lon;
	private Connection conn;
	
	public Location(float lat, float lon) {
		conn = DBHandler.getInstance();
		// find loc_id for lat and lon args
		// create entry if none exists
		PreparedStatement prestmt;
		try {
			String query = "SELECT loc_id FROM location WHERE lat = ? AND lon = ?";
			prestmt = this.conn.prepareStatement(query);
			prestmt.setFloat(1,lat);
			prestmt.setFloat(2, lon);
			ResultSet res = prestmt.executeQuery();
			res.next();
			this.loc_id = res.getInt("loc_id");
			res.close();
			prestmt.close();
			
		} catch (SQLException e) {
			// it does not exist yet, we add it
			try {
				String query = "INSERT INTO location(lat,lon) VALUES(?,?)";
				prestmt = this.conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				prestmt.setFloat(1,lat);
				prestmt.setFloat(2, lon);
				this.loc_id = prestmt.executeUpdate();
				prestmt.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	
	public Location(int loc_id) {
		conn = DBHandler.getInstance();
		PreparedStatement prestmt;
		try {
			String query = "SELECT lat,lon FROM location WHERE loc_id= ?";
			prestmt = this.conn.prepareStatement(query);
			prestmt.setFloat(1,loc_id);
			ResultSet res = prestmt.executeQuery();
			res.next();
			this.loc_id = loc_id;
			this.lat = res.getFloat("lat");
			this.lon = res.getFloat("lon");
			res.close();
			prestmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		return "{loc_id: "+loc_id+", lon: "+lon+", lat:"+lat+"}";
	}

	@Override
	protected boolean update() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	protected boolean insert() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
