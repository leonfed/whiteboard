package ru.leonfed.whiteboard.server.dao;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhiteboardDaoImplTest {

    WhiteboardDao whiteboardDao;

    @Before
    public void initDao() {
        whiteboardDao = new WhiteboardDaoImpl();
    }

    @Test
    public void createWhiteboardWithoutUsers() {
        String whiteboard = whiteboardDao.createWhiteboard();
        assertThat(whiteboardDao.getUsers(whiteboard).size()).isEqualTo(0);
    }

    @Test
    public void getUsers() {
        String whiteboard = whiteboardDao.createWhiteboard();
        String user1 = whiteboardDao.joinWhiteboard(whiteboard);
        String user2 = whiteboardDao.joinWhiteboard(whiteboard);
        assertThat(whiteboardDao.getUsers(whiteboard)).containsExactlyInAnyOrder(user1, user2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void joinWithIncorrectWhiteboard() {
        whiteboardDao.joinWhiteboard("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserWithIncorrectWhiteboard() {
        whiteboardDao.getUsers("unknown");
    }
}
