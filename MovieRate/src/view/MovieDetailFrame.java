package view;

import java.awt.*;
import javax.swing.*;
import model.Movie;
import model.ReviewItem;
import model.User;

/**
 * Shows the full detail of a single movie — poster, synopsis, director, etc.
 * Accepts a {@link Movie} object so all data comes from the catalogue model.
 */
public class MovieDetailFrame extends JFrame {

    private User currentUser;
    private Runnable onReviewSaved;

    /** Convenience no-arg constructor for IDE preview / testing. */
    public MovieDetailFrame() {
        this(new Movie("Movie Detail", "Genre", 0, "Unknown", "Unknown", "Synopsis not available.", null), null);
    }

    /**
     * Legacy constructor — looks up movie in catalogue by title.
     */
    public MovieDetailFrame(String movieTitleValue, String genreValue, double ratingValue) {
        this(Movie.getCatalogue().stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(movieTitleValue))
                .findFirst()
                .orElse(new Movie(movieTitleValue, genreValue, ratingValue, "Unknown", "Unknown", "Synopsis not available.", null)),
             null);
    }

    public MovieDetailFrame(Movie movie) {
        this(movie, null, null);
    }

    public MovieDetailFrame(Movie movie, User user) {
        this(movie, user, null);
    }

    public MovieDetailFrame(Movie movie, User user, Runnable onReviewSaved) {
        this.currentUser    = user;
        this.onReviewSaved  = onReviewSaved;
        setTitle(movie.getTitle() + " - Movie Detail");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        Color navy  = new Color(30, 30, 47);
        Color yellow = new Color(245, 197, 24);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navy);
        header.setPreferredSize(new Dimension(1000, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JButton backButton = new JButton("← Back");
        backButton.setBackground(navy);
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());

        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        header.add(backButton, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);

        // ------------------------------------------------
        // CONTENT
        // ------------------------------------------------
        JPanel content = new JPanel(new BorderLayout(30, 30));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Poster panel
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(260, 420));
        posterPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        posterPanel.setBackground(new Color(248, 248, 248));

        JLabel posterLabel = MoviesPanel.buildPosterLabel(movie.getPosterFileName(), movie.getTitle(), 258, 418);
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(248, 248, 248));
        posterPanel.add(posterLabel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(movie.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel lblGenre    = new JLabel("Genre    : " + movie.getGenre());
        JLabel lblDirector = new JLabel("Director : " + movie.getDirector());
        JLabel lblDuration = new JLabel("Duration : " + movie.getDuration());
        for (JLabel lbl : new JLabel[]{lblGenre, lblDirector, lblDuration}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        JLabel lblRating = new JLabel("Rating: " + movie.getRating() + " / 5");
        lblRating.setForeground(Color.BLACK);
        lblRating.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel synopsisLabel = new JLabel("Synopsis");
        synopsisLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextArea synopsis = new JTextArea(movie.getSynopsis());
        synopsis.setWrapStyleWord(true);
        synopsis.setLineWrap(true);
        synopsis.setEditable(false);
        synopsis.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        synopsis.setBackground(new Color(245, 245, 245));
        synopsis.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane synopsisScroll = new JScrollPane(synopsis);
        synopsisScroll.setPreferredSize(new Dimension(500, 180));
        synopsisScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JButton rateButton = new JButton("Rate Movie");
        rateButton.setBackground(yellow);
        rateButton.setForeground(Color.BLACK);
        rateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rateButton.setFocusPainted(false);
        rateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateButton.setPreferredSize(new Dimension(180, 45));
        rateButton.setMaximumSize(new Dimension(180, 45));
        rateButton.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this,
                    "Silakan login terlebih dahulu untuk memberi rating.",
                    "Login Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Pass null sebagai existingReview supaya AddMovieRatingFrame tahu ini insert baru
            // Judul film di-prefill lewat constructor dengan title
            new AddMovieRatingFrame(currentUser, onReviewSaved, movie.getTitle());
        });

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(lblGenre);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(lblDirector);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(lblDuration);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(lblRating);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(synopsisLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(synopsisScroll);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(rateButton);

        content.add(posterPanel, BorderLayout.WEST);
        content.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}
