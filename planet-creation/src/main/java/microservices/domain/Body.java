package microservices.domain;

public final class Body {
    private final String id;
    private final String name;
    private final double radius;
    private final double mass;
    private final Vector vector;
    private final Vector location;
    private final Colour colour;

    public Body(final String id, final String name, final double radius, final double mass, final Vector vector, final Vector location, final Colour colour) {
        this.id = id;
        this.name = name;
        this.radius = radius;
        this.mass = mass;
        this.vector = vector;
        this.location = location;
        this.colour = colour;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Vector getVector() {
        return vector;
    }

    public Vector getLocation() {
        return location;
    }

    public Colour getColour() {
        return colour;
    }
}
