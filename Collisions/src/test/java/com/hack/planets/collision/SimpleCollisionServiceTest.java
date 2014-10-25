package com.hack.planets.collision;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleCollisionServiceTest {
    private static final String BODY_ONE = "aaaaa";
    private static final String BODY_TWO = "bbbbb";
    private static final Position INTERSECTION_POINT = new Position(3, 3);
    private CollisionService instance;
    private IntersectionDetector intersectionDetector;


    @Before
    public void setUp() throws Exception {
        intersectionDetector = mock(IntersectionDetector.class);
        instance = new SimpleCollisionService(intersectionDetector);
    }

    @Test
    public void should_have_no_collision_when_no_movement() throws Exception {
        instance.bodyCreated("someBody", new Position(0, 0));

        List<?> collisions = instance.calculateCollisions();

        assertThat(collisions.size(), is(0));
    }

    @Test
    public void should_have_single_collision_for_insect_of_two_bodies() throws Exception {
        when(intersectionDetector.isCollision(anyObject(), anyObject()))
                .thenReturn(true);
        when(intersectionDetector.getIntersection(anyObject(), anyObject()))
                .thenReturn(INTERSECTION_POINT);

        instance.bodyCreated(BODY_ONE, new Position(1,0));
        instance.bodyMoved(BODY_ONE, new Position(0,1));

        instance.bodyCreated(BODY_TWO, new Position(0,0));
        instance.bodyMoved(BODY_TWO, new Position(1,1));

        List<?> collisions =instance.calculateCollisions();

        assertThat(collisions.size(), is(1));
        assertThat(collisions.get(0), is(new Collision(BODY_TWO, BODY_ONE, INTERSECTION_POINT)));
    }

    @Test
    public void should_have_no_collisions_when_no_intersect() throws Exception {
        when(intersectionDetector.isCollision(anyObject(), anyObject()))
                .thenReturn(false);

        instance.bodyCreated(BODY_ONE, new Position(0,0));
        instance.bodyMoved(BODY_ONE, new Position(0,0));

        instance.bodyCreated(BODY_TWO, new Position(1, 1));
        instance.bodyMoved(BODY_TWO, new Position(1, 1));

        List<?> collisions =instance.calculateCollisions();
        System.out.println(collisions);

        assertThat(collisions.size(), is(0));
    }
}