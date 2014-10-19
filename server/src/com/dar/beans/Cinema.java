package com.dar.beans;

public class Cinema {
	private float lat, lon;
	private boolean modified;
	
	public Cinema(float lat, float lon){
		this.lat = lat;
		this.lon = lon;
		this.modified = true;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
		this.modified = true;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
		this.modified = true;
	}
	public void save() {
		if(modified){
			modified = false;
			// TODO
		}
	}
}
