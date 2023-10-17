package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    public List<Task> add(Task task);

    public List<Task> getHistory();
}
