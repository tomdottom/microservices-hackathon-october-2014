using System;
using TrackPlanetPosition;

namespace ConsoleTest
{
    class Program
    {
        static void Main(string[] args)
        {
            var tracker = new Tracker();
            tracker.Subscribe();
        }

        public static void MessageAppeared(IMessage message)
        {
            Console.WriteLine(message.ToString());
        }
    }
}
