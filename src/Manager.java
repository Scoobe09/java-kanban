import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private Integer globalId = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private Integer createId() {
        return ++globalId;
    }

    public Task getTaskById(int globalId) {
        return tasks.get(globalId);
    }

    public Subtask getSubtaskById(int globalId) {
        return subtasks.get(globalId);
    }

    public Epic getEpicById(int globalId) {
        return epics.get(globalId);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubTasksByEpicId(int epicId) {
        ArrayList<Subtask> subs = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            for (Integer str : epics.get(epicId).getSubTasksIds()) {
                subs.add(subtasks.get(str));
            }
        }
        return subs;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        Integer subId = subtask.getId();
        if (subtasks.containsKey(subId)) {
            subtasks.put(subId, subtask);
            updateEpicStatus(epics.get(subtask.getIdEpic()).getId());
        }
    }

    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
            updateEpicStatus(epicId);
        }
    }

    public void saveTask(Task task) {
        if (task != null) {
            task.setId(createId());
            tasks.put(task.getId(), task);
        }
    }

    public void saveEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            epics.put(epic.getId(), epic);
        }
    }

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

    private void updateEpicStatus(int epId) {
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

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic value : epics.values()) {
                value.setStatus(TaskStatus.NEW);
                value.setSubsId(new ArrayList<>());
            }
        }
    }

    public void removeTasksById(int globalId) {
        tasks.remove(globalId);
    }

    public void removeEpicsById(int globalId) {
        if (epics.containsKey(globalId)) {
            Epic epic = epics.get(globalId);
            ArrayList<Integer> Int = epic.getSubTasksIds();
            for (Integer i : Int) {
                subtasks.remove(globalId);
            }
            epics.remove(globalId);
        }
    }

    public void removeSubtasksById(Integer id) {
        if (id != null && subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getIdEpic());
            epic.getSubTasksIds().remove(id);
            updateEpic(epic);
            subtasks.remove(id);
        }
    }

    public void printTask() {
        for (Task description : tasks.values()) {
            System.out.println(description);
        }
    }

    public void printEpic() {
        for (Task description : epics.values()) {
            System.out.println(description);
        }
    }

    public void printSubtask() {
        for (Task description : subtasks.values()) {
            System.out.println(description);
        }
    }
}
