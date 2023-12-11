package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer server;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        manager = Managers.getDefault();
        init();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test
    public void testLoadAndSave() throws IOException, InterruptedException {
        manager.loadFromServer();

        assertEquals(5, manager.getAllTasks().size());
        assertEquals(0, manager.getHistory().size());
    }
}