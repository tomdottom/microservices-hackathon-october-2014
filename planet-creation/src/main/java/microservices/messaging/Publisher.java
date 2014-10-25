package microservices.messaging;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Publisher {

    private final String exchangeName;
    private final Connection connection;
    private final Gson gson;

    @Autowired
    public Publisher(@Value("${rabbitmq.exchangeName}") final String exchangeName,
                     final Connection connection,
                     final Gson gson) {
        this.exchangeName = exchangeName;
        this.connection = connection;
        this.gson = gson;
    }

    public void publish(final Object message, final String topicName) {
        try {
            final Channel channel = connection.createChannel();
            final String json = gson.toJson(message);
            channel.basicPublish(exchangeName, topicName, null, json.getBytes());
            System.out.printf("Sent [%s]: %s%n", topicName, json);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}