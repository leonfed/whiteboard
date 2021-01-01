package ru.leonfed.whiteboard.client.view;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.List;
import java.util.function.Consumer;

public interface View {

    void setVisible(boolean visible);

    void repaint(List<PaintShape> shapes);

    void setAddShapeListener(Consumer<PaintShape> addShapeListener);
}
