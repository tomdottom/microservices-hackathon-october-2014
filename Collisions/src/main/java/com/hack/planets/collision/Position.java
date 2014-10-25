package com.hack.planets.collision;

import com.hack.planets.rabitmq.model.Location;

import java.util.Objects;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public final class Position {
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


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        return Objects.equals(this.x, other.x) && Objects.equals(this.y, other.y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
