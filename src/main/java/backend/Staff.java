package backend;

public class Staff {
    private String name;
    private int age;
    private String role;
    private String userID;
    private int salary;
    private String startDate;
    private String endDate;

    public Staff(String name, int age, String role, String userID, int salary, String startDate, String endDate) {
        this.name = name;
        this.age = age;
        this.role = role;
        this.userID = userID;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    public String getUserID() {
        return userID;
    }

    public int getSalary() {
        return salary;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
