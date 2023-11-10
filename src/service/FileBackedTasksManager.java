package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

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
        try {
            String line = Files.readString(file.toPath());
            String[] str = line.split(System.lineSeparator());
            int maxId = -1;
            for (int i = 1; i < str.length; i++) {
                Task task = CSVFormat.fromString(str[i]);
                int id = task.getId();
                if (id > maxId) maxId = id;
                if (task.getType().equals(Types.TASK)) {
                    fileBackedTasksManager.tasks.put(id, task);
                } else if (task.getType().equals(Types.SUBTASK)) {
                    fileBackedTasksManager.subtasks.put(id, (Subtask) task);
                } else {
                    fileBackedTasksManager.epics.put(id, (Epic) task);
                }
            }

            String hLine = str[str.length - 1];
            for (Integer hId : CSVFormat.historyFromString(hLine)) {
                Task task = fileBackedTasksManager.getTaskById(hId);
                fileBackedTasksManager.historyManager.add(task);
            }

            for (Subtask subtask : fileBackedTasksManager.getSubtasks()) {
                Epic epic = fileBackedTasksManager.epics.get(subtask.getIdEpic());
                epic.setId(subtask.getId());
            }

            fileBackedTasksManager.globalId = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл" + file.getName(), e);
        }
        return fileBackedTasksManager;
    }

    void save() {
        if (!file.isFile()) {
            createFile();
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
                for (Task task : getTasks()) {
                    writer.write(task.toString());
                    writer.newLine();
                }
                for (Subtask subtask : getSubtasks()) {
                    writer.write(subtask.toString());
                    writer.newLine();
                }
                for (Epic epic : getEpics()) {
                    writer.write(epic.toString());
                    writer.newLine();
                }

                writer.newLine();
                writer.write(CSVFormat.historyToString(historyManager));
                writer.newLine();
            } catch (IOException e) {
                throw new ManagerSaveException("Не удалось сохранить в файл" + file.getName(), e);
            }
        }
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

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }


    @Override
    public void updateEpicStatus(int epId) {
        super.updateEpicStatus(epId);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }


    @Override
    public void removeTasksById(int globalId) {
        super.removeTasksById(globalId);
        save();
    }

    @Override
    public void removeEpicsById(int globalId) {
        super.removeEpicsById(globalId);
        save();
    }

    @Override
    public void removeSubtasksById(Integer id) {
        super.removeSubtasksById(id);
        save();
    }

    @Override
    public void printTask() {
        super.printTask();
        save();
    }

    @Override
    public void printEpic() {
        super.printEpic();
        save();
    }

    @Override
    public void printSubtask() {
        super.printSubtask();
        save();
    }
}
