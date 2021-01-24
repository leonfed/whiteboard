package ru.leonfed.whiteboard.core.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.UUID;

public class ViewConverter {
    public static Point convertPoint(Point2D point2D) {
        return new Point(point2D.getX(), point2D.getY());
    }

    public static PaintLine convertLine(Line2D line2D) {
        String id = UUID.randomUUID().toString();
        Point point1 = convertPoint(line2D.getP1());
        Point point2 = convertPoint(line2D.getP2());
        return new PaintLine(id, point1, point2);
    }
}
