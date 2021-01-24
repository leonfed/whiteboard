package ru.leonfed.whiteboard.core.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonConverterTest {

    @Test
    public void convertLine() throws JSONException {
        PaintLine paintLine = ModelGenerators.generatePaintLine();
        JSONObject jsonObject = paintLine.toJson();
        PaintShape actualPaintShape = JsonConverter.fromJsonPaintShape(jsonObject);
        assertThat(actualPaintShape).isInstanceOf(PaintLine.class);
        assertThat(actualPaintShape).isEqualTo(paintLine);
    }

    @Test(expected = JSONException.class)
    public void putWithNullKey() throws JSONException {
        JSONObject jsonObject = new JSONObject().put("type", "unknown-type");
        JsonConverter.fromJsonPaintShape(jsonObject);
    }

    @Test
    public void convertLines() throws JSONException {
        PaintShape paintLine1 = ModelGenerators.generatePaintLine();
        PaintShape paintLine2 = ModelGenerators.generatePaintLine();
        JSONObject jsonShapeList = JsonConverter.toJsonPaintShapes(List.of(paintLine1, paintLine2));
        List<PaintShape> actualShapeList = JsonConverter.fromJsonPaintShapes(jsonShapeList);
        assertThat(actualShapeList).containsExactlyInAnyOrder(paintLine1, paintLine2);
    }
}
