package ru.leonfed.whiteboard.server;

import org.apache.commons.lang.StringUtils;
import ru.leonfed.whiteboard.server.dao.PaintShapesDao;
import ru.leonfed.whiteboard.server.dao.PaintShapesDaoImpl;
import ru.leonfed.whiteboard.server.dao.WhiteboardDao;
import ru.leonfed.whiteboard.server.dao.WhiteboardDaoImpl;
import ru.leonfed.whiteboard.server.service.WhiteboardService;
import ru.leonfed.whiteboard.server.service.WhiteboardServiceImpl;

import java.io.IOException;

public class ServerInitializer {

    static final String DEFAULT_HOSTNAME = "localhost";
    static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        String hostname = DEFAULT_HOSTNAME;
        int port = DEFAULT_PORT;

        if (args.length >= 1 && args[0] != null) {
            hostname = args[0];
        }

        if (args.length >= 2 && args[1] != null && StringUtils.isNumeric(args[1])) {
            port = Integer.parseInt(args[1]);
        }

        System.out.println("Start server with hostname=[" + hostname + "] and port=[" + port + "]...");

        try {
            WhiteboardDao whiteboardDao = new WhiteboardDaoImpl();
            PaintShapesDao shapesDao = new PaintShapesDaoImpl();
            WhiteboardService whiteboardService = new WhiteboardServiceImpl(whiteboardDao, shapesDao);
            Server server = new Server(hostname, port, whiteboardService);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
