import model.Epic;
import model.Subtask;
import model.Task;
import server.HttpTaskServer;
import server.KVServer;
import service.Managers;
import service.TaskManager;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {


        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();


        Task task1 = new Task("Первый", "Первая таска",
                LocalDateTime.of(2020, 5, 16, 22, 15), 60);
        taskManager.saveTask(task1);

        Task task2 = new Task("Второй", "Вторая таска");
        taskManager.saveTask(task2);
        Task task3 = new Task("!!!!!", "FsfDFfw");
        taskManager.saveTask(task3);
        Epic epic1 = new Epic("Эпик1", "Первый эпик");
        taskManager.saveEpic(epic1);
        Subtask subtask1_1 = new Subtask("Саб1", "первый саб эпика",
                LocalDateTime.of(2020, 5, 16, 23, 16), 60, epic1.getId());
        taskManager.saveSubtask(subtask1_1);
        Subtask subtask1_2 = new Subtask("Саб2", "второй саб эпика",
                LocalDateTime.of(2019, 5, 16, 22, 15), 60, epic1.getId());
        taskManager.saveSubtask(subtask1_2);
        Subtask subtask1_3 = new Subtask("Саб3", "третий саб эпика", epic1.getId());
        taskManager.saveSubtask(subtask1_3);
        Epic epic2 = new Epic("Эпик2", "Второй эпик");
        taskManager.saveEpic(epic2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1_1.getId());
        taskManager.getSubtaskById(subtask1_2.getId());
        taskManager.getSubtaskById(subtask1_3.getId());
        taskManager.getEpicById(epic2.getId());
        printHistory(taskManager);

    }

    public static void printHistory(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}