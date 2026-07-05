package dao;

import config.DatabaseConfig;
import model.User;

import java.sql.*;

/**
 * Handles all user-related database operations (register & login).
 */
public class UserDAO {

    /**
     * Registers a new user. Returns false if username already taken.
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername().trim());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Username sudah ada
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates login credentials. Returns User if valid, null otherwise.
     */
    public User loginUser(String username, String password) {
        String sql = "SELECT id, full_name, username, email, password FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the user's profile information.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
