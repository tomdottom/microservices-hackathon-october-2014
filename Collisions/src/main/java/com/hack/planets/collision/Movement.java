package com.hack.planets.collision;

import java.util.Objects;

/**
* Created by julianghionoiu on 25/10/2014.
*/
public final class Movement {
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

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Movement other = (Movement) obj;
        return Objects.equals(this.start, other.start) && Objects.equals(this.end, other.end);
    }

    @Override
    public String toString() {
        return "Movement{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
