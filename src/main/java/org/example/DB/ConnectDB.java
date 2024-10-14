//package org.example.DB;
//
//
//import org.example.Model.SensorData;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class ConnectDB {
//    private static final String URL = "jdbc:mysql://localhost:3306/sensor_db";
//    private static final String USER = "root";
//    private static final String PASSWORD = "";
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//
//    public static void saveSensorData(SensorData data) {
//        String query = "INSERT INTO sensor_data (temperature, humidity, fanSpeed, ledState, buzzerState) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setFloat(1, data.getTemperature());
//            stmt.setFloat(2, data.getHumidity());
//            stmt.setInt(3, data.getFanSpeed());
//            stmt.setString(4, data.getLedState());
//            stmt.setString(5, data.getBuzzerState());
//            stmt.executeUpdate();
//
//            System.out.println("Data saved to database");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
