package pt.isel;

import java.awt.geom.Point2D;

public class Utils {

    public static Point2D.Float parsePoint(String location) {
        float x = Float.parseFloat(location.substring(1).split(",")[0]);
        float y = Float.parseFloat(
                location
                        .substring(0, location.length() - 1)
                        .split(",")[1]
        );

        return new Point2D.Float(x, y);
    }

    public static String pointToString(Point2D.Float location) {
        return "(" + location.x + "," + location.y + ")";
    }
}
