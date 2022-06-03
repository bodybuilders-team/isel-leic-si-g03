package pt.isel;

import java.awt.geom.Point2D;

public class Utils {

    /**
     * Parses a String to a Point2D.
     *
     * @param location String to be parsed
     * @return parsed Point2D
     */
    public static Point2D.Float parsePoint(String location) {
        float x = Float.parseFloat(location.substring(1).split(",")[0]);
        float y = Float.parseFloat(
                location
                        .substring(0, location.length() - 1)
                        .split(",")[1]
        );

        return new Point2D.Float(x, y);
    }

    /**
     * Converts a Point2D to a String.
     *
     * @param location Point2D to be converted
     * @return String with the point coordinates
     */
    public static String pointToString(Point2D.Float location) {
        return "(" + location.x + "," + location.y + ")";
    }
}
