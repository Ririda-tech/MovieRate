package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.ReviewItem;
import model.ReviewStore;
import model.User;

public class ReviewPanel extends JPanel {
    private final User currentUser;
    private final JPanel body;

    public ReviewPanel(User user) {
        this.currentUser = user;

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900,70));

        JLabel title = new JLabel("My Reviews");
        title.setFont(new Font("Segoe UI", Font.BOLD,28));

        JButton btnAdd = new JButton("Add Review");
        btnAdd.setBackground(new Color(245, 197, 24));
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> new AddMovieRatingFrame(currentUser, this::refreshReviews));

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(btnAdd);

        add(header, BorderLayout.NORTH);

        body = new JPanel();
        body.setBackground(new Color(245,245,245));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        refreshReviews();

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    public void refreshReviews() {
        body.removeAll();

        List<ReviewItem> reviews = ReviewStore.getReviewsForUser(currentUser != null ? currentUser.getUsername() : null);
        if (reviews.isEmpty()) {
            JLabel empty = new JLabel("Belum ada film yang kamu tambahkan.");
            empty.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            body.add(empty);
        } else {
            for (ReviewItem review : reviews) {
                body.add(createReviewCard(review));
                body.add(Box.createVerticalStrut(10));
            }
        }

        body.revalidate();
        body.repaint();
    }

    private JPanel createReviewCard(ReviewItem review){
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setMaximumSize(new Dimension(850, 220));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel posterLabel = createPosterLabel(review);
        posterLabel.setPreferredSize(new Dimension(110, 150));
        card.add(posterLabel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JLabel movieTitle = new JLabel(review.getMovieTitle());
        movieTitle.setFont(new Font("Segoe UI", Font.BOLD,20));

        JLabel lblRating = new JLabel("Rating: " + review.getRating() + " / 5");
        lblRating.setForeground(new Color(30, 30, 47));
        lblRating.setFont(new Font("Segoe UI", Font.BOLD,18));

        top.add(movieTitle, BorderLayout.WEST);
        top.add(lblRating, BorderLayout.EAST);

        JTextArea reviewText = new JTextArea(review.getReview());
        reviewText.setEditable(false);
        reviewText.setWrapStyleWord(true);
        reviewText.setLineWrap(true);
        reviewText.setBackground(Color.WHITE);
        reviewText.setFont(new Font("Segoe UI", Font.PLAIN,14));
        reviewText.setBorder(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        edit.setBackground(new Color(30,30,47));
        edit.setForeground(Color.WHITE);
        delete.setBackground(Color.RED);
        delete.setForeground(Color.WHITE);

        edit.addActionListener(e -> new AddMovieRatingFrame(currentUser, this::refreshReviews, review));
        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus review ini?", "Delete Review", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ReviewStore.removeReview(review.getId());
                refreshReviews();
            }
        });

        buttonPanel.add(edit);
        buttonPanel.add(delete);

        contentPanel.add(top);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(reviewText);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(buttonPanel);

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createPosterLabel(ReviewItem review) {
        String fileName = review != null ? review.getPosterFileName() : null;
        String title    = review != null ? review.getMovieTitle()    : null;

        JLabel posterLabel = MoviesPanel.buildPosterLabel(fileName, title, 110, 150);
        posterLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(248, 248, 248));

        return posterLabel;
    }

}
