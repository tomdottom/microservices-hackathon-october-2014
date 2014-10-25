package com.example;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import microservices.domain.CollisionEvent;
import microservices.domain.Vector;
import microservices.messaging.Publisher;
import org.junit.Before;
import org.junit.Test;

public class PublisherTest {

    private Publisher publisher;

    @Before
    public void setUp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("178.62.106.39");
        Connection connection = factory.newConnection();
        publisher = new Publisher("combo", connection, new Gson());
    }

    @Test
    public void testPublish() throws Exception {
        String message = "testing object mapper...";
        publisher.publish(ImmutableMap.of("who", "Stas", "says", message), "chat");
    }

    @Test
    public void collision() {
        publisher.publish(new CollisionEvent("2", "3", new Vector(1, 2)), "body.collided");
    }
}
