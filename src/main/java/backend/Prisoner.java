package backend;

import javafx.scene.layout.HBox;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

public class Prisoner {
    private int id;
    private String name;
    private String type;
    private int age;
    private int roomID;
    private Date startDate;
    private Date endDate;

    public Prisoner(int id, String name, String type, int age, int roomID, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.roomID = roomID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Prisoner(HBox root, ArrayList<Object> previousPage, ArrayList<Object> nextPage, Map<String, Object> prisoner) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    public int getRoomID() {
        return roomID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
