package ru.leonfed.whiteboard.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WhiteboardDaoImpl implements WhiteboardDao {

    private final HashMap<String, ArrayList<String>> usersByWhiteboard = new HashMap<>();

    @Override
    public String createWhiteboard() {
        String whiteboardId = UUID.randomUUID().toString();
        usersByWhiteboard.put(whiteboardId, new ArrayList<>());
        return whiteboardId;
    }

    @Override
    public String joinWhiteboard(String whiteboardId) throws IllegalStateException  {


        String userId = UUID.randomUUID().toString();
        usersByWhiteboard.get(whiteboardId).add(userId);
        return userId;
    }
}
