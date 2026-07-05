package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import model.Movie;
import model.ReviewItem;
import model.User;

/**
 * Panel that shows the full movie catalogue with working search + genre filter.
 */
public class MoviesPanel extends JPanel {

    private final JPanel body;
    private final JTextField searchField;
    private final JComboBox<String> genreBox;
    private final List<Movie> catalogue;
    private final User currentUser;
    private final Runnable onReviewSaved;

    public MoviesPanel(User user, Runnable onReviewSaved) {
        this.currentUser   = user;
        this.onReviewSaved = onReviewSaved;
        catalogue = Movie.getCatalogue();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 80));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Movies");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 35));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Search movie...");

        genreBox = new JComboBox<>(new String[]{"All", "Action", "Adventure", "Drama", "Sci-Fi"});
        genreBox.setPreferredSize(new Dimension(120, 35));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(30, 30, 47));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setPreferredSize(new Dimension(90, 35));

        rightPanel.add(searchField);
        rightPanel.add(genreBox);
        rightPanel.add(searchBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ------------------------------------------------
        // BODY
        // ------------------------------------------------
        body = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        body.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        // ------------------------------------------------
        // EVENTS
        // ------------------------------------------------
        searchBtn.addActionListener(e -> applyFilter());

        // Also filter on Enter key inside search field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) applyFilter();
            }
        });

        genreBox.addActionListener(e -> applyFilter());

        // Initial render
        renderMovies(catalogue);
    }

    // ------------------------------------------------
    // Filter logic
    // ------------------------------------------------
    private void applyFilter() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedGenre = (String) genreBox.getSelectedItem();

        List<Movie> filtered = catalogue.stream()
            .filter(m -> {
                boolean matchGenre = "All".equals(selectedGenre) || m.getGenre().equalsIgnoreCase(selectedGenre);
                boolean matchSearch = keyword.isEmpty() || m.getTitle().toLowerCase().contains(keyword);
                return matchGenre && matchSearch;
            })
            .toList();

        renderMovies(filtered);
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
        card.setPreferredSize(new Dimension(180, 290));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Poster
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(180, 150));
        posterPanel.setBackground(new Color(220, 220, 220));

        posterPanel.add(buildPosterLabel(movie.getPosterFileName(), movie.getTitle(), 160, 140), BorderLayout.CENTER);

        // Info
        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(movie.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGenre = new JLabel(movie.getGenre());
        lblGenre.setForeground(Color.GRAY);
        lblGenre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRating = new JLabel("Rating: " + movie.getRating() + " / 5");
        lblRating.setForeground(new Color(30, 30, 47));
        lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton detail = new JButton("Detail");
        detail.setBackground(new Color(30, 30, 47));
        detail.setForeground(Color.WHITE);
        detail.setFocusPainted(false);
        detail.setAlignmentX(Component.CENTER_ALIGNMENT);
        detail.addActionListener(e -> new MovieDetailFrame(movie, currentUser, onReviewSaved));

        info.add(Box.createVerticalStrut(10));
        info.add(lblTitle);
        info.add(Box.createVerticalStrut(5));
        info.add(lblGenre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblRating);
        info.add(Box.createVerticalStrut(10));
        info.add(detail);

        card.add(posterPanel, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    // ------------------------------------------------
    // Shared poster label builder
    // ------------------------------------------------
    static JLabel buildPosterLabel(String posterFileName, String movieTitle, int w, int h) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        String fileName = (posterFileName != null && !posterFileName.isBlank())
            ? posterFileName
            : ReviewItem.resolvePosterFileNameFromTitle(movieTitle);

        File posterFile = null;
        if (fileName != null && !fileName.isBlank()) {
            for (File dir : ReviewItem.getPosterBaseDirectories()) {
                File candidate = new File(dir, fileName);
                System.out.println("[poster] trying: " + candidate.getAbsolutePath() + " -> " + candidate.exists());
                if (candidate.exists()) { posterFile = candidate; break; }
            }
        }

        // If posterFileName was provided but the file wasn't found, retry using the movie title.
        if (posterFile == null && (posterFileName != null && !posterFileName.isBlank()) && movieTitle != null) {
            String resolved = ReviewItem.resolvePosterFileNameFromTitle(movieTitle);
            if (resolved != null && !resolved.isBlank() && !resolved.equals(fileName)) {
                for (File dir : ReviewItem.getPosterBaseDirectories()) {
                    File candidate = new File(dir, resolved);
                    System.out.println("[poster] fallback trying: " + candidate.getAbsolutePath() + " -> " + candidate.exists());
                    if (candidate.exists()) { posterFile = candidate; break; }
                }
            }
        }

        if (posterFile != null) {
            System.out.println("[poster] FOUND: " + posterFile.getAbsolutePath());
            ImageIcon icon = new ImageIcon(posterFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("[poster] NOT FOUND for: " + fileName + " / " + movieTitle);
            label.setText("No Poster");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(Color.GRAY);
        }

        return label;
    }
}
