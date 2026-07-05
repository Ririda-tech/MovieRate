package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import model.Movie;
import model.User;

/**
 * Home panel — shows featured movies with a working search bar.
 */
public class HomePanel extends JPanel {

    private final JTextField searchField;
    private final JPanel body;
    private final List<Movie> featured;
    private final User currentUser;
    private final Runnable onReviewSaved;

    public HomePanel(User user, Runnable onReviewSaved) {
        this.currentUser  = user;
        this.onReviewSaved = onReviewSaved;
        featured = Movie.getCatalogue();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Search movie...");

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(30, 30, 47));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(90, 35));

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        header.add(welcome, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ------------------------------------------------
        // BODY
        // ------------------------------------------------
        body = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        body.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // ------------------------------------------------
        // EVENTS
        // ------------------------------------------------
        searchBtn.addActionListener(e -> applySearch());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) applySearch();
            }
        });

        renderMovies(featured);
    }

    private void applySearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            renderMovies(featured);
            return;
        }
        List<Movie> result = featured.stream()
            .filter(m -> m.getTitle().toLowerCase().contains(keyword))
            .toList();
        renderMovies(result);
    }

    private void renderMovies(List<Movie> movies) {
        body.removeAll();

        if (movies.isEmpty()) {
            JLabel empty = new JLabel("No movies found.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            body.add(empty);
        } else {
            for (Movie m : movies) {
                body.add(createMovieCard(m));
            }
        }

        body.revalidate();
        body.repaint();
    }

    // ------------------------------------------------
    // Movie card
    // ------------------------------------------------
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(190, 300));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Poster
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(190, 170));
        posterPanel.setBackground(new Color(220, 220, 220));
        posterPanel.add(
            MoviesPanel.buildPosterLabel(movie.getPosterFileName(), movie.getTitle(), 170, 160),
            BorderLayout.CENTER
        );

        // Info
        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel(movie.getTitle());
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblGenre = new JLabel(movie.getGenre());
        lblGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGenre.setForeground(Color.GRAY);

        JLabel lblRating = new JLabel("Rating: " + movie.getRating() + " / 5");
        lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRating.setForeground(new Color(30, 30, 47));
        lblRating.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton detailBtn = new JButton("Detail");
        detailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailBtn.setBackground(new Color(30, 30, 47));
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFocusPainted(false);
        detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailBtn.addActionListener(e -> new MovieDetailFrame(movie, currentUser, onReviewSaved));

        info.add(lblTitle);
        info.add(Box.createVerticalStrut(5));
        info.add(lblGenre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblRating);
        info.add(Box.createVerticalStrut(10));
        info.add(detailBtn);

        card.add(posterPanel, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
    }
}
