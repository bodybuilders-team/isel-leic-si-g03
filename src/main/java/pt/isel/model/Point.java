package pt.isel.model;

import jakarta.persistence.Embeddable;

/**
 * Represents a point in a 2D space.
 */
@Embeddable
public class Point {

    private Float x;
    private Float y;

    /**
     * Creates a Point instance.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Needed for JPA...
    public Point() {}


    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate.
     */
    public Float getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the x coordinate.
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate.
     */
    public Float getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the y coordinate.
     */
    public void setY(Float y) {
        this.y = y;
    }


    /**
     * Parses a String to a Point.
     *
     * @param location String to be parsed
     * @return parsed Point
     */
    public static Point parsePoint(String location) {
        float x = Float.parseFloat(location.substring(1).split(",")[0]);
        float y = Float.parseFloat(location.substring(0, location.length() - 1).split(",")[1]);

        return new Point(x, y);
    }

    /**
     * Converts a Point to a String.
     *
     * @return String with the point coordinates
     */
    @Override
    public String toString() {
        return "(" + this.getX() + "," + this.getY() + ")";
    }
}
