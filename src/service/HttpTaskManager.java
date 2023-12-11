package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        this(url, false);
    }

    public HttpTaskManager(String url, Boolean load) throws IOException, InterruptedException {
        super(null);
        client = new KVTaskClient(url);
        if (load) {
            loadFromServer();
        }
        gson = Managers.getGson();
    }

    public void loadFromServer() throws IOException, InterruptedException {
        List<Task> tasksFromServer = gson.fromJson(client.load("/tasks"), new TypeToken<List<Task>>() {
        }.getType());
        if (tasksFromServer != null) {
            for (Task task : tasksFromServer) {
                int id = task.getId();
                globalId = Math.max(globalId, id);
                tasks.put(id, task);
                priority.add(task);
            }
        }
        List<Epic> epicFromServer = gson.fromJson(client.load("/epics"), new TypeToken<List<Epic>>() {
        }.getType());
        if (epicFromServer != null) {
            for (Epic epic : epicFromServer) {
                int id = epic.getId();
                globalId = Math.max(globalId, id);
                epics.put(id, epic);
                priority.add(epic);
            }
        }
        List<Subtask> subFromServer = gson.fromJson(client.load("/subtasks"), new TypeToken<List<Subtask>>() {
        }.getType());
        if (subFromServer != null) {
            for (Subtask subtask : subFromServer) {
                int id = subtask.getId();
                globalId = Math.max(globalId, id);
                subtasks.put(id, subtask);
                priority.add(subtask);
            }
        }
    }

    @Override
    public void save() {
        try {
            client.put("/tasks", gson.toJson(getTasks()));
            client.put("/subtasks", gson.toJson(getSubtasks()));
            client.put("/epics", gson.toJson(getEpics()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
