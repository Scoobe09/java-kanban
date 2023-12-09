package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    public void beforeEach() {
        file = new File("src/text.csv");
        manager = new FileBackedTasksManager(file);
        init();
    }

    @Override
    @Test
    void saveTask() {
        super.saveTask();
        assertTrue(file.exists());
    }

    @Test
    public void saveTest() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter str = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            str.newLine();
            str.write("1,TASK,Таска,NEW,chad,null,0");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String s = manager.getTaskById(task.getId()).toString();
        assertEquals(s, FileBackedTasksManager.loadFromFile(file).getTaskById(task.getId()).toString());
    }

    @Test
    public void loadFromFileTest() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertFalse(manager.tasks.isEmpty());
        assertFalse(manager.subtasks.isEmpty());
        assertFalse(manager.epics.isEmpty());
    }

    @Test
    public void crossingCheckTest() {
        Subtask subtask1 = new Subtask("Сабтаск111", "саб1",
                LocalDateTime.of(2023, 2, 15, 22, 30), 60, epic.getId());
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск111", "саб2",
                LocalDateTime.of(2023, 2, 15, 22, 30), 60, epic.getId());
        manager.saveSubtask(subtask2);

        assertEquals(3, manager.getPrioritizedTasks().size());
    }
}