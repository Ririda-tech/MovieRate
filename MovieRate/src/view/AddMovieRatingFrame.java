package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import model.ReviewItem;
import model.ReviewStore;
import model.User;

public class AddMovieRatingFrame extends JFrame {

    private String posterFileName = "";
    private final User currentUser;
    private final Runnable onSaved;
    private final ReviewItem existingReview;

    /** Add new review (no prefill) */
    public AddMovieRatingFrame(User user, Runnable onSaved) {
        this(user, onSaved, null, null);
    }

    /** Edit existing review */
    public AddMovieRatingFrame(User user, Runnable onSaved, ReviewItem existingReview) {
        this(user, onSaved, existingReview, null);
    }

    /** Rate from MovieDetailFrame — title pre-filled, always insert new */
    public AddMovieRatingFrame(User user, Runnable onSaved, String prefilledTitle) {
        this(user, onSaved, null, prefilledTitle);
    }

    public AddMovieRatingFrame(User user, Runnable onSaved, ReviewItem existingReview, String prefilledTitle) {
        this.currentUser = user;
        this.onSaved = onSaved;
        this.existingReview = existingReview;

        if (existingReview != null && existingReview.getPosterFileName() != null) {
            this.posterFileName = existingReview.getPosterFileName();
        }

        setTitle(existingReview != null ? "Edit Review" : "Add Movie & Rating");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLbl = new JLabel(existingReview != null ? "Edit Review" : "Add Movie & Rating");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Movie title field
        JLabel lblMovieName = new JLabel("Movie Title:");
        JTextField txtMovieName = new JTextField();
        if (existingReview != null) {
            txtMovieName.setText(existingReview.getMovieTitle());
        } else if (prefilledTitle != null && !prefilledTitle.isBlank()) {
            txtMovieName.setText(prefilledTitle);
            if (posterFileName.isBlank()) {
                posterFileName = ReviewItem.resolvePosterFileNameFromTitle(prefilledTitle);
            }
        }

        // Rating combo
        JLabel lblRating = new JLabel("Rating (1-5):");
        JComboBox<String> comboRating = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        if (existingReview != null) {
            comboRating.setSelectedItem(existingReview.getRating());
        }

        // Review text area
        JLabel lblReview = new JLabel("Review:");
        JTextArea txtReview = new JTextArea(4, 20);
        txtReview.setLineWrap(true);
        txtReview.setWrapStyleWord(true);
        if (existingReview != null) {
            txtReview.setText(existingReview.getReview());
        }
        JScrollPane scrollReview = new JScrollPane(txtReview);

        // Poster picker
        JLabel lblPoster = new JLabel("Poster:");
        JButton btnPoster = new JButton("Select Poster Image");
        JLabel lblSelectedFile = new JLabel(posterFileName.isEmpty() ? "No image selected" : posterFileName);
        lblSelectedFile.setForeground(Color.GRAY);

        JButton btnSubmit = new JButton(existingReview != null ? "Update Review" : "Submit Rating");
        btnSubmit.setBackground(new Color(245, 197, 24));
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ------------------------------------------------
        // Poster picker action
        // ------------------------------------------------
        btnPoster.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selected = fc.getSelectedFile();
                lblSelectedFile.setText(selected.getName());
                try {
                    for (File dest : new File[]{new File("assets/poster"), new File("MovieRate/assets/poster")}) {
                        dest.mkdirs();
                        Files.copy(selected.toPath(), new File(dest, selected.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    posterFileName = selected.getName();
                    JOptionPane.showMessageDialog(this, "Poster uploaded!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to upload poster.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ------------------------------------------------
        // Submit action
        // ------------------------------------------------
        btnSubmit.addActionListener(e -> {
            String title  = txtMovieName.getText().trim();
            String review = txtReview.getText().trim();
            String rating = (String) comboRating.getSelectedItem();

            if (title.isEmpty() || review.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            // Resolve poster — optional, boleh kosong
            if (posterFileName.isBlank()) {
                posterFileName = ReviewItem.resolvePosterFileNameFromTitle(title);
            }

            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("[AddMovieRatingFrame] Saving: user=" + currentUser.getUsername() + " movie=" + title);

            // existingReview != null dan id > 0 → update; semua case lain → insert baru
            boolean isEdit = (existingReview != null && existingReview.getId() > 0);

            if (isEdit) {
                existingReview.setMovieTitle(title);
                existingReview.setRating(rating);
                existingReview.setReview(review);
                existingReview.setPosterFileName(posterFileName);
                ReviewStore.updateReview(existingReview);
            } else {
                ReviewItem newItem = new ReviewItem();
                newItem.setId(0);
                newItem.setUsername(currentUser.getUsername());
                newItem.setMovieTitle(title);
                newItem.setRating(rating);
                newItem.setReview(review);
                newItem.setPosterFileName(posterFileName);
                ReviewStore.addReview(newItem);
                System.out.println("[AddMovieRatingFrame] Inserted id=" + newItem.getId());
            }

            if (onSaved != null) {
                onSaved.run();
            }

            JOptionPane.showMessageDialog(this, isEdit ? "Review updated!" : "Review saved!");
            dispose();
        });

        // ------------------------------------------------
        // Layout
        // ------------------------------------------------
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLbl, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        panel.add(lblMovieName, gbc);
        gbc.gridx = 1;
        panel.add(txtMovieName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(lblRating, gbc);
        gbc.gridx = 1;
        panel.add(comboRating, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(lblReview, gbc);
        gbc.gridx = 1;
        panel.add(scrollReview, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(lblPoster, gbc);
        gbc.gridx = 1;
        panel.add(btnPoster, gbc);

        gbc.gridx = 1; gbc.gridy++;
        panel.add(lblSelectedFile, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(btnSubmit, gbc);

        add(panel);
        setVisible(true);
    }
}
