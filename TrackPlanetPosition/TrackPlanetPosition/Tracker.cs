using System;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;

namespace TrackPlanetPosition
{
    public class Tracker
    {
        private const string ExchangeName = "combo";
        private const string BodyCreated = "body.created";
        private const string BodyMovement = "body.movement";

        public readonly IDictionary<string, BodyCreated> Planets = new Dictionary<string, BodyCreated>();

        public void Subscribe()
        {
            var factory = new ConnectionFactory { HostName = "178.62.106.39", Port = 5672 };
            using (var connection = factory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                    channel.ExchangeDeclare(ExchangeName, "topic");
                    var queueName = channel.QueueDeclare().QueueName;

                    channel.QueueBind(queueName, ExchangeName, BodyCreated);
                    channel.QueueBind(queueName, ExchangeName, BodyMovement);

                    var consumer = new QueueingBasicConsumer(channel);
                    channel.BasicConsume(queueName, true, consumer);

                    while (true)
                    {
                        var deliveryArgs = consumer.Queue.Dequeue();
                        var body = deliveryArgs.Body;
                        var message = Encoding.UTF8.GetString(body);
                        var topic = deliveryArgs.RoutingKey;
                        switch (topic)
                        {
                            case BodyCreated:
                                var created = DeserialiseBodyCreated(message);
                                if (created != null)
                                    Save(created);
                                break;
                            case BodyMovement:
                                var movement = DeserialiseBodyMovement(message);
                                if (movement != null)
                                    Save(movement);
                                break;
                        }
                    }
                }
            }
        }

        private void Save(IMessage message)
        {
            var movement = message as BodyMovement;
            BodyCreated entry;
            if (movement != null && Planets.TryGetValue(movement.Id, out entry))
            {
                entry.Location = movement.Location;
                return;
            }

            var created = message as BodyCreated;
            if (created == null) return;

            if (Planets.ContainsKey(created.Id))
            {
                Planets[created.Id] = created;
            }
            else
            {
                Planets.Add(created.Id, created);
            }
        }

        public static BodyCreated DeserialiseBodyCreated(string message)
        {
            return JsonConvert.DeserializeObject<BodyCreated>(message);
        }

        private static BodyMovement DeserialiseBodyMovement(string message)
        {
            return JsonConvert.DeserializeObject<BodyMovement>(message);
        }
    }
}