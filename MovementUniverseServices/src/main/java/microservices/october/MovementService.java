package microservices.october;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import microservices.october.infrastructure.Publisher;
import microservices.october.infrastructure.Subscriber;
import microservices.october.messages.BodyCreationMessage;
import microservices.october.messages.BodyDestroyedMessage;
import microservices.october.messages.BodyForceMessage;
import microservices.october.messages.BodyMovementMessage;

import java.util.HashMap;
import java.util.Map;

import static microservices.october.Configuration.EXCHANGE_NAME;
import static microservices.october.Topics.*;

public class MovementService {
    private ConnectionFactory connectionFactory = setUpConnectionFactory();
    private Publisher publisher = new Publisher(connectionFactory, EXCHANGE_NAME);
    private Subscriber subscriber = new Subscriber(connectionFactory, EXCHANGE_NAME, BODY_FORCE_TOPIC, BODY_CREATED_TOPIC, BODY_DESTROYED_TOPIC);
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, BodyCreationMessage> bodies = new HashMap<>();

    private ConnectionFactory setUpConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configuration.CONNECTION_HOST);
        factory.setPort(Configuration.CONNECTION_PORT);
        return factory;
    }

    private void handleBodyDescription(BodyCreationMessage bodyCreationMessage) throws Exception {
        bodies.put(bodyCreationMessage.id, bodyCreationMessage);
    }

    private void handleBodyForce(BodyForceMessage bodyForceMessage) throws Exception {
        // 1. check if there is a body with provided id
        BodyCreationMessage bodyDescription = bodies.get(bodyForceMessage.id);
        if (bodyDescription == null) {
            System.out.println("Unknown body: " + bodyForceMessage.id);
            return;
        }
        // 2. get mass of it
        double mass = bodyDescription.mass;
        // 3. calculate new velocity
        double ax = bodyForceMessage.force.x / mass;
        double ay = bodyForceMessage.force.y / mass;

        bodyDescription.velocity.x += ax * 1;
        bodyDescription.velocity.y += ay * 1;
        System.out.println("Body " + bodyForceMessage.id + " is moving by (" + bodyDescription.velocity.x + ", " + bodyDescription.velocity.y + ")");
        // 4. calculate new location
        bodyDescription.location.x += bodyDescription.velocity.x;
        bodyDescription.location.y += bodyDescription.velocity.y;
        // 5. publish
        BodyMovementMessage bodyMovementMessage = new BodyMovementMessage();
        bodyMovementMessage.id = bodyDescription.id;
        bodyMovementMessage.location = bodyDescription.location;
        bodyMovementMessage.velocity = bodyDescription.velocity;
        publisher.publish(Topics.BODY_MOVEMENT_TOPIC, objectMapper.writeValueAsString(bodyMovementMessage));
    }

    private void processOneMessage() throws Exception {
        QueueingConsumer.Delivery delivery = subscriber.getNextMessage();
        String message = new String(delivery.getBody());
        String topic = delivery.getEnvelope().getRoutingKey();

        System.out.println("Received a message on '" + topic + "' '" + message + "'");

        if (topic.equals(BODY_CREATED_TOPIC)) {
            BodyCreationMessage bodyCreationMessage = objectMapper.readValue(message, BodyCreationMessage.class);
            handleBodyDescription(bodyCreationMessage);
        } else if (topic.equals(BODY_FORCE_TOPIC)) {
            BodyForceMessage bodyForceMessage = objectMapper.readValue(message, BodyForceMessage.class);
            handleBodyForce(bodyForceMessage);
        } else if (topic.equals(BODY_DESTROYED_TOPIC)) {
            BodyDestroyedMessage bodyDestroyedMessage = objectMapper.readValue(message, BodyDestroyedMessage.class);
            handleBodyDestroyed(bodyDestroyedMessage);
        }
    }

    private void handleBodyDestroyed(BodyDestroyedMessage bodyDestroyedMessage) {
        bodies.remove(bodyDestroyedMessage.id);
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
        new MovementService().enterInfiniteLoop();
    }
}
