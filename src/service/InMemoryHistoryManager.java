package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node first;
    private Node last;
    private Map<Integer, Node> tasks = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            Node newNode = new Node(task, null, null);
            if (last == null) {
                first = newNode;
            } else {
                last.next = newNode;
                remove(task.getId());
            }
            last = newNode;
            tasks.put(task.getId(), newNode);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node current = first;
        while (current != null) {
            result.add(current.task);
            current = current.next;
        }
        return result;
    }

    @Override
    public void remove(int id) {
        Node remove = tasks.remove(id);
        if (remove != null) {
            if (remove.next == null && remove.prev == null) {
                first = null;
                if (remove.prev == null) {
                    first = remove.next;
                    remove.prev = null;
                } else if (remove.next == null) {
                    last = remove.prev;
                    remove.next = null;
                } else {
                    remove.prev.next = remove.next;
                    remove.next.prev = remove.prev;
                }
            }
        }
    }
}
