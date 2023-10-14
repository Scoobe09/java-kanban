import java.util.ArrayList;
import java.util.List;

public class Subtask extends Task {
    private Integer idEpic;


    public Subtask(String name, String description) {
        super(name, description); // TODO: 12.10.2023
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
