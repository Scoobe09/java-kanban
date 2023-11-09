package model;

import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class CSVFormat {

    public static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
    }

    public static Task fromString(String value) {
        // String[] str = value.split(System.lineSeparator());
        //  for (String pen : str) {
        String[] obj = value.split(",");
        Types type = Types.valueOf(obj[1]);
        String name = obj[2];
        String description = obj[4];
        if (type.equals(Types.TASK)) {
            return new Task(name, description);
        }
        if (type.equals(Types.SUBTASK)) {
            int idEpic = Integer.parseInt(obj[6]);
            return new Subtask(name, description, idEpic);
        }
        type = Types.EPIC;
        return new Epic(name, description);

    }


    public static String historyToString(HistoryManager manager) {
        StringBuilder ids = new StringBuilder();
        for (Task str : manager.getHistory()) {
            ids.append(str.getId());
        }
        return ids.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] str = value.split(",");
        List<Integer> ids = new ArrayList<>(str.length);
        int id = Integer.parseInt(str[str.length - 1]);
        ids.add(id);

        return ids;
    }
}
