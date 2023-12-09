package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Task getTaskById(Integer globalId);

    Subtask getSubtaskById(Integer globalId);

    Epic getEpicById(Integer globalId);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubTasksByEpicId(Integer epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubtask(Subtask subtask);


    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void removeTasksById(Integer globalId);

    void removeEpicsById(Integer globalId);

    void removeSubtasksById(Integer id);
    ArrayList<Task> getAllTasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}


