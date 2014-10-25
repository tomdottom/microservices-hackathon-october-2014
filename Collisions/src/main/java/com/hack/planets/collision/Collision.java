package com.hack.planets.collision;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public class Collision {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collision collision = (Collision) o;

        if (!body1.equals(collision.body1)) return false;
        if (!body2.equals(collision.body2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = body1.hashCode();
        result = 31 * result + body2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Collision{" +
                "body1='" + body1 + '\'' +
                ", body2='" + body2 + '\'' +
                '}';
    }
}
