package ru.leonfed.whiteboard.client.tasks;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import ru.leonfed.whiteboard.client.controller.MainController;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SyncingShapesTaskTest {

    MainController controller = mock(MainController.class);

    SyncingShapesTask syncingShapesTask;

    @Before
    public void initTask() {
        syncingShapesTask = new SyncingShapesTask(controller);
    }

    @Test
    public void runWithIncreasingTimestamps() throws IOException, JSONException {

        ArrayList<Instant> postTimestamps = new ArrayList<>();
        ArrayList<Instant> getTimestamps = new ArrayList<>();

        doAnswer((Answer<Void>) invocation -> {
            Instant timestamp = (Instant) invocation.getArguments()[0];
            postTimestamps.add(timestamp);
            return null;
        }).when(controller).postShapesToServer(any(Instant.class));

        doAnswer((Answer<Void>) invocation -> {
            Instant timestamp = (Instant) invocation.getArguments()[0];
            getTimestamps.add(timestamp);
            return null;
        }).when(controller).getShapesFromServer(any(Instant.class));

        syncingShapesTask.run();
        syncingShapesTask.run();
        syncingShapesTask.run();

        assertThat(postTimestamps.size()).isEqualTo(3);
        assertThat(getTimestamps.size()).isEqualTo(3);

        ArrayList<Instant> sortedPostTimestamps = new ArrayList<>(postTimestamps);
        Collections.sort(sortedPostTimestamps);
        assertThat(postTimestamps).containsExactlyElementsOf(sortedPostTimestamps);

        ArrayList<Instant> sortedGetTimestamps = new ArrayList<>(getTimestamps);
        Collections.sort(sortedGetTimestamps);
        assertThat(getTimestamps).containsExactlyElementsOf(sortedGetTimestamps);
    }
}
