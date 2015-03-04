package microservices.domain;

public final class CollisionEvent {

    private final String body1;
    private final String body2;
    private final Vector location;

    public CollisionEvent(final String body1, final String body2, final Vector location) {
        this.body1 = body1;
        this.body2 = body2;
        this.location = location;
    }

    public String getBody1() {
        return body1;
    }

    public String getBody2() {
        return body2;
    }

    public Vector getLocation() {
        return location;
    }
}
