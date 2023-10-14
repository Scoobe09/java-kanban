import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subsId;

    public Epic(String name, String description) {
        super(name, description);
        subsId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void setSubsId(ArrayList<Integer> subsId) {
        this.subsId = subsId;
    }
}
