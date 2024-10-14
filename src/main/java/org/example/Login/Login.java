package org.example.Login;

import java.sql.*;
import java.util.Base64;

public class Login {
    private Connection connection;

    public Login(Connection connection) {
        this.connection = connection;
    }

    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT password, level FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                int level = rs.getInt("level");

                // So sánh hash password
                return validatePassword(password, storedHash, level);
            }
        }
        return false;
    }

    private boolean validatePassword(String password, String storedHash, int level) {
        // Mã hóa password và so sánh với hash đã lưu
        return password.equals(new String(Base64.getDecoder().decode(storedHash))); // Chỉnh sửa theo phương thức mã hóa của bạn
    }
}
