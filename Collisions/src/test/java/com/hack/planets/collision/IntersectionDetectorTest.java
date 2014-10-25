package com.hack.planets.collision;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

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
    public void test()
    {
        assertTrue(intersectionDetector.isCollision(new Movement(new Position(0,0), new Position(0,0)), new Movement(new Position(0,0), new Position(0,0))));
    }


}
