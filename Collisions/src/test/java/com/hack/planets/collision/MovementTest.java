package com.hack.planets.collision;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementTest {
    @Test
    public void shouldHonorEqualsContract() {
        EqualsVerifier.forClass(Movement.class).verify();
    }

}