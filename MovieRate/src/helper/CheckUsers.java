package helper;
import config.DatabaseConfig;
import java.sql.*;
public class CheckUsers {
    public static void main(String[] a) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Cek semua users tables
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("=== users columns ===");
            ResultSet cols = meta.getColumns(null, null, "users", "%");
            while (cols.next())
                System.out.println("  " + cols.getString("COLUMN_NAME") + " [" + cols.getString("TYPE_NAME") + "] TABLE_CAT=" + cols.getString("TABLE_CAT"));
            
            // Buat movierate_reviews kalau belum ada
            String sql = """
                CREATE TABLE IF NOT EXISTS movierate_reviews (
                    id               INT AUTO_INCREMENT PRIMARY KEY,
                    username         VARCHAR(100) NOT NULL,
                    movie_title      VARCHAR(200) NOT NULL,
                    rating           VARCHAR(5)   NOT NULL,
                    review           TEXT         NOT NULL,
                    poster_file_name VARCHAR(255),
                    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            conn.createStatement().execute(sql);
            System.out.println("Table movierate_reviews siap!");
        }
    }
}