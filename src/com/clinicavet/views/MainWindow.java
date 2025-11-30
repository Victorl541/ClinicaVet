package com.clinicavet.views;

import com.clinicavet.App;
import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {

    private MainController mainController;
    private JPanel contentPanel;
    private Map<String, JPanel> views;
    private JMenuBar menuBar;
    private JLabel lblUserInfo;
    private JLabel lblRoleInfo;

    public MainWindow() {
        setTitle("Clínica Veterinaria - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setResizable(true);

        views = new HashMap<>();
        initComponents();
        setupWindowListener();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void initComponents() {
        // --- MENU BAR ---
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 152, 219));
        menuBar.setForeground(Color.WHITE);
        setJMenuBar(menuBar);
        
        // ✅ Aplicar Look and Feel personalizado a los menús
        configureMenuLookAndFeel();

        // --- PANEL SUPERIOR ---
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // --- PANEL CENTRAL (CONTENIDO) ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        add(contentPanel, BorderLayout.CENTER);

        // --- PANEL INFERIOR (STATUS BAR) ---
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * ✅ Configurar estilo consistente de menús
     */
    private void configureMenuLookAndFeel() {
        // Colores explícitos para máxima visibilidad
        Color menuBackground = new Color(52, 152, 219);
        Color menuForeground = Color.WHITE;
        Color menuItemBackground = Color.WHITE;
        Color menuItemForeground = new Color(0, 0, 0);  // Negro puro
        Color selectionBackground = new Color(52, 152, 219);
        Color selectionForeground = Color.WHITE;
        Color borderColor = new Color(150, 150, 150);
        
        // Fuentes robustas para Linux
        Font menuFont = new Font("DejaVu Sans", Font.BOLD, 12);
        Font menuItemFont = new Font("DejaVu Sans", Font.PLAIN, 11);
        
        // === MENUBAR ===
        UIManager.put("MenuBar.background", menuBackground);
        UIManager.put("MenuBar.foreground", menuForeground);
        UIManager.put("MenuBar.border", BorderFactory.createLineBorder(borderColor, 1));
        
        // === MENU (Elementos principales) ===
        UIManager.put("Menu.background", menuBackground);
        UIManager.put("Menu.foreground", menuForeground);
        UIManager.put("Menu.font", menuFont);
        UIManager.put("Menu.selectionBackground", selectionBackground);
        UIManager.put("Menu.selectionForeground", selectionForeground);
        UIManager.put("Menu.border", BorderFactory.createEmptyBorder(5, 10, 5, 10));
        UIManager.put("Menu.opaque", true);
        
        // === MENUITEM (Opciones dentro del menú) ===
        UIManager.put("MenuItem.background", menuItemBackground);
        UIManager.put("MenuItem.foreground", menuItemForeground);
        UIManager.put("MenuItem.font", menuItemFont);
        UIManager.put("MenuItem.selectionBackground", selectionBackground);
        UIManager.put("MenuItem.selectionForeground", selectionForeground);
        UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));
        UIManager.put("MenuItem.opaque", true);
        UIManager.put("MenuItem.acceleratorForeground", new Color(100, 100, 100));
        
        // === POPUPMENU ===
        UIManager.put("PopupMenu.background", menuItemBackground);
        UIManager.put("PopupMenu.foreground", menuItemForeground);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(borderColor, 1));
        UIManager.put("PopupMenu.opaque", true);
        
        // === SEPARATOR ===
        UIManager.put("Separator.background", borderColor);
        UIManager.put("Separator.foreground", borderColor);
        
        System.out.println("✅ Estilos de menú configurados para Linux");
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Logo/Título
        JLabel lblTitle = new JLabel("CLÍNICA VETERINARIA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.WEST);

        // Info Usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(new Color(52, 152, 219));

        lblUserInfo = new JLabel();
        lblUserInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUserInfo.setForeground(Color.WHITE);
        userPanel.add(lblUserInfo);

        lblRoleInfo = new JLabel();
        lblRoleInfo.setFont(new Font("Arial", Font.BOLD, 12));
        lblRoleInfo.setForeground(new Color(46, 204, 113));
        userPanel.add(lblRoleInfo);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> handleLogout());
        userPanel.add(btnLogout);

        panel.add(userPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(new Color(189, 195, 199));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblStatus = new JLabel("Conectado");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(lblStatus);

        return panel;
    }

    public void setupMenu() {
        menuBar.removeAll();

        User currentUser = mainController.getCurrentUser();
        String userRole = currentUser.getRol().getName();

        // --- MENÚ HOME ---
        JMenu menuHome = createMenuHome();
        menuBar.add(menuHome);

        // --- MENÚ SEGÚN ROL ---
        if ("ADMIN".equalsIgnoreCase(userRole)) {
            createAdminMenu();
        } else if ("MEDICO".equalsIgnoreCase(userRole)) {
            createMedicoMenu();
        } else if ("AUXILIAR".equalsIgnoreCase(userRole)) {
            createAuxiliarMenu();
        }

        menuBar.revalidate();
        menuBar.repaint();
    }

    private JMenu createMenuHome() {
        JMenu menu = new JMenu("Inicio");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemHome = new JMenuItem("🏠 Home");
        itemHome.setFont(new Font("Arial", Font.PLAIN, 12));
        itemHome.setBackground(Color.WHITE);
        itemHome.setForeground(new Color(44, 62, 80));
        itemHome.addActionListener(e -> mainController.openHome());
        menu.add(itemHome);

        return menu;
    }

    private void createAdminMenu() {
        // --- MENÚ GESTIÓN ---
        JMenu menuManagement = new JMenu("Gestión");
        menuManagement.setForeground(Color.WHITE);
        menuManagement.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemUsers = new JMenuItem("👤 Usuarios");
        itemUsers.setFont(new Font("Arial", Font.PLAIN, 12));
        itemUsers.setBackground(Color.WHITE);
        itemUsers.setForeground(new Color(44, 62, 80));
        itemUsers.addActionListener(e -> mainController.openUsers());
        menuManagement.add(itemUsers);

        JMenuItem itemOwners = new JMenuItem("👨‍👩‍👧‍👦 Propietarios");
        itemOwners.setFont(new Font("Arial", Font.PLAIN, 12));
        itemOwners.setBackground(Color.WHITE);
        itemOwners.setForeground(new Color(44, 62, 80));
        itemOwners.addActionListener(e -> mainController.openOwners());
        menuManagement.add(itemOwners);

        JMenuItem itemPets = new JMenuItem("🐾 Mascotas");
        itemPets.setFont(new Font("Arial", Font.PLAIN, 12));
        itemPets.setBackground(Color.WHITE);
        itemPets.setForeground(new Color(44, 62, 80));
        itemPets.addActionListener(e -> mainController.openPets());
        menuManagement.add(itemPets);

        menuBar.add(menuManagement);

        // --- MENÚ CLÍNICA ---
        JMenu menuClinical = new JMenu("Clínica");
        menuClinical.setForeground(Color.WHITE);
        menuClinical.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemAppointments = new JMenuItem("📅 Citas");
        itemAppointments.setFont(new Font("Arial", Font.PLAIN, 12));
        itemAppointments.setBackground(Color.WHITE);
        itemAppointments.setForeground(new Color(44, 62, 80));
        itemAppointments.addActionListener(e -> mainController.openAppointments());
        menuClinical.add(itemAppointments);

        menuBar.add(menuClinical);
    }

    private void createMedicoMenu() {
        // --- MENÚ CITAS Y REGISTROS ---
        JMenu menuClinical = new JMenu("Clínica");
        menuClinical.setForeground(Color.WHITE);
        menuClinical.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemAppointments = new JMenuItem("📅 Citas");
        itemAppointments.setFont(new Font("Arial", Font.PLAIN, 12));
        itemAppointments.setBackground(Color.WHITE);
        itemAppointments.setForeground(new Color(44, 62, 80));
        itemAppointments.addActionListener(e -> mainController.openAppointments());
        menuClinical.add(itemAppointments);

        JMenuItem itemMedicalRecords = new JMenuItem("📋 Registros Médicos");
        itemMedicalRecords.setFont(new Font("Arial", Font.PLAIN, 12));
        itemMedicalRecords.setBackground(Color.WHITE);
        itemMedicalRecords.setForeground(new Color(44, 62, 80));
        itemMedicalRecords.addActionListener(e -> mainController.openMedicalRecords());
        menuClinical.add(itemMedicalRecords);

        menuBar.add(menuClinical);

        // --- MENÚ CONSULTA GENERAL ---
        JMenu menuConsult = new JMenu("Consulta");
        menuConsult.setForeground(Color.WHITE);
        menuConsult.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemPets = new JMenuItem("🐾 Mascotas");
        itemPets.setFont(new Font("Arial", Font.PLAIN, 12));
        itemPets.setBackground(Color.WHITE);
        itemPets.setForeground(new Color(44, 62, 80));
        itemPets.addActionListener(e -> mainController.openPets());
        menuConsult.add(itemPets);

        JMenuItem itemOwners = new JMenuItem("👨‍👩‍👧‍👦 Propietarios");
        itemOwners.setFont(new Font("Arial", Font.PLAIN, 12));
        itemOwners.setBackground(Color.WHITE);
        itemOwners.setForeground(new Color(44, 62, 80));
        itemOwners.addActionListener(e -> mainController.openOwners());
        menuConsult.add(itemOwners);

        menuBar.add(menuConsult);
    }

    private void createAuxiliarMenu() {
        System.out.println("🔧 Configurando menú para AUXILIAR...");

        // --- MENÚ FACTURACIÓN ---
        JMenu menuBilling = new JMenu("Facturación");
        menuBilling.setForeground(Color.WHITE);
        menuBilling.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemInvoices = new JMenuItem("📋 Facturas");
        itemInvoices.setFont(new Font("Arial", Font.PLAIN, 12));
        itemInvoices.setBackground(Color.WHITE);
        itemInvoices.setForeground(new Color(44, 62, 80));
        itemInvoices.addActionListener(e -> {
            try {
                System.out.println("   → Abriendo Facturas");
                mainController.openInvoices();
            } catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso Denegado", 
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        menuBilling.add(itemInvoices);

        JMenuItem itemPayments = new JMenuItem("💳 Pagos");
        itemPayments.setFont(new Font("Arial", Font.PLAIN, 12));
        itemPayments.setBackground(Color.WHITE);
        itemPayments.setForeground(new Color(44, 62, 80));
        itemPayments.addActionListener(e -> {
            try {
                System.out.println("   → Abriendo Pagos");
                mainController.openPaymentsTab();
            } catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso Denegado", 
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        menuBilling.add(itemPayments);

        menuBar.add(menuBilling);

        // --- MENÚ CONSULTA GENERAL ---
        JMenu menuConsult = new JMenu("Consulta");
        menuConsult.setForeground(Color.WHITE);
        menuConsult.setFont(new Font("Arial", Font.BOLD, 12));

        JMenuItem itemPets = new JMenuItem("🐾 Mascotas");
        itemPets.setFont(new Font("Arial", Font.PLAIN, 12));
        itemPets.setBackground(Color.WHITE);
        itemPets.setForeground(new Color(44, 62, 80));
        itemPets.addActionListener(e -> mainController.openPets());
        menuConsult.add(itemPets);

        JMenuItem itemOwners = new JMenuItem("👨‍👩‍👧‍👦 Propietarios");
        itemOwners.setFont(new Font("Arial", Font.PLAIN, 12));
        itemOwners.setBackground(Color.WHITE);
        itemOwners.setForeground(new Color(44, 62, 80));
        itemOwners.addActionListener(e -> mainController.openOwners());
        menuConsult.add(itemOwners);

        JMenuItem itemAppointments = new JMenuItem("📅 Citas");
        itemAppointments.setFont(new Font("Arial", Font.PLAIN, 12));
        itemAppointments.setBackground(Color.WHITE);
        itemAppointments.setForeground(new Color(44, 62, 80));
        itemAppointments.addActionListener(e -> mainController.openAppointments());
        menuConsult.add(itemAppointments);

        menuBar.add(menuConsult);

        System.out.println("✅ Menú AUXILIAR configurado");
    }

    public void setUserInfo(User user) {
        lblUserInfo.setText("Usuario: " + user.getName() + " (" + user.getEmail() + ")");
        lblRoleInfo.setText("[" + user.getRol().getName() + "]");
    }

    public void showView(String viewName, JPanel view) {
        views.put(viewName, view);
        contentPanel.removeAll();
        contentPanel.add(view, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ===== MÉTODOS AUXILIARES =====

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Guardar datos antes de cerrar sesión
            try {
                System.out.println("Guardando datos...");
                // Los datos se guardan automáticamente con el shutdown hook
            } catch (Exception ex) {
                System.err.println("Error al guardar: " + ex.getMessage());
            }

            // Cerrar MainWindow
            this.setVisible(false);
            this.dispose();

            // Volver a mostrar Login
            SwingUtilities.invokeLater(() -> {
                Login loginView = new Login(App.getMainController().getLoginController());
                loginView.setVisible(true);
            });
        }
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        MainWindow.this,
                        "¿Deseas cerrar la aplicación?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}