using System;
using System.Text;
using Newtonsoft.Json.Linq;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace ChatPubSub
{
    public class ChatPublisher
    {
        public void Publish(string message)
        {
            var factory = new ConnectionFactory { HostName = "178.62.106.39", Port = 5672 };
            using (var connection = factory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                    var jsonMessage = PrepareMessage("vimal", message);
                    var body = Encoding.UTF8.GetBytes(jsonMessage);
                    channel.BasicPublish("combo", "chat", null, body);
                }
            }
        }

        public void Subscribe()
        {
            var factory = new ConnectionFactory { HostName = "178.62.106.39", Port = 5672 };
            using (var connection = factory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                    var consumer = new QueueingBasicConsumer(channel);
                    channel.BasicConsume(null, true, consumer);
                    while (true)
                    {
                        var args = consumer.Queue.Dequeue();
                        var body = args.Body;
                        var message = Encoding.UTF8.GetString(body);
                    }
                }
            }
        }

        private static string PrepareMessage(string name, string message)
        {
            var jObject = new JObject {{"who", name}, {"says", message}};
            return jObject.ToString();
        }

        private JObject GetJsonObject(string message)
        {
            var jObject = JObject.Parse(message);
            if (jObject != null)
                var data = new Tuple<string, string>(jObject.GetValue("who"), jObject.GetValue("says"));

        }
    }
}