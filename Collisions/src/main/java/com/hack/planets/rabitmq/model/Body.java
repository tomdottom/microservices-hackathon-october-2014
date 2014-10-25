package com.hack.planets.rabitmq.model;

public class Body {
	private String id;
	private Location location;
	
	public Body(){
	}
	
	public Body(String id, Location location) {
		this.id = id;
		this.location = location;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Body [id=" + id + ", location=" + location + "]";
	}
	
	
}
