package model;

import dao.ReviewDAO;
import java.util.List;

/**
 * Facade untuk semua operasi review.
 * Semua data disimpan dan dibaca dari MySQL via ReviewDAO.
 */
public class ReviewStore {

    private static final ReviewDAO dao = new ReviewDAO();

    private ReviewStore() {}

    public static void addReview(ReviewItem review) {
        if (review != null) {
            dao.addReview(review);
        }
    }

    public static boolean removeReview(int id) {
        return dao.deleteReview(id);
    }

    public static boolean updateReview(ReviewItem updatedReview) {
        if (updatedReview == null) return false;
        return dao.updateReview(updatedReview);
    }

    public static List<ReviewItem> getAllReviews() {
        return dao.getAllReviews();
    }

    public static void clear() {
        dao.deleteAllReviews();
    }

    public static List<ReviewItem> getReviewsForUser(String username) {
        if (username == null) return List.of();
        return dao.getReviewsByUser(username);
    }
}
