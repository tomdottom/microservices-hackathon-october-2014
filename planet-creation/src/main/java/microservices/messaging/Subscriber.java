package microservices.messaging;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Subscriber {

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Autowired
    private Connection connection;

    @Autowired
    private Gson gson;

    public <T> void subscribe(String topicName, Class<T> classOfT, MessageProcessor<T> processor) {
        try {
            final Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, "topic");
            final String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, topicName);

            System.out.println("Waiting for messages. To exit press CTRL+C");

            final QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

            while (true) {
                final QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                final String body = new String(delivery.getBody());
                System.out.printf("Received [%s] %s%n", topicName, body);
                final T t = gson.fromJson(body, classOfT);
                processor.process(t);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static interface MessageProcessor<T> {
        void process(T message) throws IOException;
    }
}
