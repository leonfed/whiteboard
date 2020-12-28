package ru.leonfed.whiteboard.client.draft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AwtPaint {

    public static void paintWindow() {
        JFrame paint = new JFrame();

        paint.add(new JComponent() {
            final ArrayList<Shape> shapes = new ArrayList<>();
            Line2D currentLine = null;
            boolean pressedFlag = false;

            {
                MouseAdapter mouseAdapter = new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        pressedFlag = true;
                        currentLine = new Line2D.Double(e.getPoint(), e.getPoint());
                        shapes.add(currentLine);
                        repaint();
                    }

                    public void mouseDragged(MouseEvent e) {
                        if (pressedFlag) {
                            currentLine.setLine(currentLine.getP1(), e.getPoint());
                            currentLine = new Line2D.Double(e.getPoint(), e.getPoint());
                            shapes.add(currentLine);
                            repaint();
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        currentLine = null;
                        pressedFlag = false;
                        repaint();
                    }
                };
                addMouseListener(mouseAdapter);
                addMouseMotionListener(mouseAdapter);
            }

            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                for (Shape shape : shapes) {
                    g2d.draw(shape);
                }
                System.out.println(shapes.size());
            }
        });

        paint.setSize(500, 500);
        paint.setLocationRelativeTo(null);
        paint.setVisible(true);
        paint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
