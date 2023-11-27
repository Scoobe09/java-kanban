package service;

import java.io.File;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
     // return new InMemoryTaskManager();
       return FileBackedTasksManager.loadFromFile(new File("src/text.csv"));
    }
}
