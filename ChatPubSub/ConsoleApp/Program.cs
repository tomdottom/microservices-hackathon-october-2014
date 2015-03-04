using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ChatPubSub;

namespace ConsoleApp
{
    class Program
    {
        static void Main(string[] args)
        {
            var publisher = new ChatPublisher();

            Console.WriteLine("Write 'exit' to end");
            while (true)
            {
                Console.Write("Enter new message:");
                var input = Console.ReadLine();
                if(input != null && input.ToLowerInvariant() == "exit")
                    break;
                publisher.Publish(input);

            }

            Console.ReadLine();
        }
    }
}
