package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest<T extends TaskManager> extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        task = new Task("Таска", "!!!!");
        manager.saveTask(task);
        epic = new Epic("Эпик", "????");
        manager.saveEpic(epic);
        subtask = new Subtask("Сабтаск", "1111", epic.getId());
        manager.saveSubtask(subtask);
    }

}
