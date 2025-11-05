package com.clinicavet.views;

import com.clinicavet.controllers.LoginController;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final LoginController controller;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login(LoginController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Clínica Veterinaria");
        setSize(480, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Clínica Veterinaria", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0, 102, 204));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        form.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        form.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> onLogin());
        form.add(btnLogin, gbc);

        mainPanel.add(form, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        boolean ok = controller.login(email, password, this);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
