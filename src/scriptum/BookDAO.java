package scriptum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    
    public List<Buku> getAllBooks() {
        List<Buku> books = new ArrayList<>();
        String sql = "SELECT * FROM buku ORDER BY judul";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return books;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }
    
    public Buku getBookById(int id) {
        String sql = "SELECT * FROM buku WHERE buku_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return null;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractBookFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addBook(Buku buku) {
        String sql = "INSERT INTO buku (judul, penulis, penerbit, thn_terbit, ktegori, stok) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPenulis());
            stmt.setString(3, buku.getPenerbit());
            stmt.setInt(4, buku.getThnTerbit());
            stmt.setString(5, buku.getKtegori());
            stmt.setInt(6, buku.getStok());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateBook(Buku buku) {
        String sql = "UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, thn_terbit = ?, ktegori = ?, stok = ? WHERE buku_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPenulis());
            stmt.setString(3, buku.getPenerbit());
            stmt.setInt(4, buku.getThnTerbit());
            stmt.setString(5, buku.getKtegori());
            stmt.setInt(6, buku.getStok());
            stmt.setInt(7, buku.getBukuId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM buku WHERE buku_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Buku> searchBooks(String keyword) {
        List<Buku> books = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE judul LIKE ? OR penulis LIKE ? OR ktegori LIKE ?";
        
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return books;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(extractBookFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }
    
    private Buku extractBookFromResultSet(ResultSet rs) throws SQLException {
        Buku buku = new Buku();
        buku.setBukuId(rs.getInt("buku_id"));
        buku.setJudul(rs.getString("judul"));
        buku.setPenulis(rs.getString("penulis"));
        buku.setPenerbit(rs.getString("penerbit"));
        buku.setThnTerbit(rs.getInt("thn_terbit"));
        buku.setKtegori(rs.getString("ktegori"));
        buku.setStok(rs.getInt("stok"));
        
        return buku;
    }
}

