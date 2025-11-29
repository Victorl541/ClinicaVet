package com.clinicavet.views;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblUserInfo;
    private JButton btnUsers;
    private JButton btnMedicalRecords;

    private MainController controller;
    private LoginController loginController;

    public MainWindow() {
        FlatLightLaf.setup();
        initComponents();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    private void initComponents() {
        setTitle("Sistema Veterinario — Panel Principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 245, 245));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblUserInfo = new JLabel("Usuario: --- | Rol: ---");
        lblUserInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topBar.add(lblUserInfo, BorderLayout.EAST);

        // --- SIDE MENU ---
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sideMenu.setBackground(new Color(240, 240, 240));

        JButton btnHome = new JButton("🏠 Inicio");
        btnUsers = new JButton("👥 Usuarios");
        JButton btnOwners = new JButton("🔑 Dueños");
        JButton btnPets = new JButton("🐕 Mascotas");
        JButton btnAppointments = new JButton("📅 Citas");
        btnMedicalRecords = new JButton("🩺 Atención Médica");
        JButton btnLogout = new JButton("🚪 Cerrar sesión");

        sideMenu.add(btnHome);
        sideMenu.add(btnUsers);
        sideMenu.add(btnOwners);
        sideMenu.add(btnPets);
        sideMenu.add(btnAppointments);
        sideMenu.add(btnMedicalRecords);
        sideMenu.add(Box.createVerticalGlue());
        sideMenu.add(btnLogout);

        // --- CONTENT ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        add(topBar, BorderLayout.NORTH);
        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        btnHome.addActionListener(e -> {
            if (controller != null) controller.openHome();
        });
        btnUsers.addActionListener(e -> {
            if (controller != null) controller.openUsers();
        });
        btnOwners.addActionListener(e -> {
            if (controller != null) controller.openOwners();
        });
        btnPets.addActionListener(e -> {
            if (controller != null) controller.openPets();
        });
        btnAppointments.addActionListener(e -> {
            if (controller != null) controller.openAppointments();
        });
        btnMedicalRecords.addActionListener(e -> {
            if (controller != null) controller.openMedicalRecords();
        });
        btnLogout.addActionListener(e -> {
            if (loginController != null) {
                loginController.logout();
            }
        });
    }

    public void updateUserHeader(String text) {
        lblUserInfo.setText(text);
    }

    public void showView(String name, JPanel view) {
        contentPanel.add(view, name);
        cardLayout.show(contentPanel, name);
    }

    public void loadDefaultView() {
        if (controller != null) {
            controller.openHome();
        }
    }

    public void setUserPermissions(User user) {
        if (user != null && user.getRol() != null) {
            boolean isAdmin = user.getRol().getName().equals("ADMIN");
            boolean isMedico = user.getRol().getName().equals("MEDICO");
            
            btnUsers.setVisible(isAdmin);
            btnUsers.setEnabled(isAdmin);
            
            btnMedicalRecords.setVisible(isMedico);
            btnMedicalRecords.setEnabled(isMedico);
        }
    }

    public void openOwners() {
        // TODO
    }

    public void openPets() {
        // TODO
    }

    public void openAppointments() {
        // TODO
    }
}