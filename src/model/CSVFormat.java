package model;


import java.util.ArrayList;
import java.util.List;

public class CSVFormat {

    public static Task fromString(String value) {
        String[] obj = value.split(",");
        int id = Integer.parseInt(obj[0]);
        Types type = Types.valueOf(obj[1]);
        String name = obj[2];
        TaskStatus status = TaskStatus.valueOf(obj[3]);
        String description = obj[4];
        if (type.equals(Types.TASK)) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        } else if (type.equals(Types.EPIC)) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else {
            Subtask subtask = new Subtask(name, description, Integer.parseInt(obj[5]));
            subtask.setId(id);
            subtask.setStatus(status);
            return subtask;
        }
    }

    public static String historyToString(List<Task> tasks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : tasks) {
            stringBuilder.append(task.getId() + ",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] str = value.split(",");
        List<Integer> ids = new ArrayList<>(str.length);
        int id = Integer.parseInt(str[str.length - 1]);
        ids.add(id);

        return ids;
    }
}
