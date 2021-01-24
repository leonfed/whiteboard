package ru.leonfed.whiteboard.client.tasks;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.leonfed.whiteboard.client.controller.MainController;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RefreshViewTaskTest {

    MainController controller = mock(MainController.class);

    RefreshViewTask refreshViewTask;

    @Before
    public void initTask() {
        refreshViewTask = new RefreshViewTask(controller);
    }

    @Test
    public void runWithIncreasingTimestamps() throws IOException, JSONException {
        refreshViewTask.run();
        verify(controller, Mockito.times(1)).refreshView();
    }
}
