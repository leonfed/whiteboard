package ru.leonfed.whiteboard.core.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

public abstract class PaintShape {
    protected final String id;

    protected PaintShape(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract Shape toViewShape();

    public abstract JSONObject toJson() throws JSONException;
}
