package ru.leonfed.whiteboard.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteboardDaoImpl implements WhiteboardDao {

    private final ConcurrentHashMap<String, ArrayList<String>> usersByWhiteboard = new ConcurrentHashMap<>();

    @Override
    public String createWhiteboard() {
        String whiteboardId = UUID.randomUUID().toString();
        usersByWhiteboard.put(whiteboardId, new ArrayList<>());
        return whiteboardId;
    }

    @Override
    public String joinWhiteboard(String whiteboardId) throws IllegalArgumentException {
        if (!usersByWhiteboard.containsKey(whiteboardId)) {
            throw new IllegalArgumentException("Whiteboard with this id does not exist");
        }

        String userId = UUID.randomUUID().toString();
        usersByWhiteboard.get(whiteboardId).add(userId);
        return userId;
    }

    @Override
    public List<String> getUsers(String whiteboardId) throws IllegalArgumentException {
        if (!usersByWhiteboard.containsKey(whiteboardId)) {
            throw new IllegalArgumentException("Whiteboard with this id does not exist");
        }

        return usersByWhiteboard.get(whiteboardId);
    }
}
