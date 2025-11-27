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
    }

    public boolean login(String email, String password, Login loginView) {
        Optional<User> opt = userService.getByEmail(email);

        if (opt.isEmpty()) {
            return false;
        }

        User user = opt.get();

        if (!user.isActivo() || !user.getPassword().equals(password)) {
            return false;
        }

        mainController.setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setController(mainController);
            mainWindow.setLoginController(this);
            mainController.setMainWindow(mainWindow);
            mainWindow.updateUserHeader("Usuario: " + user.getName() + " | Rol: " + user.getRol().getName());
            
            // Establecer permisos según rol
            mainWindow.setUserPermissions(user);
            
            mainWindow.setVisible(true);
            
            // Cargar vista de inicio por defecto
            mainWindow.loadDefaultView();
        });

        loginView.dispose();
        return true;
    }

    public void logout() {
        // Cerrar sesión del controlador principal
        mainController.logout();

        // Mostrar Login nuevamente
        SwingUtilities.invokeLater(() -> {
            Login login = new Login(this);
            login.setVisible(true);
        });
    }

    public void onLogout() {
        logout();
    }
}