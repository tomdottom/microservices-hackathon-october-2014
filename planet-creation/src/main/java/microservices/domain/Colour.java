package microservices.domain;

public final class Colour {

    private final int red;
    private final int green;
    private final int blue;

    public Colour(final int red, final int green, final int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
