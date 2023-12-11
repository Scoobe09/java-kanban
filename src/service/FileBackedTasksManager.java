package service;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try {
                String line = Files.readString(file.toPath());
                String[] str = line.split("\n");
                for (int i = 1; i < str.length; i++) {
                    if (str[i].isEmpty()) {
                        if (!str[++i].isEmpty()) {
                            String hLine = str[i];
                            for (Integer hId : CSVFormat.historyFromString(hLine)) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.takeFromMap(hId));
                            }
                        }
                        break;
                    } else {
                        fileBackedTasksManager.putToMap(str[i]);
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Не удалось прочитать файл" + file.getName(), e);
            }
        }
        fileBackedTasksManager.tasks.entrySet().forEach(System.out::println);
        return fileBackedTasksManager;
    }

    private void putToMap(String str) {
        String[] split = str.split(",");
        Integer id1 = Integer.parseInt(split[0]);
        if (id1 > globalId) {
            globalId = id1;
        }
        if (getType(str) == Types.TASK) {
            Task task = CSVFormat.fromString(str);
            if (crossingCheck(task)) {
                id1 = task.getId();
                tasks.put(id1, task);
                priority.add(task);
            }
        } else if (getType(str) == Types.SUBTASK) {
            Subtask subtask = (Subtask) CSVFormat.fromString(str);
            if (crossingCheck(subtask)) {
                id1 = subtask.getId();
                subtasks.put(id1, subtask);
                priority.add(subtask);
                Epic epic = epics.get(subtask.getIdEpic());
                epic.getSubTasksIds().add(subtask.getId());
                updateEpicStatus(epic.getId());
            }
        } else {
            Epic epic = (Epic) CSVFormat.fromString(str);
            id1 = epic.getId();
            epics.put(id1, epic);
        }
    }

    void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,starTime,duration,epic" + "\n");
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


    private Task takeFromMap(Integer id) {
        Task task = tasks.get(id);
        if (task == null) {
            task = epics.get(id);
        }
        if (task == null) {
            task = subtasks.get(id);
        }
        return task;
    }

    @Override
    public Task getTaskById(Integer globalId) {
        Task task = super.getTaskById(globalId);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer globalId) {
        Subtask subtask = super.getSubtaskById(globalId);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer globalId) {
        Epic epic = super.getEpicById(globalId);
        save();
        return epic;
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
    public void updateEpicStatus(Integer epId) {
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
    public void removeTasksById(Integer globalId) {
        super.removeTasksById(globalId);
        save();
    }

    @Override
    public void removeEpicsById(Integer globalId) {
        super.removeEpicsById(globalId);
        save();
    }

    @Override
    public void removeSubtasksById(Integer id) {
        super.removeSubtasksById(id);
        save();
    }

    public static Types getType(String line) {
        String[] obj = line.split(",");
        Types type = Types.valueOf(obj[1]);
        if (type == Types.TASK) {
            return Types.TASK;
        } else if (type == Types.SUBTASK) {
            return Types.SUBTASK;
        } else {
            return Types.EPIC;
        }
    }
}

