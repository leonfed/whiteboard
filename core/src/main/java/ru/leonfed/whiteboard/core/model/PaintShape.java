package ru.leonfed.whiteboard.core.model;

public abstract class PaintShape {
    protected final int id;

    protected PaintShape(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
