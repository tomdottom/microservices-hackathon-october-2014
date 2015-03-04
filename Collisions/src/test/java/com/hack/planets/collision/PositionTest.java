package com.hack.planets.collision;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class PositionTest {
    @Test
    public void shouldHonorEqualsContract() {
        EqualsVerifier.forClass(Position.class).verify();
    }

}