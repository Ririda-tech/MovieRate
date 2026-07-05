package helper;
import config.DatabaseConfig;
import java.sql.*;
public class CheckReviews {
    public static void main(String[] a) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("=== reviews columns ===");
            ResultSet cols = meta.getColumns(null, null, "reviews", "%");
            while (cols.next())
                System.out.println("  " + cols.getString("COLUMN_NAME") + " [" + cols.getString("TYPE_NAME") + "]");
        }
    }
}