package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.Login;
import com.clinicavet.views.MainWindow;
import java.util.Optional;
import javax.swing.*;

public class LoginController {

    private final IUserService userService;
    private final RolService rolService;
    private final IOwnerService ownerService;
    private final IPetService petService;
    private final IAppointmentService appointmentService;

    public LoginController(IUserService userService, RolService rolService, IOwnerService ownerService, IPetService petService, IAppointmentService appointmentService) {
        this.userService = userService;
        this.rolService = rolService;
        this.ownerService = ownerService;
        this.petService = petService;
        this.appointmentService = appointmentService;
    }

    public boolean login(String email, String password, Login loginView) {
        Optional<User> userOpt = userService.getByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password) && user.isActivo()) {

                SwingUtilities.invokeLater(() -> {
                    MainWindow mainWindow = new MainWindow(user, userService, rolService, ownerService, petService, appointmentService, this);
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
