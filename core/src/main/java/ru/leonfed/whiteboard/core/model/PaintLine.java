package ru.leonfed.whiteboard.core.model;

public class PaintLine extends PaintShape {
    private final Point start;
    private final Point end;

    public PaintLine(int id, Point start, Point end) {
        super(id);
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
