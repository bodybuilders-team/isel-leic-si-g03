package pt.isel.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Point {
    private Float x;
    private Float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point() {

    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
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
