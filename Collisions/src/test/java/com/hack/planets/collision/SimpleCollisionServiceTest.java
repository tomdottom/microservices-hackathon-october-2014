package com.hack.planets.collision;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleCollisionServiceTest {
    private static final String BODY_ONE = "aaaaa";
    private static final String BODY_TWO = "bbbbb";
    private CollisionService instance;


    @Before
    public void setUp() throws Exception {
        instance = new SimpleCollisionService();
    }

    @Test
    public void one_body_and_no_movement_should_have_not_collision() throws Exception {
        instance.bodyCreated("someBody", new Position(0,0));

        List<?> collisions = instance.calculateCollisions();

        assertThat(collisions.size(), is(0));
    }

    @Test
    public void return_single_collision_for_insect_of_two_bodies() throws Exception {
        instance.bodyCreated(BODY_ONE, new Position(1,0));
        instance.bodyMoved(BODY_ONE, new Position(0,1));

        instance.bodyCreated(BODY_TWO, new Position(0,0));
        instance.bodyMoved(BODY_TWO, new Position(1,1));

        List<?> collisions =instance.calculateCollisions();
        System.out.println(collisions);

        assertThat(collisions.size(), is(1));
    }

    @Test
    public void register_no_collisions_when_no_intersect() throws Exception {
        instance.bodyCreated(BODY_ONE, new Position(0,0));
        instance.bodyMoved(BODY_ONE, new Position(0,0));

        instance.bodyCreated(BODY_TWO, new Position(1,1));
        instance.bodyMoved(BODY_TWO, new Position(1,1));

        List<?> collisions =instance.calculateCollisions();
        System.out.println(collisions);

        assertThat(collisions.size(), is(0));
    }
}