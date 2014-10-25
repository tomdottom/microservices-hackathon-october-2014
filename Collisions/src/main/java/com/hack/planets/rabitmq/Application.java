package com.hack.planets.rabitmq;

 import java.util.List;

import com.hack.planets.collision.Collision;
import com.hack.planets.collision.CollisionService;
import com.hack.planets.collision.Position;
import com.hack.planets.collision.SimpleCollisionService;
import com.hack.planets.rabitmq.model.Body;
import com.hack.planets.rabitmq.model.Time;
 import org.slf4j.LoggerFactory;

public class Application {
	private static CollisionService collisionService = new SimpleCollisionService();

	public static void main(String[] args) {
		Client client = new Client();

        LoggerFactory.getLogger(Application.class).info("Starting to listen for messages");

		while(true){
			Body msg = client.receiveBody("body.created");
			if(msg != null){
				collisionService.bodyCreated(msg.getId(), new Position(msg.getLocation()));
			}
			
			msg = client.receiveBody("body.movement");
			if(msg != null){
				collisionService.bodyMoved(msg.getId(), new Position(msg.getLocation()));
			}
			
			msg = client.receiveBody("body.destroyed");
			if(msg != null){
				collisionService.bodyDestroyed(msg.getId());
			}
			
			Time tmsg = client.receiveTime("time");
			if(tmsg != null){
				List<Collision> collisions = collisionService.calculateCollisions();
                LoggerFactory.getLogger(Application.class).info("Resulting collisions: "+collisions);
                if(collisions != null && !collisions.isEmpty()){
                    LoggerFactory.getLogger(Application.class).info("Publishing");
                    client.publish("body.collision", collisions);
				}
			}
		}
	}
}
