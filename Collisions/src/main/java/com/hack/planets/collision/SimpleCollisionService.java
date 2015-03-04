package com.hack.planets.collision;

import org.slf4j.LoggerFactory;

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
        LoggerFactory.getLogger(SimpleCollisionService.class).info("Body "+bodyName+" created at: "+initialPosition);
        previousMovements.put(bodyName, new Movement(initialPosition, initialPosition));
    }

    @Override
    public void bodyMoved(String bodyName, Position actualPosition) {
        LoggerFactory.getLogger(SimpleCollisionService.class).info("Body "+bodyName+" moved to: "+actualPosition);
        if (previousMovements.containsKey(bodyName)) {
            Movement previous = previousMovements.get(bodyName);
            Movement current = new Movement(previous.getEnd(), actualPosition);
            previousMovements.put(bodyName, current);
        } else {
            bodyCreated(bodyName, actualPosition);
        }
    }

    @Override
    public void bodyDestroyed(String bodyName) {
        LoggerFactory.getLogger(SimpleCollisionService.class).info("Body "+bodyName+" destroyed");
        previousMovements.remove(bodyName);
    }

    @Override
    public List<Collision> calculateCollisions() {
        LoggerFactory.getLogger(SimpleCollisionService.class).info("Computing collisions");

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
