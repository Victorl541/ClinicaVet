package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.Login;
import com.clinicavet.views.MainWindow;

import javax.swing.*;
import java.util.Optional;

public class LoginController {

    private final IUserService userService;
    private final RolService rolService;
    private final MainController mainController;

    public LoginController(IUserService userService,
                           RolService rolService,
                           MainController mainController) {

        this.userService = userService;
        this.rolService = rolService;
        this.mainController = mainController;
        
        
        this.mainController.setLoginController(this);
    }

    public boolean login(String email, String password, Login loginView) {
        // Validar campos vacíos
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            showErrorMessage(loginView, "Email y contraseña son requeridos");
            return false;
        }

        // Buscar usuario
        Optional<User> opt = userService.getByEmail(email.trim());

        if (opt.isEmpty()) {
            showErrorMessage(loginView, "Usuario no encontrado");
            return false;
        }

        User user = opt.get();

        // Validar usuario activo
        if (!user.isActivo()) {
            showErrorMessage(loginView, "Usuario inactivo. Contacta al administrador");
            return false;
        }

        // Validar contraseña
        if (!user.getPassword().equals(password)) {
            showErrorMessage(loginView, "Contraseña incorrecta");
            return false;
        }

        // Validar rol asignado
        if (user.getRol() == null) {
            showErrorMessage(loginView, "Usuario sin rol asignado");
            return false;
        }

        // Login exitoso
        mainController.setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setMainController(mainController);
            mainController.setMainWindow(mainWindow);
            
            // Configurar información del usuario
            mainWindow.setUserInfo(user);
            
            // Configurar menú según rol
            mainWindow.setupMenu();
            
            mainWindow.setVisible(true);
            
            // Cargar vista de inicio
            mainController.openHome();
        });

        loginView.dispose();
        return true;
    }

    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            mainController.logout();

            SwingUtilities.invokeLater(() -> {
                Login login = new Login(this);
                login.setVisible(true);
            });
        }
    }

    // ===== MÉTODOS AUXILIARES =====

    private void showErrorMessage(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error de Autenticación",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccessMessage(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===== GETTERS =====

    public IUserService getUserService() {
        return userService;
    }

    public RolService getRolService() {
        return rolService;
    }

    public MainController getMainController() {
        return mainController;
    }
}