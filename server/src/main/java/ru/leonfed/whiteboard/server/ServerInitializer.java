package ru.leonfed.whiteboard.server;

import ru.leonfed.whiteboard.server.dao.PaintShapesDao;
import ru.leonfed.whiteboard.server.dao.PaintShapesDaoImpl;
import ru.leonfed.whiteboard.server.dao.WhiteboardDao;
import ru.leonfed.whiteboard.server.dao.WhiteboardDaoImpl;
import ru.leonfed.whiteboard.server.service.WhiteboardService;
import ru.leonfed.whiteboard.server.service.WhiteboardServiceImpl;

import java.io.IOException;

public class ServerInitializer {

    public static void main(String[] args) {
        System.out.println("It does something :|");

        try {
            WhiteboardDao whiteboardDao = new WhiteboardDaoImpl();
            PaintShapesDao shapesDao = new PaintShapesDaoImpl();

            WhiteboardService whiteboardService = new WhiteboardServiceImpl(whiteboardDao, shapesDao);

            Server server = new Server(whiteboardService);
            server.start();
        } catch (IOException e) {
            System.out.println("Handle exception: " + e.getMessage());
        }

    }
}
