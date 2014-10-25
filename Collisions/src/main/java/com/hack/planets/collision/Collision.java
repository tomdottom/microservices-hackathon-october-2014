package com.hack.planets.collision;

import java.util.Objects;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public final class Collision {
    private final String body1;
    private final String body2;
    private final Position position;

    public Collision(String body1, String body2, Position position) {
        this.body1 = body1;
        this.body2 = body2;
        this.position = position;
    }

    public String getBody1() {
        return body1;
    }

    public String getBody2() {
        return body2;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body1, body2, position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Collision other = (Collision) obj;
        return Objects.equals(this.body1, other.body1) && Objects.equals(this.body2, other.body2) && Objects.equals(this.position, other.position);
    }

    @Override
    public String toString() {
        return "Collision{" +
                "body1='" + body1 + '\'' +
                ", body2='" + body2 + '\'' +
                ", position=" + position +
                '}';
    }
}
