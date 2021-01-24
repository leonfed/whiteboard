package ru.leonfed.whiteboard.core.model;

import java.util.Random;
import java.util.UUID;

/**
 * Using for generating random objects in testing other modules.
 */
public class ModelGenerators {

    static Random random = new Random();

    public static Point generatePoint() {
        return new Point(random.nextDouble(), random.nextDouble());
    }

    public static PaintLine generatePaintLine() {
        return new PaintLine(UUID.randomUUID().toString(), generatePoint(), generatePoint());
    }

}
