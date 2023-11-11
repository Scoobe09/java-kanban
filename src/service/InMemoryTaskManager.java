package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected Integer globalId = 0;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected Integer createId() {
        return ++globalId;
    }

    @Override
    public Task getTaskById(int globalId) {
        historyManager.add(tasks.get(globalId));
        return tasks.get(globalId);

    }

    @Override
    public Subtask getSubtaskById(int globalId) {
        historyManager.add(subtasks.get(globalId));
        return subtasks.get(globalId);
    }

    @Override
    public Epic getEpicById(int globalId) {
        historyManager.add(epics.get(globalId));
        return epics.get(globalId);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public ArrayList<Subtask> getSubTasksByEpicId(int epicId) {
        ArrayList<Subtask> subs = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            for (Integer str : epics.get(epicId).getSubTasksIds()) {
                subs.add(subtasks.get(str));
            }
        }
        return subs;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Integer subId = subtask.getId();
        if (subtasks.containsKey(subId)) {
            subtasks.put(subId, subtask);
            updateEpicStatus(epics.get(subtask.getIdEpic()).getId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void saveTask(Task task) {
        if (task != null) {
            task.setId(createId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void saveEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(createId());
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getIdEpic());
            ArrayList<Integer> subId = epic.getSubTasksIds();
            subId.add(subtask.getId());
            updateEpic(epic);
        }
    }


    @Override
    public void deleteTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        deleteSubtasks();
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer subId : subtasks.keySet()) {
            historyManager.remove(subId);
        }
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic value : epics.values()) {
                value.setStatus(TaskStatus.NEW);
                value.setSubsId(new ArrayList<>());
            }
        }
    }


    @Override
    public void removeTasksById(int globalId) {
        historyManager.remove(globalId);
        tasks.remove(globalId);
    }

    @Override
    public void removeEpicsById(int globalId) {
        historyManager.remove(globalId);
        if (epics.containsKey(globalId)) {
            Epic epic = epics.get(globalId);
            ArrayList<Integer> subsIds = epic.getSubTasksIds();
            for (Integer i : subsIds) {
                subtasks.remove(i);
                historyManager.remove(i);
            }
            epics.remove(globalId);
        }
    }

    @Override
    public void removeSubtasksById(Integer id) {
        if (id != null && subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getIdEpic());
            epic.getSubTasksIds().remove(id);
            updateEpic(epic);
            historyManager.remove(id);
            subtasks.remove(id);
        }
    }

    @Override
    public void printTask() {
        for (Task description : tasks.values()) {
            System.out.println(description);
        }
    }

    @Override
    public void printEpic() {
        for (Task description : epics.values()) {
            System.out.println(description);
        }
    }

    @Override
    public void printSubtask() {
        for (Task description : subtasks.values()) {
            System.out.println(description);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void updateEpicStatus(int epId) {
        Epic epic = epics.get(epId);
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTasksIds()) {
            statuses.add(subtasks.get(subtaskId).getStatus());
        }
        if (statuses.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        if (statuses.size() == 1) {
            epic.setStatus(statuses.get(0));
            return;
        }
        if (!statuses.contains(TaskStatus.NEW) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.DONE);
        } else if (!statuses.contains(TaskStatus.DONE) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}