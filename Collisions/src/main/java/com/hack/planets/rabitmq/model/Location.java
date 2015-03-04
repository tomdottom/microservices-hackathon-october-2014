package com.hack.planets.rabitmq.model;

import java.math.BigDecimal;

public class Location {
	private BigDecimal x;
	private BigDecimal y;
	
	public Location() {
	}
	
	public Location(BigDecimal x, BigDecimal y) {
		this.x = x;
		this.y = y;
	}
	public BigDecimal getX() {
		return x;
	}
	public void setX(BigDecimal x) {
		this.x = x;
	}
	public BigDecimal getY() {
		return y;
	}
	public void setY(BigDecimal y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}
}
