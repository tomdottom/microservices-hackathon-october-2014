package com.hack.planets.rabitmq;

import java.util.List;

import com.google.gson.Gson;
import com.hack.planets.collision.Collision;
import com.hack.planets.rabitmq.model.Body;
import com.hack.planets.rabitmq.model.Time;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Client {
	private static final long TIMEOUT = 100;
	
	private ConnectionFactory factory;

	public Client() {
		factory = new ConnectionFactory();
		factory.setHost(Config.RABBIT_MQ_SERVER);
		factory.setPort(Config.RABBIT_MQ_PORT);
	}

	public void publish(String routingKey, List<Collision> collisions) {
		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");

			Gson gson = new Gson();
			String json = gson.toJson(collisions);

			channel.basicPublish(Config.EXCHANGE, routingKey, null,
					json.getBytes());
			System.out.println(" [x] Sent '" + routingKey + "':'" + json + "'");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public Body receiveBody(String routingKey) {
		Connection connection = null;
		Channel channel = null;
		try {

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");
			String queueName = channel.queueDeclare().getQueue();

			channel.queueBind(queueName, Config.EXCHANGE, routingKey);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			QueueingConsumer.Delivery delivery = consumer.nextDelivery(TIMEOUT);
			if(delivery == null){
				return  null;
			}
			String message = new String(delivery.getBody());
			Gson gson = new Gson();
			Body msg = gson.fromJson(message, Body.class);
//			Message msg = new Message();
//			msg.setFromJson(message);
			return msg;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
			
		}
	}
	
	public Time receiveTime(String routingKey) {
		Connection connection = null;
		Channel channel = null;
		try {

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");
			String queueName = channel.queueDeclare().getQueue();

			channel.queueBind(queueName, Config.EXCHANGE, routingKey);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			QueueingConsumer.Delivery delivery = consumer.nextDelivery(TIMEOUT);
			if(delivery == null){
				return  null;
			}
			String message = new String(delivery.getBody());
			Gson gson = new Gson();
			Time msg = gson.fromJson(message, Time.class);
//			Message msg = new Message();
//			msg.setFromJson(message);
			return msg;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
			
		}
	}
}