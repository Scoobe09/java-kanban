package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest<T extends TaskManager> extends TaskManagerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    public void beforeEach() {
        file = new File("src/text.csv");
        manager = new FileBackedTasksManager(file);
        task = new Task("task", "!!!!");
        manager.saveTask(task);
        epic = new Epic("epic", "????");
        manager.saveEpic(epic);
        subtask = new Subtask("sub", "1111", epic.getId());
        manager.saveSubtask(subtask);
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
        assertTrue(file.exists());
    }
}
