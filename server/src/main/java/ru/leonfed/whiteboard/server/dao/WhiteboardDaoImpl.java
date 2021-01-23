package ru.leonfed.whiteboard.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WhiteboardDaoImpl implements WhiteboardDao {

    private final HashMap<String, ArrayList<String>> users = new HashMap<>();

    @Override
    public String createWhiteboard() {
        //todo generate id
        //String whiteboardId = UUID.randomUUID().toString();
        String whiteboardId = "123";
        users.put(whiteboardId, new ArrayList<>());
        return whiteboardId;
    }

    @Override
    public String joinWhiteboard(String whiteboardId) {
        String userId = UUID.randomUUID().toString();
        users.get(whiteboardId).add(userId);
        return userId;
    }
}
