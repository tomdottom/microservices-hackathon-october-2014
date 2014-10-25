package com.hack.planets.rabitmq;

 import java.util.List;

import com.hack.planets.collision.Collision;
import com.hack.planets.collision.CollisionService;
import com.hack.planets.collision.Position;
import com.hack.planets.collision.SimpleCollisionService;
import com.hack.planets.rabitmq.model.Body;
import com.hack.planets.rabitmq.model.Time;

public class Application {
	private static CollisionService collisionService = new SimpleCollisionService();

	public static void main(String[] args) {
		Client client = new Client();

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
				if(collisions != null && !collisions.isEmpty()){
					client.publish("body.collision", collisions);
				}
			}
		}
	}
}
