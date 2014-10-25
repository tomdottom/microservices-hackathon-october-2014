package com.hack.planets.collision;

import java.util.List;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public interface CollisionService {

    void bodyCreated(String bodyName, Position initial);

    void bodyMoved(String bodyName, Position newPosition);

    void bodyDestroyed(String bodyName);

    List<Collision> calculateCollisions();

}
