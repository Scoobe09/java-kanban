package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer idEpic;

    public Subtask(String name, String description, Integer idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Integer idEpic) {
        super(name, description, startTime);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, LocalDateTime startTime, int duration, Integer idEpic) {
        super(name, description, startTime, duration);
        this.idEpic = idEpic;
    }


    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return id + "," + Types.SUBTASK + "," + name + "," + status + "," + description + "," + dataTimeToString(startTime) + "," + duration + "," + idEpic;
    }
}
