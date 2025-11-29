package scriptum;

import java.sql.*;

public class UserDAO {
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null! Check your database configuration.");
            return null;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserById(int userId) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return null;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (username, password, email, nama, tgl_dftr, status_lgn, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getNama());
            stmt.setDate(5, java.sql.Date.valueOf(user.getTglDftr()));
            stmt.setBoolean(6, user.isStatusLgn());
            stmt.setString(7, user.getRole());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateUserStatus(int userId, boolean status) {
        String sql = "UPDATE user SET status_lgn = ? WHERE user_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setNama(rs.getString("nama"));
        
        Date tglDftr = rs.getDate("tgl_dftr");
        if (tglDftr != null) {
            user.setTglDftr(tglDftr.toLocalDate());
        }
        
        user.setStatusLgn(rs.getBoolean("status_lgn"));
        user.setRole(rs.getString("role"));
        
        return user;
    }
}

