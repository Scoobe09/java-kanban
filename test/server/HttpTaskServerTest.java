package server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager manager;
    private Gson gson = Managers.getGson();


    private HttpClient httpClient;
    private Task task;
    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpClient = HttpClient.newHttpClient();
        Task task1 = new Task("Первый", "Первая таска",
                LocalDateTime.of(2020, 5, 16, 22, 15), 60);
        manager.saveTask(task1);

        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(task, actual.get(1));
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Task>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(task, actual.get(1));
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}