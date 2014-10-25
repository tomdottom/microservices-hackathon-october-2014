package com.hack.planets.collision;

import java.util.*;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public class SimpleCollisionService implements CollisionService {
    HashMap<String, Movement> previousMovements;

    public SimpleCollisionService() {
        previousMovements = new HashMap<>();
    }

    @Override
    public void bodyCreated(String bodyName, Position initialPosition) {
        previousMovements.put(bodyName, new Movement(initialPosition, initialPosition));
    }

    @Override
    public void bodyMoved(String bodyName, Position initialPosition) {


    }

    @Override
    public void bodyDestroyed(String bodyName) {

    }

    @Override
    public List<Collision> calculateCollisions() {
        List<Collision> collisions = new LinkedList<>();

        for (String bodyA : previousMovements.keySet()) {
            for (String bodyB : previousMovements.keySet()) {
                if (!bodyA.equals(bodyB)) {
                    if (!collisions.contains(new Collision(bodyB, bodyA))) {
                        collisions.add(new Collision(bodyA, bodyB));
                    }
                }
            }
        }

        return collisions;
    }
}
