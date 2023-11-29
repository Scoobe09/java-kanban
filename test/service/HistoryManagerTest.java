package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class HistoryManagerTest<T extends HistoryManager> {
    protected InMemoryHistoryManager manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
        task = new Task("Таска", "!!!!");
        task.setId(1);
        manager.add(task);
        epic = new Epic("Эпик", "????");
        epic.setId(2);
        manager.add(epic);
        subtask = new Subtask("Сабтаск", "1111", epic.getId());
        subtask.setId(3);
        manager.add(subtask);
    }

    @Test
    public void addTest() {
        manager.add(task);
        List<Task> str = List.of(epic, subtask, task);
        assertArrayEquals(str.toArray(), manager.getHistory().toArray());

    }

    @Test
    void testAddShouldReturnNotEmptyHistory() {
        manager.add(task);
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История пустая.");
    }

    @Test
    public void removeStart() {
        manager.remove(task.getId());
        for (Task task1 : manager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeMid() {
        manager.remove(epic.getId());
        for (Task task1 : manager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeEnd() {
        manager.remove(subtask.getId());
        for (Task task1 : manager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, manager.getHistory().size());
    }
}