package ru.leonfed.whiteboard.client.view;

import ru.leonfed.whiteboard.core.model.PaintLine;
import ru.leonfed.whiteboard.core.model.PaintShape;
import ru.leonfed.whiteboard.core.model.Point;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.UUID;

public class ShapeConverter {

    static Point convertPoint(Point2D point2D) {
        return new Point(point2D.getX(), point2D.getY());
    }

    static PaintShape convertLine(Line2D line2D) {
        String id = UUID.randomUUID().toString();
        Point point1 = convertPoint(line2D.getP1());
        Point point2 = convertPoint(line2D.getP2());
        return new PaintLine(id, point1, point2);
    }
}
