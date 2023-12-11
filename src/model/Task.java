package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    protected LocalDateTime startTime;
    protected int duration;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        status = TaskStatus.NEW;
    }


    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        if (duration == 0) {
            return null;
        }
        return startTime.plusMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + Types.TASK + "," + name + "," + status + "," + description + "," + dataTimeToString(startTime) + "," + duration;
    }

    protected String dataTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return localDateTime.format(formatter);
    }
}
