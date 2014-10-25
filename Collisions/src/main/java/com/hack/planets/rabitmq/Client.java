package com.hack.planets.rabitmq;

import java.util.Map;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Client {
	private ConnectionFactory factory;

	public Client() {
		factory = new ConnectionFactory();
		factory.setHost(Config.RABBIT_MQ_SERVER);
		factory.setPort(Config.RABBIT_MQ_PORT);
	}

	public void publish(String routingKey, Map<String, Object> data) {
		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Config.EXCHANGE, "topic");

			Message message = new Message();
			message.setDetails(data);
			String string = message.getJson();

			channel.basicPublish(Config.EXCHANGE, routingKey, null,
					string.getBytes());
			System.out.println(" [x] Sent '" + routingKey + "':'" + data + "'");

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

	public ChatMsg receive(String routingKey) {
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

			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			Gson gson = new Gson();
			ChatMsg msg = gson.fromJson(message, ChatMsg.class);
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