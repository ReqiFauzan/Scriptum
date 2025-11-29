package scriptum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
   private static final String USERNAME = "root";  // Ganti jika berbeda
   private static final String PASSWORD = "";      // Ganti dengan password 
    
    private static Connection connection;
    
    public static Connection getConnection() {
        try {
            // Cek jika connection null atau sudah closed
            if (connection == null || connection.isClosed()) {
                // Load MySQL Driver
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    // Fallback ke driver lama jika driver baru tidak ditemukan
                    Class.forName("com.mysql.jdbc.Driver");
                }
                
                // Buat koneksi baru
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            System.err.println("Make sure mysql-connector-java is in your classpath.");
            e.printStackTrace();
            connection = null;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            System.err.println("URL: " + URL);
            System.err.println("Username: " + USERNAME);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            connection = null;
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Method untuk test koneksi
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                return !conn.isClosed();
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }
}

