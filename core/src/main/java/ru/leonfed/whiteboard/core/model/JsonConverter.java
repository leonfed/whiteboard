package ru.leonfed.whiteboard.core.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    public static Point fromJsonPoint(JSONObject jsonObject) throws JSONException {
        double x = jsonObject.getDouble("x");
        double y = jsonObject.getDouble("y");
        return new Point(x, y);
    }

    public static PaintShape fromJsonPaintShape(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getString("type");
        if (type.equals("line")) {
            String id = jsonObject.getString("id");
            Point point1 = fromJsonPoint(jsonObject.getJSONObject("point1"));
            Point point2 = fromJsonPoint(jsonObject.getJSONObject("point2"));
            return new PaintLine(id, point1, point2);
        } else {
            throw new JSONException("Type of paint shape is absent");
        }
    }

    public static List<PaintShape> fromJsonPaintShapes(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("shapes");
        ArrayList<PaintShape> paintShapes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            PaintShape paintShape = fromJsonPaintShape(jsonArray.getJSONObject(i));
            paintShapes.add(paintShape);
        }
        return paintShapes;
    }

    public static JSONObject toJsonPaintShapes(List<PaintShape> paintShapes) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (PaintShape paintShape : paintShapes) {
            jsonArray.put(paintShape.toJson());
        }
        return new JSONObject().put("shapes", jsonArray);
    }
}
