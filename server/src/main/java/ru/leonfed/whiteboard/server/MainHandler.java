package ru.leonfed.whiteboard.server;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

public interface MainHandler {

    /**
     * @param after       - timestamp
     * @param excludeUser - user, who want to get new shapes
     * @return new shapes which is upload to server after timestamp 'after' and not by 'excludeUser'
     */
    List<PaintShape> getNewShapes(Instant after, int excludeUser);

    /**
     * @param user - user, who post new shapes
     */
    void storeNewShapes(List<PaintShape> shapes, int user);
}
