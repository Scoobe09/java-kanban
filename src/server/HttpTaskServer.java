package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;

    private HttpServer server;
    private Gson gson;

    private TaskManager taskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server1 = new HttpTaskServer();
        server1.start();
        // server1.stop();
    }

    private void handler(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String request = httpExchange.getRequestMethod();
            switch (request) {
                case "GET":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        System.out.println(path);
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(httpExchange, response);
                        return;
                    }
                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        String pathID = path.replaceFirst("id=", "");
                        int id = parseInt(pathID);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен неверный id = " + pathID);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        String pathID = path.replaceFirst("id=", "");
                        int id = parseInt(pathID);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен не верный id = " + pathID);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        String pathID = path.replaceFirst("/tasks/subtask/", "");
                        int id = parseInt(pathID);
                        if (id != 1) {
                            String response = gson.toJson(taskManager.getSubtaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен не верный id = " + pathID);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    }
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    if (checkBody(httpExchange, body)) {
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        Task task = gson.fromJson(body, Task.class);
                        if (taskManager.getAllTasks().contains(task)) {

                            taskManager.updateTask(task);
                        } else {
                            taskManager.saveTask(task);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (taskManager.getAllTasks().contains(subtask)) {

                            taskManager.updateSubtask(subtask);
                        } else {
                            taskManager.saveSubtask(subtask);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (taskManager.getAllTasks().contains(epic)) {

                            taskManager.updateEpic(epic);
                        } else {
                            taskManager.saveEpic(epic);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        String pathID = path.replaceFirst("/tasks/task/", "");
                        int id = parseInt(pathID);
                        if (id != 1) {
                            taskManager.removeTasksById(id);
                            System.out.println("Задача с id - " + id + " удалена.");
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен не верный id = " + pathID);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                default:
                    System.out.println("Ждём GET или DELETE запрос, а получили - " + request);
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parseInt(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту: " + PORT);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private boolean checkBody(HttpExchange exchange, String body) throws IOException {
        if (body.isEmpty()) {
            exchange.sendResponseHeaders(400, 0);
            System.out.println("Body is empty");
            return true;
        }
        return false;
    }
}
