package ru.leonfed.whiteboard.server;

import org.apache.commons.lang.StringUtils;
import ru.leonfed.whiteboard.core.logging.Logger;
import ru.leonfed.whiteboard.core.logging.LoggerConfig;
import ru.leonfed.whiteboard.core.logging.LoggerConfig.LoggerLevel;
import ru.leonfed.whiteboard.core.logging.LoggerFactory;
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

    static final LoggerLevel LOGGER_LEVEL = LoggerLevel.INFO;

    static final Logger log = LoggerFactory.logger(ServerInitializer.class);

    public static void main(String[] args) {
        LoggerConfig.setLoggerLevel(LOGGER_LEVEL);

        String hostname = DEFAULT_HOSTNAME;
        int port = DEFAULT_PORT;

        if (args.length >= 1 && args[0] != null) {
            hostname = args[0];
        }

        if (args.length >= 2 && args[1] != null && StringUtils.isNumeric(args[1])) {
            port = Integer.parseInt(args[1]);
        }

        log.info("Start server with hostname=[" + hostname + "] and port=[" + port + "]...");

        try {
            WhiteboardDao whiteboardDao = new WhiteboardDaoImpl();
            PaintShapesDao shapesDao = new PaintShapesDaoImpl();
            WhiteboardService whiteboardService = new WhiteboardServiceImpl(whiteboardDao, shapesDao);
            Server server = new Server(hostname, port, whiteboardService);
            server.start();
        } catch (IOException e) {
            log.error("Exception of initializing server", e);
        }
    }
}
