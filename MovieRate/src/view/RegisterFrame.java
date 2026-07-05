package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import dao.UserDAO;
import model.User;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {

        // ==========================
        // FRAME
        // ==========================
        setTitle("MovieRate - Register");
        setSize(900, 650);
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
        header.setBackground(new Color(30,30,47));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));

        JLabel title = new JLabel("MOVIERATE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));

        JLabel subtitle = new JLabel("Movie Rating & Review System");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        mainPanel.add(header, BorderLayout.NORTH);

        // ==========================
        // REGISTER CARD
        // ==========================
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(480,500));
        card.setBorder(BorderFactory.createEmptyBorder(25,35,25,35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel registerTitle = new JLabel("Create Account");
        registerTitle.setFont(new Font("Segoe UI", Font.BOLD,28));

        JLabel lblName = new JLabel("Full Name");
        JTextField txtName = new JTextField();

        JLabel lblUsername = new JLabel("Username");
        JTextField txtUsername = new JTextField();

        JLabel lblEmail = new JLabel("Email");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Password");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblConfirm = new JLabel("Confirm Password");
        JPasswordField txtConfirm = new JPasswordField();

        txtName.setPreferredSize(new Dimension(300,35));
        txtUsername.setPreferredSize(new Dimension(300,35));
        txtEmail.setPreferredSize(new Dimension(300,35));
        txtPassword.setPreferredSize(new Dimension(300,35));
        txtConfirm.setPreferredSize(new Dimension(300,35));

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setBackground(new Color(245,197,24));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Segoe UI",Font.BOLD,15));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setPreferredSize(new Dimension(300,40));

        JLabel login = new JLabel("Already have an account? Login");
        login.setForeground(Color.GRAY);

        gbc.gridx=0;
        gbc.gridy=0;
        card.add(registerTitle,gbc);

        gbc.gridy++;
        card.add(lblName,gbc);

        gbc.gridy++;
        card.add(txtName,gbc);

        gbc.gridy++;
        card.add(lblUsername,gbc);

        gbc.gridy++;
        card.add(txtUsername,gbc);

        gbc.gridy++;
        card.add(lblEmail,gbc);

        gbc.gridy++;
        card.add(txtEmail,gbc);

        gbc.gridy++;
        card.add(lblPassword,gbc);

        gbc.gridy++;
        card.add(txtPassword,gbc);

        gbc.gridy++;
        card.add(lblConfirm,gbc);

        gbc.gridy++;
        card.add(txtConfirm,gbc);

        gbc.gridy++;
        card.add(btnRegister,gbc);

        gbc.gridy++;
        card.add(login,gbc);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirm.getPassword());

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password dan konfirmasi password tidak cocok!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setFullName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            UserDAO userDAO = new UserDAO();
            boolean registered = userDAO.registerUser(user);

            if (registered) {
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Registrasi gagal. Username mungkin sudah dipakai.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new LoginFrame();
            }
        });

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(30,30,47));
        wrapper.add(card);

        mainPanel.add(wrapper,BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);

    }

}