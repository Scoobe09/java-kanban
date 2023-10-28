import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "task1");
        taskManager.saveTask(task1);
        Task task2 = new Task("task2", "task2");
        taskManager.saveTask(task2);
        Epic epic1 = new Epic("1", "1");
        taskManager.saveEpic(epic1);
        Subtask subtask1_1 = new Subtask("1.1", "", epic1.getId());
        taskManager.saveSubtask(subtask1_1);
        Subtask subtask1_2 = new Subtask("1.2", "", epic1.getId());
        taskManager.saveSubtask(subtask1_2);
        Subtask subtask1_3 = new Subtask("1.3", "", epic1.getId());
        taskManager.saveSubtask(subtask1_3);
        Epic epic2 = new Epic("2", "2");
        taskManager.saveEpic(epic2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1_1.getId());
        taskManager.getSubtaskById(subtask1_2.getId());
        taskManager.getSubtaskById(subtask1_3.getId());
        taskManager.getEpicById(epic2.getId());
        printHistory(taskManager);
        System.out.println("Вызываю ещё раз task1 и epic1.");
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        printHistory(taskManager);
        System.out.println("Удаляю task1.");
        taskManager.removeTasksById(task1.getId());
        printHistory(taskManager);
        System.out.println("Удаляю epic1.");
        taskManager.removeEpicsById(epic1.getId());
        printHistory(taskManager);
    }

    public static void printHistory(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}