package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;// = new File("E:\\Work\\Projects\\java-kanban\\src", "text.csv");


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void createFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            Files.createFile(file.toPath());
            writer.println("id,type,name,status,description,epic");
        } catch (IOException e) {
            System.out.println("Не удалось создать файл для записи.");
            e.printStackTrace();
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String line = Files.readString(file.toPath());
                String[] str = line.split(System.lineSeparator());
                for (int i = 1; i < str.length; i++) {
                    Task task = CSVFormat.fromString(str[i]);
                    int id = task.getId();
                    if (task.getType().equals(Types.TASK)) {
                        fileBackedTasksManager.tasks.put(id, task);
                    } else if (task.getType().equals(Types.SUBTASK)) {
                        fileBackedTasksManager.subtasks.put(id, (Subtask) task);
                    } else {
                        fileBackedTasksManager.epics.put(id, (Epic) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл" + file.getName(), e);
        }
        return fileBackedTasksManager;
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (Task task : getTasks()) {
                writer.println(task.toString());
            }
            for (Subtask subtask : getSubtasks()) {
                writer.println(subtask.toString());
            }
            for (Epic epic : getEpics()) {
                writer.println(epic.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл" + file.getName(), e);
        }
    }

}
