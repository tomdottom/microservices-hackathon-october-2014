package com.hack.planets.rabitmq;

import java.util.HashMap;
import java.util.Map;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String routingKey = "chat";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("who", "Juan and Tom");
		data.put("says", "Hello");
		
		Client client = new Client();
		//client.publish(routingKey, data);
		ChatMsg msg = client.receive(routingKey);
		System.out.println(msg.toString());
		
		
	}

}
