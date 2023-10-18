package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_TASK_IN_HISTORY = 10;
    private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> add(Task task) {
        history.add(task);
        if (history.size() == MAX_TASK_IN_HISTORY) {
            history.remove(0);
        }
        return history;

    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}

