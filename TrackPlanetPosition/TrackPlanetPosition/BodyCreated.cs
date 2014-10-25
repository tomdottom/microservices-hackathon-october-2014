using System;

namespace TrackPlanetPosition
{
    public class BodyCreated : IMessage
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public Colour Colour { get; set; }
        public Point Location { get; set; }
        public Point Vector { get; set; }
        public decimal Radius { get; set; }
    }

    public class BodyMovement : IMessage
    {
        public string Id { get; set; }
        public Point Location { get; set; }
        public Point Velocity { get; set; }
    }

    public class Point
    {
        public decimal X { get; set; }
        public decimal Y { get; set; }
    }

    public class Colour
    {
        public decimal R { get; set; }
        public decimal G { get; set; }
        public decimal B { get; set; }
    }
}