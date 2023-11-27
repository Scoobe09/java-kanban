package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;



class InMemoryHistoryManagerTest <T extends HistoryManager> extends HistoryManagerTest<InMemoryHistoryManager>{
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
}