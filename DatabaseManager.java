import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager.java
 * ─────────────────────────────────────────────────────────────────
 * Handles ALL database operations:
 *   - Opening / closing the SQLite connection
 *   - Creating the "shapes" table if it doesn't exist
 *   - Saving shape records
 *   - Fetching all saved records
 *   - Deleting a record
 *
 * SQLite is a lightweight, file-based database — perfect for
 * beginner projects because it needs no separate server.
 * The database file (shapes.db) is created automatically in the
 * project folder when the program first runs.
 * ─────────────────────────────────────────────────────────────────
 */
public class DatabaseManager {

    // JDBC URL tells Java to use the SQLite driver and the file "shapes.db"
    private static final String DB_URL = "jdbc:sqlite:shapes.db";

    // The Connection object represents our open link to the database
    private Connection connection;

    /**
     * Constructor – connects to the database and creates the table.
     * Called once when the application starts.
     */
    public DatabaseManager() {
        connect();
        createTable();
    }

    /**
     * connect() – Opens a connection to the SQLite database file.
     * DriverManager.getConnection() is the standard JDBC method.
     */
    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("[DB] Connected to shapes.db successfully.");
        } catch (SQLException e) {
            System.err.println("[DB] Connection error: " + e.getMessage());
        }
    }

    /**
     * createTable() – Creates the "shapes" table if it doesn't
     * already exist (IF NOT EXISTS prevents re-creation on restart).
     *
     * Table columns:
     *   id          – Auto-incrementing primary key
     *   shape_type  – Name of shape ("Circle", "Square", etc.)
     *   dimensions  – Human-readable dimension string
     *   area        – Calculated area  (stored as REAL = decimal)
     *   perimeter   – Calculated perimeter
     *   created_at  – Timestamp, filled automatically by SQLite
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shapes ("
                   + "  id          INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "  shape_type  TEXT    NOT NULL,"
                   + "  dimensions  TEXT    NOT NULL,"
                   + "  area        REAL    NOT NULL,"
                   + "  perimeter   REAL    NOT NULL,"
                   + "  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP"
                   + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("[DB] Table 'shapes' ready.");
        } catch (SQLException e) {
            System.err.println("[DB] Table creation error: " + e.getMessage());
        }
    }

    /**
     * saveShape() – Inserts one row into the shapes table.
     *
     * We use PreparedStatement (with ? placeholders) instead of
     * building the SQL string manually.  This prevents SQL Injection
     * and handles special characters automatically.
     *
     * @param shapeType  e.g. "Circle"
     * @param dimensions e.g. "radius=5.0"
     * @param area       the calculated area
     * @param perimeter  the calculated perimeter
     */
    public boolean saveShape(String shapeType, String dimensions,
                             double area, double perimeter) {
        String sql = "INSERT INTO shapes (shape_type, dimensions, area, perimeter) "
                   + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shapeType);
            pstmt.setString(2, dimensions);
            pstmt.setDouble(3, area);
            pstmt.setDouble(4, perimeter);
            pstmt.executeUpdate();  // Actually runs the INSERT
            System.out.println("[DB] Shape saved: " + shapeType);
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] Save error: " + e.getMessage());
            return false;
        }
    }

    /**
     * getAllShapes() – Fetches every saved shape from the database,
     * ordered newest-first, and returns them as a list of String
     * arrays (each array = one table row).
     *
     * Columns returned: [id, shape_type, dimensions, area, perimeter, created_at]
     */
    public List<String[]> getAllShapes() {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, shape_type, dimensions, area, perimeter, created_at "
                   + "FROM shapes ORDER BY id DESC";
        try (Statement   stmt = connection.createStatement();
             ResultSet   rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // rs.getString(column) / rs.getDouble(column) reads each cell
                String[] row = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("shape_type"),
                    rs.getString("dimensions"),
                    String.format("%.4f", rs.getDouble("area")),
                    String.format("%.4f", rs.getDouble("perimeter")),
                    rs.getString("created_at")
                };
                rows.add(row);
            }
        } catch (SQLException e) {
            System.err.println("[DB] Fetch error: " + e.getMessage());
        }
        return rows;
    }

    /**
     * deleteShape() – Removes a shape record by its ID.
     * @param id The primary key of the row to delete.
     */
    public boolean deleteShape(int id) {
        String sql = "DELETE FROM shapes WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            System.out.println("[DB] Deleted shape id=" + id);
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("[DB] Delete error: " + e.getMessage());
            return false;
        }
    }

    /**
     * close() – Should be called when the application exits to
     * release the database connection cleanly.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Close error: " + e.getMessage());
        }
    }
}
