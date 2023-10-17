package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

   private static final int MAX = 10;
   private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> add(Task task) {
        history.add(task);
    return new ArrayList<>(history);
    }

    @Override
    public List<Task> getHistory() {
        if(history.size() == MAX){
            history.remove(0);
        }
        return history;
    }
}

