package ru.leonfed.whiteboard.core.model;

import org.junit.Test;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewConverterTest {
    static Random random = new Random();

    @Test
    public void convertViewToModel() {
        double x1 = random.nextDouble();
        double y1 = random.nextDouble();
        double x2 = random.nextDouble();
        double y2 = random.nextDouble();

        Line2D line2D = new Line2D.Double(x1, y1, x2, y2);

        PaintLine paintLine = ViewConverter.convertLine(line2D);
        Point point1 = paintLine.getPoint1();
        Point point2 = paintLine.getPoint2();

        assertThat(point1.getX()).isEqualTo(x1);
        assertThat(point1.getY()).isEqualTo(y1);
        assertThat(point2.getX()).isEqualTo(x2);
        assertThat(point2.getY()).isEqualTo(y2);
    }

    @Test
    public void convertModelToView() {
        PaintLine paintLine = ModelGenerators.generatePaintLine();

        Shape viewShape = paintLine.toViewShape();
        assertThat(viewShape).isInstanceOf(Line2D.class);
        Line2D line2D = (Line2D) viewShape;

        PaintLine actualPaintLine = ViewConverter.convertLine(line2D);
        assertThat(actualPaintLine.getPoint1()).isEqualTo(paintLine.getPoint1());
        assertThat(actualPaintLine.getPoint2()).isEqualTo(paintLine.getPoint2());
    }
}
