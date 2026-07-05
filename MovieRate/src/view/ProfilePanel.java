package view;

import java.awt.*;
import javax.swing.*;
import model.User;

public class ProfilePanel extends JPanel {

    private final User currentUser;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel usernameValueLabel;
    private JLabel favoriteGenreValueLabel;
    private JLabel moviesReviewedValueLabel;

    public ProfilePanel(User user) {
        this.currentUser = user;

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        // ==========================
        // HEADER
        // ==========================

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900,70));
        header.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD,28));

        header.add(title);

        add(header, BorderLayout.NORTH);

        // ==========================
        // BODY
        // ==========================

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(245,245,245));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450,500));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25,30,25,30));

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN,70));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        String displayName = currentUser != null && currentUser.getFullName() != null && !currentUser.getFullName().isBlank()
                ? currentUser.getFullName()
                : (currentUser != null ? currentUser.getUsername() : "User");

        nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD,22));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String emailText = currentUser != null && currentUser.getEmail() != null && !currentUser.getEmail().isBlank()
                ? currentUser.getEmail()
                : "-";
        emailLabel = new JLabel(emailText);
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(avatar);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(30));

        usernameValueLabel = new JLabel(currentUser != null && currentUser.getUsername() != null ? currentUser.getUsername() : "-");
        favoriteGenreValueLabel = new JLabel("-");
        moviesReviewedValueLabel = new JLabel("0");

        card.add(createInfo("Username", usernameValueLabel));
        card.add(createInfo("Favorite Genre", favoriteGenreValueLabel));
        card.add(createInfo("Movies Reviewed", moviesReviewedValueLabel));

        card.add(Box.createVerticalStrut(25));

        JButton edit = new JButton("Edit Profile");
        edit.setAlignmentX(Component.CENTER_ALIGNMENT);
        edit.setBackground(new Color(30,30,47));
        edit.setForeground(Color.WHITE);
        edit.setFocusPainted(false);
        edit.addActionListener(e -> openEditProfileDialog());

        card.add(edit);

        body.add(card);

        add(body, BorderLayout.CENTER);

        refreshProfile();
    }

    private void openEditProfileDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit Profile", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Full Name");
        JTextField txtName = new JTextField(currentUser.getFullName());

        JLabel lblEmail = new JLabel("Email");
        JTextField txtEmail = new JTextField(currentUser.getEmail());

        JLabel lblPassword = new JLabel("New Password");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblConfirm = new JLabel("Confirm Password");
        JPasswordField txtConfirm = new JPasswordField();

        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(245, 197, 24));
        btnSave.setForeground(Color.BLACK);
        btnSave.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblName, gbc);
        gbc.gridy++;
        panel.add(txtName, gbc);

        gbc.gridy++;
        panel.add(lblEmail, gbc);
        gbc.gridy++;
        panel.add(txtEmail, gbc);

        gbc.gridy++;
        panel.add(lblPassword, gbc);
        gbc.gridy++;
        panel.add(txtPassword, gbc);

        gbc.gridy++;
        panel.add(lblConfirm, gbc);
        gbc.gridy++;
        panel.add(txtConfirm, gbc);

        gbc.gridy++;
        panel.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String newName = txtName.getText().trim();
            String newEmail = txtEmail.getText().trim();
            String newPassword = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirm.getPassword()).trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Full name dan email tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPassword.isBlank() && !newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Password baru dan konfirmasi harus sama.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            currentUser.setFullName(newName);
            currentUser.setEmail(newEmail);
            if (!newPassword.isBlank()) {
                currentUser.setPassword(newPassword);
            }

            if (new dao.UserDAO().updateUser(currentUser)) {
                nameLabel.setText(newName);
                emailLabel.setText(newEmail);
                JOptionPane.showMessageDialog(dialog, "Profil berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan perubahan. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void refreshProfile() {
        if (currentUser == null) return;

        nameLabel.setText(currentUser.getFullName() != null && !currentUser.getFullName().isBlank()
                ? currentUser.getFullName() : currentUser.getUsername());
        emailLabel.setText(currentUser.getEmail() != null && !currentUser.getEmail().isBlank()
                ? currentUser.getEmail() : "-");

        var reviews = model.ReviewStore.getReviewsForUser(currentUser.getUsername());
        moviesReviewedValueLabel.setText(String.valueOf(reviews.size()));
        favoriteGenreValueLabel.setText(determineFavoriteGenre(reviews));
    }

    private String determineFavoriteGenre(java.util.List<model.ReviewItem> reviews) {
        if (reviews == null || reviews.isEmpty()) return "-";

        java.util.Map<String, Integer> genreCount = new java.util.HashMap<>();
        for (model.ReviewItem review : reviews) {
            String title = review.getMovieTitle();
            if (title == null || title.isBlank()) continue;
            for (model.Movie movie : model.Movie.getCatalogue()) {
                if (title.equalsIgnoreCase(movie.getTitle())) {
                    String genre = movie.getGenre();
                    genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
                    break;
                }
            }
        }

        return genreCount.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("-");
    }

    private JPanel createInfo(String label, JLabel valueLabel){

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel left = new JLabel(label);
        left.setFont(new Font("Segoe UI", Font.BOLD,15));

        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(left, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);

        panel.setMaximumSize(new Dimension(380,35));

        return panel;

    }

}