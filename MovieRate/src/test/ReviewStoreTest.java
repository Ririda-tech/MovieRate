import model.ReviewItem;
import model.ReviewStore;

public class ReviewStoreTest {
    public static void main(String[] args) {
        ReviewStore.clear();

        ReviewItem review = new ReviewItem();
        review.setUsername("riri");
        review.setMovieTitle("Interstellar");
        review.setRating("5");
        review.setReview("Great sci-fi movie");
        review.setPosterFileName("");

        if (!"interstellar.jpg".equals(review.resolvePosterFileName())) {
            throw new IllegalStateException("Expected poster to be resolved from movie title");
        }

        review.setPosterFileName("interstellar.jpg");

        ReviewStore.addReview(review);

        if (ReviewStore.getReviewsForUser("riri").size() != 1) {
            throw new IllegalStateException("Expected one review for riri");
        }

        if (!ReviewStore.removeReview(review.getId())) {
            throw new IllegalStateException("Expected review to be removed");
        }

        if (!ReviewStore.getReviewsForUser("riri").isEmpty()) {
            throw new IllegalStateException("Expected no reviews after delete");
        }

        System.out.println("ReviewStoreTest passed");
    }
}
