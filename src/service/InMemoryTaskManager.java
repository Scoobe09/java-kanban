package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Integer globalId = 0;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected final Set<Task> priority = new TreeSet<>(new StartComparator());
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected Integer createId() {
        return ++globalId;
    }

    @Override
    public Task getTaskById(Integer globalId) {
        historyManager.add(tasks.get(globalId));
        return tasks.get(globalId);
    }

    @Override
    public Subtask getSubtaskById(Integer globalId) {
        historyManager.add(subtasks.get(globalId));
        return subtasks.get(globalId);
    }

    @Override
    public Epic getEpicById(Integer globalId) {
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
    public ArrayList<Subtask> getSubTasksByEpicId(Integer epicId) {
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
            if (crossingCheck(task)) {
                tasks.put(task.getId(), task);
                priority.add(task);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Integer subId = subtask.getId();
        if (subtasks.containsKey(subId)) {
            subtasks.put(subId, subtask);
            priority.add(subtask);
            updateEpicStatus(epics.get(subtask.getIdEpic()).getId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
            updateEpicStatus(epicId);
            updateEpicTime(epic);
        }
    }

    @Override
    public void saveTask(Task task) {
        if (task != null) {
            if (crossingCheck(task)) {
                task.setId(createId());
                tasks.put(task.getId(), task);
                priority.add(task);
            }
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
            if (crossingCheck(subtask)) {
                subtask.setId(createId());
                subtasks.put(subtask.getId(), subtask);
                priority.add(subtask);
                Epic epic = epics.get(subtask.getIdEpic());
                ArrayList<Integer> subId = epic.getSubTasksIds();
                subId.add(subtask.getId());
                updateEpicStatus(epic.getId());
                updateEpicTime(epic);
            }
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
    public void removeTasksById(Integer globalId) {
        historyManager.remove(globalId);
        tasks.remove(globalId);
    }

    @Override
    public void removeEpicsById(Integer globalId) {
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void updateEpicStatus(Integer epId) {
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

    protected void updateEpicTime(Epic epic) {
        List<Subtask> subs = epic.getSubTasksIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
        LocalDateTime epicStart = subs.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        LocalDateTime epicEnd = subs.stream()
                .map(subtask -> {
                    LocalDateTime strStartTime = subtask.getStartTime();
                    Integer durat = subtask.getDuration();
                    return (strStartTime != null) ? strStartTime.plusMinutes(durat) : null;
                })
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        epic.setStartTime(epicStart);
        epic.setEndTime(epicEnd);
    }

    protected static class StartComparator implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() == null || o2.getStartTime() == null) {
                if (o1.getStartTime() != null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priority);
    }

    protected Boolean crossingCheck(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        for (Task task1 : priority) {
            if (task1.getStartTime() != null) {
                if ((task.getStartTime().isAfter(task1.getStartTime()) || task.getStartTime().equals(task1.getStartTime())) &&
                        (task.getStartTime().isBefore(task1.getEndTime()) || task.getStartTime().equals(task1.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }
}