package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest <T extends HistoryManager>{
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @Test
    public void addTest(){
        manager.add(task);
        List<Task> str = List.of(epic, subtask,task );
        assertArrayEquals(str.toArray(), manager.getHistory().toArray());

    }

    @Test
    void add() {
        manager.add(task);
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");
    }

@Test
    public void removeStart(){
        manager.remove(task.getId());
    for (Task task1 : manager.getHistory()) {
        System.out.println(task1);
    }
    assertEquals(2, manager.getHistory().size());
}

    @Test
    public void removeMid(){
        manager.remove(epic.getId());
        for (Task task1 : manager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeEnd(){
        manager.remove(subtask.getId());
        for (Task task1 : manager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, manager.getHistory().size());
    }
}