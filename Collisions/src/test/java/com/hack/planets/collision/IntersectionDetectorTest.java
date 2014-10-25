package com.hack.planets.collision;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by danielvaughan on 25/10/14.
 */
public class IntersectionDetectorTest {

    private IntersectionDetector intersectionDetector;

    @Before
    public void setup()
    {
        intersectionDetector = new IntersectionDetector();
    }

    @Test
    public void not_detect_collision_for_no_movement()
    {
        assertFalse(intersectionDetector.isCollision(new Movement(new Position(1, 1), new Position(0, 0)), new Movement(new Position(1, 1), new Position(0, 0))));
    }

    @Test
    public void detect_collision_for_overlapping_paths()
    {
        assertTrue(intersectionDetector.isCollision(new Movement(new Position(0, 0), new Position(1, 1)), new Movement(new Position(1, 0), new Position(0, 1))));
        Position intersection = intersectionDetector.getIntersection(new Movement(new Position(0, 0), new Position(1, 1)), new Movement(new Position(1, 0), new Position(0, 1)));
                System.out.println(intersection);
    }

}
