package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @Test
    void saveTask() {
        final int taskId = task.getId();

        final Task savedTask = manager.getTaskById(taskId);
        assertNotNull(task, "Задача empty");
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void saveEpic() {
        final int id = epic.getId();

        final Epic saved = manager.getEpicById(id);
        assertNotNull(epic, "epic is empty");
        assertNotNull(saved, "Задача не найдена.");
        assertEquals(epic, saved, "Задачи не совпадают.");

        final List<Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void saveSubtask() {
        final int id = subtask.getId();
        final int epId = subtask.getIdEpic();
        final Subtask saved = manager.getSubtaskById(id);
        assertNotNull(subtask, "sub is empty");
        assertNotNull(saved, "Задача не найдена.");
        assertEquals(subtask, saved, "Задачи не совпадают.");

        final List<Subtask> subtasks = manager.getSubtasks();
        assertEquals(epic.getId(), epId);
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getTaskByIdNull() {
        Task exp = manager.getTaskById(null);
        assertNull(exp, "Нулевая таска");
    }

    @Test
    public void getTaskByIdOk() {
        Task exp = manager.getTaskById(task.getId());
        assertEquals(task, exp, "Неверная таска");
    }

    @Test
    public void getTaskByIdDifferent() {
        Task exp = manager.getTaskById(256);
        assertNotEquals(task, exp, "Неверное ID");
    }

    @Test
    public void getSubtaskByIdNull() {
        Subtask sub = manager.getSubtaskById(null);
        assertNull(sub, "Нулевой саб");
    }

    @Test
    public void getSubtaskByIdOk() {
        Subtask sub = manager.getSubtaskById(subtask.getId());
        assertEquals(subtask, sub, "Неверный саб");
    }

    @Test
    public void getSubtaskByIdDifferent() {
        Subtask sub = manager.getSubtaskById(119);
        assertNotEquals(subtask, sub, "Неверное ID");
    }

    @Test
    public void getEpicByIdNull() {
        Epic exp = manager.getEpicById(null);
        assertNull(exp, "Нулевая эпик");
    }

    @Test
    public void getEpicByIdOk() {
        Epic exp = manager.getEpicById(epic.getId());
        assertEquals(epic, exp, "Неверный саб");
    }

    @Test
    public void getEpicByIdDifferent() {
        Epic exp = manager.getEpicById(333);
        assertNotEquals(epic, exp, "Неверное ID");
    }

    @Test
    public void updateTask() {
        task.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Ошибка обновления статуса IN_PROGRESS");

        task.setStatus(TaskStatus.DONE);
        manager.updateTask(task);
        assertEquals(TaskStatus.DONE, task.getStatus(), "Ошибка обновления статуса DONE");
    }

    @Test
    public void updateSubtask() {
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(subtask);
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus(), "Ошибка обновления статуса IN_PROGRESS");

        subtask.setStatus(TaskStatus.DONE);
        manager.updateTask(subtask);
        assertEquals(TaskStatus.DONE, subtask.getStatus(), "Ошибка обновления статуса DONE");
    }

    @Test
    public void updateEpic() {
        epic.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(epic);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Ошибка обновления статуса IN_PROGRESS");

        epic.setStatus(TaskStatus.DONE);
        manager.updateTask(epic);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Ошибка обновления статуса DONE");
    }

    @Test
    public void getTasks() {
        List<Task> str = new ArrayList<>(manager.getTasks());
        for (Task task1 : str) {
            assertNotNull(task1);
        }
        assertArrayEquals(str.toArray(), manager.getTasks().toArray());
    }

    @Test
    public void getSubtasks() {
        List<Subtask> str = new ArrayList<>(manager.getSubtasks());
        for (Subtask task1 : str) {
            assertNotNull(task1);
        }
        assertArrayEquals(str.toArray(), manager.getSubtasks().toArray());
    }

    @Test
    public void getEpics() {
        List<Epic> str = new ArrayList<>(manager.getEpics());
        for (Epic task1 : str) {
            assertNotNull(task1);
        }
        assertArrayEquals(str.toArray(), manager.getEpics().toArray());
    }

    @Test
    public void getSubTasksByEpicId() {
        assertNotNull(subtask);
        assertNotNull(epic);

        List<Subtask> str = new ArrayList<>();
        str.add(subtask);
        assertArrayEquals(str.toArray(), manager.getSubTasksByEpicId(epic.getId()).toArray());
    }

    @Test
    public void removeTasksById() {
        manager.getTaskById(task.getId());
        manager.removeTasksById(task.getId());
        assertTrue(manager.getTasks().isEmpty());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void removeSubtasksById() {
        manager.getSubtaskById(subtask.getId());
        manager.removeSubtasksById(subtask.getId());
        assertEquals(0, manager.getSubtasks().size());

        assertEquals(1, manager.getHistory().size());

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void removeEpicById() {
        manager.getEpicById(epic.getId());
        manager.removeEpicsById(epic.getId());
        assertEquals(0, manager.getEpics().size());

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void deleteTasks() {
        manager.deleteTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    public void deleteEpics() {
        manager.deleteEpics();
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void deleteSubtasks() {
        manager.deleteSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void getHistory() {
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        manager.getTaskById(task.getId());
        List<Task> str = List.of(subtask, task);
        assertArrayEquals(str.toArray(), manager.getHistory().toArray());
    }

    @Test
    public void updateEpicStatus() {
        Subtask subtask1 = new Subtask("Сабтаск111", "miniNigga", epic.getId());
        manager.saveSubtask(subtask1);

        subtask.setStatus(TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Ошибка обновления статуса NEW");

        subtask.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.DONE);

        manager.updateSubtask(subtask);
        manager.updateSubtask(subtask1);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Ошибка обновления статуса DONE");

        subtask.setStatus(TaskStatus.NEW);
        manager.updateSubtask(subtask);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Ошибка обновления статуса IN_PROGRESS");

        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Ошибка обновления статуса IN_PROGRESS");

        manager.deleteSubtasks();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void updateEpicTime() {
        Subtask subtask1 = new Subtask("Сабтаск111", "miniNigga",
                LocalDateTime.of(2023, 2, 15, 22, 30), 60, epic.getId());
        manager.saveSubtask(subtask1);
        LocalDateTime str = subtask1.getStartTime();

        assertEquals(epic.getEndTime(), str.plusMinutes(subtask1.getDuration()), "Неправильное обновление конца эпика");
    }

    @Test
    public void crossingCheckTest() {
        Subtask subtask1 = new Subtask("Сабтаск111", "саб1",
                LocalDateTime.of(2023, 2, 15, 22, 30), 60, epic.getId());
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск111", "саб2",
                LocalDateTime.of(2023, 2, 15, 22, 30), 60, epic.getId());
        manager.saveSubtask(subtask2);

        assertEquals(51, manager.getPrioritizedTasks().size());
    }
}
