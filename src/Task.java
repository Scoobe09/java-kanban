public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected Statuses status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Statuses.NEW;
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

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }
}
