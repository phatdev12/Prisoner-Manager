package backend;

import java.sql.*;
import java.util.ArrayList;

// mysql
public class Database {
    private Connection connection = null;
    public ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();

    public Database() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/prisoner?useSSL=false";
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

    public void getPrisoner() throws SQLException {
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM prisoner");
        while(rs.next()) {
            prisoners.add(new Prisoner(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getInt("age"), rs.getInt("roomID"), rs.getDate("startDate"), rs.getDate("endDate")));
        }
        for (Prisoner prisoner : prisoners) {
            System.out.println(prisoner.getId() + " " + prisoner.getName() + " " + prisoner.getType() + " " + prisoner.getAge() + " " + prisoner.getRoomID());
        }
    }

    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.getPrisoner();
    }
}
