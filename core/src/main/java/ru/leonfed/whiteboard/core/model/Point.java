package ru.leonfed.whiteboard.core.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject().put("x", x).put("y", y);
    }
}
