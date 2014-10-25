package microservices.october;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import microservices.october.infrastructure.Publisher;
import microservices.october.infrastructure.Subscriber;
import microservices.october.messages.BodyCreationMessage;
import microservices.october.messages.BodyForceMessage;
import microservices.october.messages.Point;
import microservices.october.messages.UniverseMessage;

import static microservices.october.Configuration.*;
import static microservices.october.Topics.BODY_FORCE_TOPIC;
import static microservices.october.Topics.BODY_UNIVERSE;

public class RandomForceService {
    private ConnectionFactory connectionFactory = setUpConnectionFactory();
    private Publisher publisher = new Publisher(connectionFactory, EXCHANGE_NAME);
    private Subscriber subscriber = new Subscriber(connectionFactory, EXCHANGE_NAME, BODY_FORCE_TOPIC, BODY_UNIVERSE);
    private ObjectMapper objectMapper = new ObjectMapper();

    private ConnectionFactory setUpConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CONNECTION_HOST);
        factory.setPort(CONNECTION_PORT);
        return factory;
    }

    private void handleUniverseMessage(UniverseMessage universeMessage) throws Exception {
        for (BodyCreationMessage bodyCreationMessage : universeMessage.bodies) {
            BodyForceMessage bodyForceMessage = new BodyForceMessage();
            bodyForceMessage.id = bodyCreationMessage.id;
            bodyForceMessage.force = new Point();
            bodyForceMessage.force.x = Math.random();
            bodyForceMessage.force.y = Math.random();
            publisher.publish(BODY_FORCE_TOPIC, objectMapper.writeValueAsString(bodyForceMessage));
        }
    }

    private void processOneMessage() throws Exception {
        QueueingConsumer.Delivery delivery = subscriber.getNextMessage();
        String message = new String(delivery.getBody());
        String topic = delivery.getEnvelope().getRoutingKey();

        System.out.println("Received a message on '" + topic + "' '" + message + "'");

        if (topic.equals(BODY_UNIVERSE)) {
            UniverseMessage universeMessage = objectMapper.readValue(message, UniverseMessage.class);
            handleUniverseMessage(universeMessage);
        }
    }

    public void enterInfiniteLoop() throws Exception {
        while (true) {
            try {
                processOneMessage();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new RandomForceService().enterInfiniteLoop();
    }
}
