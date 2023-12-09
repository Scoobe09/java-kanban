package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.LocalDateTimeAdapter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
       return FileBackedTasksManager.loadFromFile(new File("src/text.csv"));
    }

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = gsonBuilder.create();

        return gson;
    }
}
