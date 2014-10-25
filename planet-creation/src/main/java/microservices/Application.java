package microservices;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import microservices.domain.Body;
import microservices.domain.CollisionEvent;
import microservices.domain.Vector;
import microservices.messaging.Publisher;
import microservices.messaging.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

    @Autowired
    private Publisher publisher;

    @Autowired
    private Subscriber subscriber;

    private Map<String, Body> bodies = new HashMap<>();

    @Override
    public void run(final String... args) throws Exception {
        final URL url = Resources.getResource("planets.json");
        final String body = Resources.toString(url, Charset.defaultCharset());
        Type listType = new TypeToken<ArrayList<Body>>() {}.getType();
        final List<Body> list = new Gson().fromJson(body, listType);
        for (Body planet : list) {
            publisher.publish(planet, "body.created");
            bodies.put(planet.getId(), planet);
        }

        subscriber.subscribe("body.collided", CollisionEvent.class, collision -> {
            final String firstId = collision.getBody1();
            final String secondId = collision.getBody2();

            final Body first = bodies.remove(firstId);
            final Body second = bodies.remove(secondId);

            publisher.publish(ImmutableMap.of("id", firstId), "body.destroyed");
            publisher.publish(ImmutableMap.of("id", secondId), "body.destroyed");

            final Body newBody = new Body(
                    first.getId() + "_" + second.getId(),
                    first.getName() + "_" + second.getName(),
                    first.getRadius() + second.getRadius(),
                    first.getMass() + second.getMass(),
                    new Vector(0, 0),
                    collision.getLocation(), //collision coordinates
                    first.getColour()
            );
            publisher.publish(newBody, "body.created");
            bodies.put(newBody.getId(), newBody);
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Application.class, args);
    }
}
