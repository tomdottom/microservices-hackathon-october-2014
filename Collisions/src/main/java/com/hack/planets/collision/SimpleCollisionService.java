package com.hack.planets.collision;

import java.util.*;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public class SimpleCollisionService implements CollisionService {

    Set<String> bodies;
    HashMap<String, Position> currentPositions;
    HashMap<String, Position> lastPositions;

    public SimpleCollisionService() {
        bodies = new HashSet<>();
        currentPositions = new HashMap<>();
        lastPositions = new HashMap<>();
    }

    @Override
    public void bodyCreated(String bodyName, Position initialPosition) {
        bodies.add(bodyName);
    }

    @Override
    public void bodyMoved(String bodyName, Position initialPosition) {
        Position lastPosition = currentPositions.get(bodyName);
        currentPositions.put(bodyName, initialPosition);
        lastPositions.put(bodyName, lastPosition);
    }

    @Override
    public void bodyDestroyed(String bodyName) {

    }

    @Override
    public List<Collision> calculateCollisions() {
        List<Collision> collisions = new LinkedList<>();

        for (String bodyA : bodies) {
            for (String bodyB : bodies) {
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
