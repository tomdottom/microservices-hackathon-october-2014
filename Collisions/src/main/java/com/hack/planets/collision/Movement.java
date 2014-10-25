package com.hack.planets.collision;

/**
* Created by julianghionoiu on 25/10/2014.
*/
public class Movement {
    private final Position start;
    private final Position end;

    public Movement(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }
}
