package com.hack.planets.collision;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by julianghionoiu on 25/10/2014.
 */
public class SimpleCollisionService implements CollisionService {
    private HashMap<String, Movement> previousMovements;
    private IntersectionDetector intersectionDetector;

    public SimpleCollisionService() {
        this(new IntersectionDetector());
    }

    protected SimpleCollisionService(IntersectionDetector intersectionDetector) {
        this.intersectionDetector = intersectionDetector;
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
        Set<String> bodies = previousMovements.keySet();

        Consumer<String> preformCheckCollision = (String bodyA) ->
            bodies.forEach((String bodyB) -> {
                if (notSame(bodyA, bodyB) &&
                        notPreviouslyDetected(collisions, bodyA, bodyB)) {
                    Collision collision = computeCollisionBetweenTwoBodies(bodyA, bodyB);
                    if ( collision != null ) {
                        collisions.add(collision);
                    }
                }
            });

        bodies.forEach(preformCheckCollision);
        return collisions;
    }

    private Collision computeCollisionBetweenTwoBodies(String bodyA, String bodyB) {
        Collision collision = null;
        Movement moveA = previousMovements.get(bodyA);
        Movement moveB = previousMovements.get(bodyB);

        if ( intersectionDetector.isCollision(moveA, moveB) ) {
            Position intersectionPoint = intersectionDetector.getIntersection(moveA, moveB);
            collision = new Collision(bodyA, bodyB, intersectionPoint);
        }

        return collision;
    }

    //~~~~ Utils

    private boolean notPreviouslyDetected(List<Collision> collisions, String bodyA, String bodyB) {
        boolean detected = false;

        for (Collision collision : collisions) {
            String oldBodyA = collision.getBody1();
            String oldBodyB = collision.getBody2();

            if (( oldBodyA == bodyA ) && ( oldBodyB == bodyB )) {
                detected = true;
            }
            if (( oldBodyA == bodyB ) && ( oldBodyB == bodyA )) {
                detected = true;
            }
        }

        return !detected;
    }

    private boolean notSame(String bodyA, String bodyB) {
        return (bodyA != bodyB);
    }
}
