import model.*;
import service.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Первый", "Первая таска");
        taskManager.saveTask(task1);
        Task task2 = new Task("Второй", "Вторая таска");
        taskManager.saveTask(task2);
        Epic epic1 = new Epic("Эпик1", "Первый эпик");
        taskManager.saveEpic(epic1);
        Subtask subtask1_1 = new Subtask("Саб1", "первый саб эпика", epic1.getId());
        taskManager.saveSubtask(subtask1_1);
        Subtask subtask1_2 = new Subtask("Саб2", "второй саб эпика", epic1.getId());
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