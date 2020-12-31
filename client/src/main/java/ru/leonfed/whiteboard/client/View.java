package ru.leonfed.whiteboard.client;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.util.List;

public interface View {

    void setWidth(long size);

    void setHeight(long size);

    void setVisible(boolean visible);

    void repaint(List<PaintShape> shapes);
}
