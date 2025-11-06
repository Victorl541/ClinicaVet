package com.clinicavet;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.OwnerRepository;
import com.clinicavet.model.repositories.PetRepository;
import com.clinicavet.model.repositories.RolRepository;
import com.clinicavet.model.repositories.UserRepository;
import com.clinicavet.model.services.OwnerService;
import com.clinicavet.model.services.PetService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;
import com.clinicavet.views.Login;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        // repositorios
        OwnerRepository ownerRepo = new OwnerRepository();
        RolRepository rolRepo = new RolRepository();
        UserRepository userRepo = new UserRepository();
        PetRepository petRepo = new PetRepository();

        // servicios
        RolService rolService = new RolService(rolRepo);
        UserService userService = new UserService(userRepo, rolService);
        OwnerService ownerService = new OwnerService(ownerRepo);
        PetService petService = new PetService(petRepo);

        // roles y admin
        Rol adminRol = new Rol(1, "ADMIN");
        Rol medicoRol = new Rol(2, "MEDICO");
        Rol auxiliarRol = new Rol(3, "AUXILIAR");

        rolService.createRol(adminRol);
        rolService.createRol(medicoRol);
        rolService.createRol(auxiliarRol);

        User adminUser = new User("Victor", "victor@utp.edu.co", "d1d2c3c4", adminRol);
        userService.createUser(adminUser);

        LoginController loginController = new LoginController(userService, rolService, ownerService, petService);

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ex) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    /* ignore */
                }
            }

            Login loginView = new Login(loginController);
            loginView.setVisible(true);
        });
    }
}