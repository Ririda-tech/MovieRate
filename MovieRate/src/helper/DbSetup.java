package helper;

import config.DatabaseConfig;
import java.sql.*;

/**
 * One-time helper: checks existing users table structure
 * and creates the reviews table if it doesn't exist yet.
 * Run once: java -cp "out;lib/mysql-connector-j-8.3.0.jar" helper.DbSetup
 */
public class DbSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Koneksi OK ke: " + conn.getMetaData().getURL());

            // Show existing tables
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("\n=== Tables in movie_rate_db ===");
            try (ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    System.out.println("  - " + tables.getString("TABLE_NAME"));
                }
            }

            // Show users table columns
            System.out.println("\n=== Columns in users ===");
            try (ResultSet cols = meta.getColumns(null, null, "users", "%")) {
                while (cols.next()) {
                    System.out.println("  " + cols.getString("COLUMN_NAME")
                        + " [" + cols.getString("TYPE_NAME") + "]");
                }
            }

            // Create reviews table
            String createReviews = """
                CREATE TABLE IF NOT EXISTS reviews (
                    id               INT AUTO_INCREMENT PRIMARY KEY,
                    username         VARCHAR(50)  NOT NULL,
                    movie_title      VARCHAR(200) NOT NULL,
                    rating           VARCHAR(5)   NOT NULL,
                    review           TEXT         NOT NULL,
                    poster_file_name VARCHAR(255),
                    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

            try (Statement st = conn.createStatement()) {
                st.execute(createReviews);
                System.out.println("\nTable 'reviews' siap.");
            }

        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
