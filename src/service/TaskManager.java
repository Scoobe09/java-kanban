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

    public ArrayList<Task> getTasks();

    public ArrayList<Subtask> getSubtasks();

    public ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubTasksByEpicId(int epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubtask(Subtask subtask);

    void updateEpicStatus(int epId);

    public void deleteTasks();

    public void deleteEpics();

    public void deleteSubtasks();

    void removeTasksById(int globalId);

    public void removeEpicsById(int globalId);

    void removeSubtasksById(Integer id);

    public void printTask();

    public void printEpic();

    public void printSubtask();


    List<Task> getHistory();
}


