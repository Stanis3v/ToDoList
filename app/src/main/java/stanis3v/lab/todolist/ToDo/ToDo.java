package stanis3v.lab.todolist.ToDo;

public class ToDo {
    private int id;
    private final String title;
    private final String des;
    private final String remind;

    public ToDo(int id, String title, String des, String remind) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.remind = remind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getRemind() {
        return remind;
    }

}
