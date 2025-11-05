package com.clinicavet.views;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.panels.ListUsersPanelAdapter;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final MainController controller;
    private final User user;
    private final IUserService userService;
    private final RolService rolService;
    private final LoginController loginController;

    private JPanel sidebar;
    private JPanel content;
    private CardLayout cardLayout;
    private ListUsersPanelAdapter listUsersPanel;

    // Panel keys
    private static final String DASHBOARD = "DASHBOARD";
    private static final String LIST_USERS = "LIST_USERS";
    private static final String CREATE_USER = "CREATE_USER";
    private static final String EDIT_USER = "EDIT_USER";
    private static final String DISABLE_USER = "DISABLE_USER";
    private static final String RESET_PASSWORD = "RESET_PASSWORD";

    public MainWindow(User loggedUser, IUserService userService, RolService rolService, LoginController loginController) {
        this.user = loggedUser;
        this.userService = userService;
        this.rolService = rolService;
        this.loginController = loginController;
        this.controller = new MainController(loggedUser, userService, rolService);
        this.controller.setMainWindow(this);

        setTitle("Clínica Veterinaria - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        initUI();
        configureByRole();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        // HEADER SUPERIOR
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel lblTitle = new JLabel("🐾 Huellitas Sanas", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("👤 " + user.getName() + " | " + user.getRol().getName());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);
        getContentPane().add(header, BorderLayout.NORTH);

        // SIDEBAR
        sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));

        sidebar.add(Box.createVerticalGlue()); // espaciado inicial

        JButton btnDashboard = createSidebarButton(" Principal", e -> showPanel(DASHBOARD));
        JButton btnListUsers = createSidebarButton(" Listar Usuarios", e -> showPanel(LIST_USERS));
        JButton btnCreateUser = createSidebarButton(" Crear Usuario", e -> showPanel(CREATE_USER));
        JButton btnEditUser = createSidebarButton(" Editar Usuario", e -> showPanel(EDIT_USER));
        JButton btnDisableUser = createSidebarButton(" Desactivar Usuario", e -> showPanel(DISABLE_USER));
        JButton btnResetPassword = createSidebarButton(" Restablecer Contraseña", e -> showPanel(RESET_PASSWORD));
        JButton btnLogout = createSidebarButton(" Cerrar Sesión", e -> logout());

        // Asignación de roles
        btnListUsers.putClientProperty("role", "ADMIN");
        btnCreateUser.putClientProperty("role", "ADMIN");
        btnEditUser.putClientProperty("role", "ADMIN");
        btnDisableUser.putClientProperty("role", "ADMIN");
        btnResetPassword.putClientProperty("role", "ADMIN");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnListUsers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnCreateUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnEditUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnDisableUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnResetPassword);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(btnLogout);

        getContentPane().add(sidebar, BorderLayout.WEST);

        // CONTENIDO CENTRAL
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        content.setBackground(Color.WHITE);

        content.add(createDashboardPanel(), DASHBOARD);
        content.add(new com.clinicavet.views.panels.CreateUserPanelAdapter(controller), CREATE_USER);
        content.add(new com.clinicavet.views.panels.EditUserPanelAdapter(controller), EDIT_USER);
        content.add(new com.clinicavet.views.panels.DisableUserPanelAdapter(controller), DISABLE_USER);
        content.add(new com.clinicavet.views.panels.ResetPasswordPanelAdapter(controller), RESET_PASSWORD);
        listUsersPanel = new com.clinicavet.views.panels.ListUsersPanelAdapter(controller);
        content.add(listUsersPanel, LIST_USERS);

        getContentPane().add(content, BorderLayout.CENTER);

        showPanel(DASHBOARD);
    }

    private JButton createSidebarButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(new Color(44, 62, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setOpaque(true);

        btn.addActionListener(listener);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(44, 62, 80));
            }
        });
        return btn;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(" Bienvenido al Panel Principal de Huellitas Sanas", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setBorder(BorderFactory.createEmptyBorder(60, 30, 60, 30));

        JLabel sub = new JLabel("Seleccione una opción del menú para comenzar", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(new Color(100, 100, 100));

        panel.add(lbl, BorderLayout.CENTER);
        panel.add(sub, BorderLayout.SOUTH);

        return panel;
    }

    private void showPanel(String key) {
        cardLayout.show(content, key);
    }

    private void configureByRole() {
        String role = user.getRol().getName().toUpperCase();
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton) {
                Object requiredRole = ((JButton) c).getClientProperty("role");
                if (requiredRole == null) {
                    c.setVisible(true);
                } else {
                    c.setVisible(role.equalsIgnoreCase(requiredRole.toString()));
                }
            }
        }
    }

    public ListUsersPanelAdapter getListUsersPanel() {
        return listUsersPanel;
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginController newLoginController = new LoginController(userService, rolService);
            Login login = new Login(newLoginController);
            login.setVisible(true);
        });
    }
}
