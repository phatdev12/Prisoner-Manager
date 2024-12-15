package backend;

import java.sql.*;
import java.util.ArrayList;

// mysql
public class Database {
    private Connection connection = null;
    public ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();
    public ArrayList<Room> rooms = new ArrayList<Room>();

    public Database() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/prisoner";
        String user="root";
        String pass="root";
        try {
            Class.forName(driverName);
            System.out.println("Connecting to the database: "+jdbcUrl);
            try {
                connection = DriverManager.getConnection(jdbcUrl,user,pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getPrisoner(int roomID) throws SQLException {
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM prisoner WHERE roomID = " + roomID);
        while(rs.next()) {
            prisoners.add(new Prisoner(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getInt("age"), rs.getInt("roomID"), rs.getDate("startDate"), rs.getDate("endDate")));
        }
        for (Prisoner prisoner : prisoners) {
            System.out.println(prisoner.getId() + " " + prisoner.getName() + " " + prisoner.getType() + " " + prisoner.getAge() + " " + prisoner.getRoomID() + " " + prisoner.getStartDate() + " " + prisoner.getEndDate());
        }

    }

    public void getRoom() throws SQLException {
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM room");
        while(rs.next()) {
            rooms.add(new Room(rs.getInt("id"), rs.getString("name"), rs.getInt("capacity")));
        }
        for (Room room : rooms) {
            System.out.println(room.getId() + " " + room.getName() + " " + room.getCapacity());
        }
    }

    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.getPrisoner(102);
    }
}
