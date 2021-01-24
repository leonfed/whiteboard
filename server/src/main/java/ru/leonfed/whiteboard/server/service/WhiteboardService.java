package ru.leonfed.whiteboard.server.service;

import ru.leonfed.whiteboard.core.model.PaintShape;

import java.time.Instant;
import java.util.List;

public interface WhiteboardService {

    /**
     * @return whiteboard id
     */
    String createWhiteboard();

    /**
     * @return user id
     * @throws IllegalStateException if whiteboard with that id does not exist
     */
    String joinToWhiteboard(String whiteboardId) throws IllegalStateException ;


    /**
     * @param userId - user, who post new shapes
     */
    void addShapes(String whiteboardId, String userId, List<PaintShape> shapes);

    /**
     * @param excludeUserId - user, who want to get new shapes
     * @return new shapes which is upload to server after timestamp 'after' and not by 'excludeUser'
     */
    List<PaintShape> getShapes(String whiteboardId, String excludeUserId, Instant after);
}
