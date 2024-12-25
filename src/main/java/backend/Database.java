package backend;

import java.sql.*;
import java.util.ArrayList;

// mysql
public class Database {
    private Connection connection = null;
    public ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();
    public ArrayList<Room> rooms = new ArrayList<Room>();

    public Database() {
        try {
            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);
            String jdbcUrl = "jdbc:mysql://localhost:3307/prisoner";
            System.out.println("Connecting to the database: "+ jdbcUrl);
            try {
                String user = "phatdev";
                String pass = "phatdev";
                connection = DriverManager.getConnection(jdbcUrl, user, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param roomID
     * @throws SQLException
     */
    public void getPrisoner(int roomID) throws SQLException {
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM prisoner WHERE roomID = " + roomID);
        while(rs.next()) {
            prisoners.add(
                    new Prisoner(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getInt("age"),
                            rs.getInt("roomID"),
                            rs.getDate("startDay"),
                            rs.getDate("endDay")
                    )
            );
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void getRoom() throws SQLException {
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM room");
        while(rs.next()) {
            rooms.add(
                    new Room(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("capacity")
                    )
            );
        }
    }

    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.getPrisoner(102);
    }
}
