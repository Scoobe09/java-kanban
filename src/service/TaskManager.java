package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Task getTaskById(int globalId);

    Subtask getSubtaskById(int globalId);

    Epic getEpicById(int globalId);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubTasksByEpicId(int epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubtask(Subtask subtask);

    void updateEpicStatus(int epId);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void removeTasksById(int globalId);

    void removeEpicsById(int globalId);

    void removeSubtasksById(Integer id);

    void printTask();

    void printEpic();

    void printSubtask();


    List<Task> getHistory();
}


