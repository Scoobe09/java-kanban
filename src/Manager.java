import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private Integer id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private Integer createId() {
        return ++id;
    }

    public Task getTasksById(int id) {
        return tasks.get(id);
    }

    public Task getSubtasksById(int id) {
        return subtasks.get(id);
    }

    public Task getEpicsById(int id) {
        return epics.get(id);
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

    public ArrayList<Subtask> getSubByEpic(int id) {
        ArrayList<Subtask> subs = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer str : epics.get(id).getSubsId()) {
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
            ArrayList<Integer> subId = epic.getSubsId();
            subId.add(subtask.getId());
            updateEpic(epic);
        }
    }

    private void updateEpicStatus(int epId) {
        Epic epic = epics.get(epId);
        ArrayList<Statuses> statuses = new ArrayList<>();
        for (Integer subtaskId : epic.getSubsId()) {
            statuses.add(subtasks.get(subtaskId).getStatus());
        }
        if (statuses.isEmpty()) {
            epic.setStatus(Statuses.NEW);
            return;
        }
        if (statuses.size() == 1) {
            epic.setStatus(statuses.get(0));
            return;
        }
        if (!statuses.contains(Statuses.NEW) && !statuses.contains(Statuses.IN_PROGRESS)) {
            epic.setStatus(Statuses.DONE);
        } else if (!statuses.contains(Statuses.DONE) && !statuses.contains(Statuses.IN_PROGRESS)) {
            epic.setStatus(Statuses.NEW);
        } else {
            epic.setStatus(Statuses.IN_PROGRESS);
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
                value.setStatus(Statuses.NEW);
                value.setSubsId(new ArrayList<>());
            }
        }
    }

    public void removeTasks(int id) {
        tasks.remove(id);
    }

    public void removeEpics(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> Int = epic.getSubsId();
            for (Integer i : Int) {
                subtasks.remove(id);
            }
            epics.remove(id);
        }
    }

    public void removeSubtasks(Integer id) {
        if (id != null && subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getIdEpic());
            epic.getSubsId().remove(id);
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
