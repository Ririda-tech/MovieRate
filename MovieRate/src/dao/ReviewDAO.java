package dao;

import config.DatabaseConfig;
import model.ReviewItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all review CRUD operations against the MySQL database.
 */
public class ReviewDAO {

    /**
     * Inserts a new review. Sets the generated ID back on the ReviewItem.
     */
    public boolean addReview(ReviewItem review) {
        String sql = "INSERT INTO movierate_reviews (username, movie_title, rating, review, poster_file_name) VALUES (?, ?, ?, ?, ?)";
        System.out.println("[ReviewDAO] addReview called: user=" + review.getUsername() + " movie=" + review.getMovieTitle());
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, review.getUsername());
            ps.setString(2, review.getMovieTitle());
            ps.setString(3, review.getRating());
            ps.setString(4, review.getReview());
            ps.setString(5, review.getPosterFileName());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    review.setId(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing review by its ID.
     */
    public boolean updateReview(ReviewItem review) {
        String sql = "UPDATE movierate_reviews SET movie_title=?, rating=?, review=?, poster_file_name=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, review.getMovieTitle());
            ps.setString(2, review.getRating());
            ps.setString(3, review.getReview());
            ps.setString(4, review.getPosterFileName());
            ps.setInt(5, review.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a review by its ID.
     */
    public boolean deleteReview(int id) {
        String sql = "DELETE FROM movierate_reviews WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all reviews belonging to a specific user.
     */
    public List<ReviewItem> getReviewsByUser(String username) {
        List<ReviewItem> list = new ArrayList<>();
        String sql = "SELECT id, username, movie_title, rating, review, poster_file_name FROM movierate_reviews WHERE username=? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReviewItem item = new ReviewItem();
                    item.setId(rs.getInt("id"));
                    item.setUsername(rs.getString("username"));
                    item.setMovieTitle(rs.getString("movie_title"));
                    item.setRating(rs.getString("rating"));
                    item.setReview(rs.getString("review"));
                    item.setPosterFileName(rs.getString("poster_file_name"));
                    list.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Returns all reviews in the database (for admin/home display).
     */
    public List<ReviewItem> getAllReviews() {
        List<ReviewItem> list = new ArrayList<>();
        String sql = "SELECT id, username, movie_title, rating, review, poster_file_name FROM movierate_reviews ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReviewItem item = new ReviewItem();
                item.setId(rs.getInt("id"));
                item.setUsername(rs.getString("username"));
                item.setMovieTitle(rs.getString("movie_title"));
                item.setRating(rs.getString("rating"));
                item.setReview(rs.getString("review"));
                item.setPosterFileName(rs.getString("poster_file_name"));
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Deletes all reviews from the database.
     */
    public boolean deleteAllReviews() {
        String sql = "DELETE FROM movierate_reviews";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
