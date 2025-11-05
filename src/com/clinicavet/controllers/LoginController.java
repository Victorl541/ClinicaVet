package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.Login;
import com.clinicavet.views.MainWindow;

import javax.swing.*;
import java.util.Optional;

public class LoginController {

    private final IUserService userService;
    private final RolService rolService;
    private final IOwnerService ownerService;

    public LoginController(IUserService userService, RolService rolService, IOwnerService ownerService) {
        this.userService = userService;
        this.rolService = rolService;
        this.ownerService = ownerService;
    }


    public boolean login(String email, String password, Login loginView) {
        Optional<User> userOpt = userService.getByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password) && user.isActivo()) {
                
                SwingUtilities.invokeLater(() -> {
                    MainWindow mainWindow = new MainWindow(user, userService, rolService, ownerService, this);
                    mainWindow.setVisible(true);
                });
                loginView.dispose();
                return true;
            }
        }
        return false;
    }


    public void onLogout() {
    }
}
