import java.util.ArrayList;
import java.util.List;

public class Subtask extends Task {
    private Integer idEpic;

    public Subtask(String name, String description,Integer idEpic) {
        super(name, description);
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
        return "Subtask{" +
                "idEpic=" + idEpic +
                '}';
    }
}
