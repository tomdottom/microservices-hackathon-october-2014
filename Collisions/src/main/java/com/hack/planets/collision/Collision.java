package com.hack.planets.collision;

import java.util.Objects;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public final class Collision {
    private final String body1;
    private final String body2;

    public Collision(String body1, String body2) {
        this.body1 = body1;
        this.body2 = body2;
    }

    public String getBody1() {
        return body1;
    }

    public String getBody2() {
        return body2;
    }


    @Override
    public int hashCode() {
        return Objects.hash(body1, body2);
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
        return Objects.equals(this.body1, other.body1) && Objects.equals(this.body2, other.body2);
    }

    @Override
    public String toString() {
        return "Collision{" +
                "body1='" + body1 + '\'' +
                ", body2='" + body2 + '\'' +
                '}';
    }
}
