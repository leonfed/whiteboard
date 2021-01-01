package ru.leonfed.whiteboard.client.view;

import ru.leonfed.whiteboard.core.model.PaintShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ViewImpl implements View {
    private final PaintJComponent jComponent = new PaintJComponent();
    private final JFrame paint = new JFrame();

    //TODO add logging
    private Consumer<PaintShape> addShapeListener = paintShape -> {
    };

    public ViewImpl(int width, int height) {
        paint.add(jComponent);
        paint.setSize(width, height);
        paint.setLocationRelativeTo(null);
        paint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void setVisible(boolean visible) {
        paint.setVisible(visible);
    }

    @Override
    public void repaint(List<PaintShape> shapes) {
        jComponent.repaint(shapes);
    }

    @Override
    public void setAddShapeListener(Consumer<PaintShape> addShapeListener) {
        this.addShapeListener = addShapeListener;
    }

    private class PaintJComponent extends JComponent {
        private List<Shape> shapes = new ArrayList<>();

        public PaintJComponent() {
            super();
            MouseAdapter mouseAdapter = new PaintMouseAdapter();
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

        public void repaint(List<PaintShape> shapes) {
            this.shapes = shapes.stream().map(PaintShape::toViewShape).collect(Collectors.toList());
            repaint();
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            for (Shape shape : shapes) {
                g2d.draw(shape);
            }
        }
    }

    private class PaintMouseAdapter extends MouseAdapter {
        private Point previousPoint = null;
        private boolean pressedFlag = false;

        public void mousePressed(MouseEvent e) {
            previousPoint = e.getPoint();
            pressedFlag = true;
        }

        public void mouseDragged(MouseEvent e) {
            if (pressedFlag) {
                Line2D line = new Line2D.Double(previousPoint, e.getPoint());
                addShapeListener.accept(ShapeConverter.convertLine(line));
                previousPoint = e.getPoint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            pressedFlag = false;
        }
    }
}
