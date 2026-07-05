package view;

import java.awt.*;
import javax.swing.*;
import model.User;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MainFrame(User user) {
        setTitle("MovieRate");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ------------------------------------------------
        // SIDEBAR
        // ------------------------------------------------
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBackground(new Color(30, 30, 47));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(Box.createVerticalStrut(25));

        JLabel logo = new JLabel("MOVIERATE");
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        JButton homeBtn    = createMenuButton("Home");
        JButton movieBtn   = createMenuButton("Movies");
        JButton reviewBtn  = createMenuButton("My Reviews");
        JButton profileBtn = createMenuButton("Profile");
        JButton logoutBtn  = createMenuButton("Logout");

        sidebar.add(homeBtn);
        sidebar.add(movieBtn);
        sidebar.add(reviewBtn);
        sidebar.add(profileBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        // ------------------------------------------------
        // CONTENT (CardLayout)
        // ------------------------------------------------
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        ReviewPanel  reviewPanel  = new ReviewPanel(user);
        HomePanel    homePanel    = new HomePanel(user, reviewPanel::refreshReviews);
        MoviesPanel  moviesPanel  = new MoviesPanel(user, reviewPanel::refreshReviews);
        ProfilePanel profilePanel = new ProfilePanel(user);

        contentPanel.add(homePanel,    "HOME");
        contentPanel.add(moviesPanel,  "MOVIES");
        contentPanel.add(reviewPanel,  "REVIEW");
        contentPanel.add(profilePanel, "PROFILE");

        // ------------------------------------------------
        // BUTTON EVENTS
        // ------------------------------------------------
        homeBtn.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
        movieBtn.addActionListener(e -> cardLayout.show(contentPanel, "MOVIES"));
        reviewBtn.addActionListener(e -> {
            reviewPanel.refreshReviews(); // selalu fresh saat dibuka
            cardLayout.show(contentPanel, "REVIEW");
        });
        profileBtn.addActionListener(e -> cardLayout.show(contentPanel, "PROFILE"));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        // ------------------------------------------------
        // ASSEMBLE
        // ------------------------------------------------
        mainPanel.add(sidebar,       BorderLayout.WEST);
        mainPanel.add(contentPanel,  BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setMinimumSize(new Dimension(200, 45));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setBackground(new Color(30, 30, 47));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
