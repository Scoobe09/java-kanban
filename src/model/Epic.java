package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;
    Types type;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();

    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
        this.subTasksIds = subsId;
    }

    @Override
    public String toString() {
        return id + "," + Types.EPIC + "," + name + "," + status + "," + description;
    }
}
