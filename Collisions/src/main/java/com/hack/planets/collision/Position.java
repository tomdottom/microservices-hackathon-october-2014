package com.hack.planets.collision;

import com.hack.planets.rabitmq.model.Location;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public class Position {
    private final float x;
    private final float y;
    
    public Position(Location location){
    	this.x = location.getX().floatValue();
        this.y = location.getY().floatValue();
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
