package view;

import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        // ==========================
        // FRAME
        // ==========================
        setTitle("MovieRate");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ==========================
        // MAIN PANEL
        // ==========================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 47));

        // ==========================
        // HEADER
        // ==========================
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 30, 47));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel title = new JLabel("MOVIERATE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel subtitle = new JLabel("Movie Rating & Review System");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        mainPanel.add(header, BorderLayout.NORTH);

        // ==========================
        // LOGIN CARD
        // ==========================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(450, 420));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JTextField txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(300, 35));

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(300, 35));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(245, 197, 24));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(300, 40));

        JLabel register = new JLabel("Don't have an account? Register");
        register.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        register.setForeground(Color.GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(loginTitle, gbc);

        gbc.gridy++;
        centerPanel.add(lblUsername, gbc);

        gbc.gridy++;
        centerPanel.add(txtUsername, gbc);

        gbc.gridy++;
        centerPanel.add(lblPassword, gbc);

        gbc.gridy++;
        centerPanel.add(txtPassword, gbc);

        gbc.gridy++;
        centerPanel.add(btnLogin, gbc);

        gbc.gridy++;
        centerPanel.add(register, gbc);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.UserDAO userDAO = new dao.UserDAO();
            model.User loggedInUser = userDAO.loginUser(username, password);

            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat datang " + loggedInUser.getUsername());
                this.dispose(); 
                new MainFrame(loggedInUser);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        register.setCursor(new Cursor(Cursor.HAND_CURSOR));
        register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new RegisterFrame();
            }
        });

        // ==========================
        // WRAPPER
        // ==========================
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(30, 30, 47));
        wrapper.add(centerPanel);

        mainPanel.add(wrapper, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }
}
