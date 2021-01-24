package ru.leonfed.whiteboard.core.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Objects;

public class PaintLine extends PaintShape {
    private final Point point1;
    private final Point point2;

    public PaintLine(String id, Point point1, Point point2) {
        super(id);
        this.point1 = point1;
        this.point2 = point2;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    @Override
    public Shape toViewShape() {
        return new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return new JSONObject()
                .put("type", "line")
                .put("id", id)
                .put("point1", point1.toJson())
                .put("point2", point2.toJson());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaintLine paintLine = (PaintLine) o;
        return id.equals(paintLine.id) && Objects.equals(point1, paintLine.point1) && Objects.equals(point2, paintLine.point2);
    }
}
