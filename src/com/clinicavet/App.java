package com.clinicavet;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.AppointmentRepository;
import com.clinicavet.model.repositories.OwnerRepository;
import com.clinicavet.model.repositories.PetRepository;
import com.clinicavet.model.repositories.RolRepository;
import com.clinicavet.model.repositories.UserRepository;
import com.clinicavet.model.services.AppointmentService;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.OwnerService;
import com.clinicavet.model.services.PetService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;
import com.clinicavet.views.Login;

import javax.swing.*;
import java.io.File;

public class App {

    private static MainController mainController;

    public static void main(String[] args) {
        // Crear directorio data/ si no existe
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // --- REPOSITORIOS ---
        RolRepository rolRepo = new RolRepository();
        UserRepository userRepo = new UserRepository();
        OwnerRepository ownerRepo = new OwnerRepository();
        PetRepository petRepo = new PetRepository();
        AppointmentRepository appointmentRepo = new AppointmentRepository();

        // Configurar dependencias entre repositorios
        userRepo.setRolRepository(rolRepo);
        petRepo.setOwnerRepository(ownerRepo);
        appointmentRepo.setPetRepository(petRepo);
        appointmentRepo.setUserRepository(userRepo);

        // Cargar datos desde archivos JSON
        rolRepo.load();
        userRepo.load();
        ownerRepo.load();
        petRepo.load();
        appointmentRepo.load();

        // --- SERVICIOS ---
        RolService rolService = new RolService(rolRepo);
        UserService userService = new UserService(userRepo, rolService);
        OwnerService ownerService = new OwnerService(ownerRepo);
        PetService petService = new PetService(petRepo);
        IAppointmentService appointmentService = new AppointmentService(appointmentRepo);

        // Crear roles y usuario admin solo si no existen
        if (rolService.listRoles().isEmpty()) {
            Rol adminRol = new Rol(1, "ADMIN");
            Rol medicoRol = new Rol(2, "MEDICO");
            Rol auxiliarRol = new Rol(3, "AUXILIAR");

            rolService.createRol(adminRol);
            rolService.createRol(medicoRol);
            rolService.createRol(auxiliarRol);

            User adminUser = new User("admin", "admin@clinic.com", "1234", adminRol);
            userService.createUser(adminUser);

            // Guardar datos iniciales
            rolRepo.save();
            userRepo.save();
        }

        // --- CONTROLLERS ---
        mainController = new MainController(userService, rolService, ownerService, petService, appointmentService);
        LoginController loginController = new LoginController(userService, rolService, mainController);

        // Agregar shutdown hook para guardar datos al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Guardando datos antes de salir...");
            userRepo.save();
            rolRepo.save();
            ownerRepo.save();
            petRepo.save();
            appointmentRepo.save();
            System.out.println("Datos guardados exitosamente.");
        }));

        // --- MOSTRAR LOGIN EN EDT ---
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

    public static MainController getMainController() {
        return mainController;
    }
}