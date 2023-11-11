package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void createFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            Files.createFile(file.toPath());
            writer.println("id,type,name,status,description,epic");
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл для записи.", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String line = Files.readString(file.toPath());
            String[] str = line.split("\n");
            for (int i = 1; i < str.length; i++) {
                if (str[i].isEmpty()) {
                    if (!str[++i].isEmpty()) {
                        String hLine = str[i];
                        for (Integer hId : CSVFormat.historyFromString(hLine)) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getTaskById(hId));
                        }
                    }
                    break;
                } else {
                    int id = -1;
                    if (getType(str[i]) == Types.TASK) {
                        Task task = CSVFormat.fromString(str[i]);
                        id = task.getId();
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                    } else if (getType(str[i]) == Types.SUBTASK) {
                        Subtask subtask = (Subtask) CSVFormat.fromString(str[i]);
                        fileBackedTasksManager.subtasks.put(subtask.getId(), subtask);
                        Epic epic = fileBackedTasksManager.epics.get(subtask.getIdEpic());
                        epic.getSubTasksIds().add(subtask.getId());
                        fileBackedTasksManager.updateEpicStatus(epic.getId());
                    } else {
                        Epic epic = (Epic) CSVFormat.fromString(str[i]);
                        fileBackedTasksManager.epics.put(epic.getId(), epic);
                    }
                    if (id > fileBackedTasksManager.globalId) {
                        fileBackedTasksManager.globalId = id;
                    }
                }
            }
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
                writer.write("id,type,name,status,description,epic" + "\n");
                for (Task task : getTasks()) {
                    writer.write(task.toString() + "\n");
                }
                for (Epic epic : getEpics()) {
                    writer.write(epic.toString() + "\n");
                }
                for (Subtask subtask : getSubtasks()) {
                    writer.write(subtask.toString() + "\n");
                }


                if (!CSVFormat.historyToString(historyManager.getHistory()).isEmpty()) {
                    writer.write("\n" + CSVFormat.historyToString(historyManager.getHistory()));
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Не удалось сохранить в файл" + file.getName(), e);
            }
        }
    }

    @Override
    public Task getTaskById(int globalId) {
        historyManager.add(tasks.get(globalId));
        save();
        return tasks.get(globalId);

    }

    @Override
    public Subtask getSubtaskById(int globalId) {
        historyManager.add(subtasks.get(globalId));
        save();
        return subtasks.get(globalId);
    }

    @Override
    public Epic getEpicById(int globalId) {
        historyManager.add(epics.get(globalId));
        save();
        return epics.get(globalId);
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

    public static Types getType(String line) {
        String[] obj = line.split(",");
        Types type = Types.valueOf(obj[1]);
        if (type == Types.TASK) {
            return type;
        } else if (type == Types.SUBTASK) {
            return type;
        } else {
            return type;
        }
    }
}

