package ru.leonfed.whiteboard.server.dao;

import java.util.List;

public interface WhiteboardDao {

    String createWhiteboard();

    String joinWhiteboard(String whiteboardId) throws IllegalArgumentException;

    List<String> getUsers(String whiteboardId) throws IllegalArgumentException;
}
