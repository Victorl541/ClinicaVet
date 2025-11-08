package com.clinicavet;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.AppointmentRepository;
import com.clinicavet.model.repositories.OwnerRepository;
import com.clinicavet.model.repositories.PetRepository;
import com.clinicavet.model.repositories.RolRepository;
import com.clinicavet.model.repositories.UserRepository;
import com.clinicavet.model.services.AppointmentService;
import com.clinicavet.model.services.OwnerService;
import com.clinicavet.model.services.PetService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;
import com.clinicavet.views.Login;
import java.io.File;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        // Crear directorio data/ si no existe
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
        // repositorios
        OwnerRepository ownerRepo = new OwnerRepository();
        RolRepository rolRepo = new RolRepository();
        UserRepository userRepo = new UserRepository();
        PetRepository petRepo = new PetRepository();
        AppointmentRepository appointmentRepo = new AppointmentRepository();
        
        // Configurar dependencias entre repositorios
        userRepo.setRolRepository(rolRepo);
        petRepo.setOwnerRepository(ownerRepo);
        appointmentRepo.setUserRepository(userRepo);
        appointmentRepo.setPetRepository(petRepo);
        
        // Cargar datos desde archivos JSON
        rolRepo.load();
        userRepo.load();
        ownerRepo.load();
        petRepo.load();
        appointmentRepo.load();

        // servicios
        RolService rolService = new RolService(rolRepo);
        UserService userService = new UserService(userRepo, rolService);
        OwnerService ownerService = new OwnerService(ownerRepo);
        PetService petService = new PetService(petRepo);
        AppointmentService appointmentService = new AppointmentService(appointmentRepo);

        // Crear roles y usuario admin solo si no existen
        if (rolService.listRoles().isEmpty()) {
            Rol adminRol = new Rol(1, "ADMIN");
            Rol medicoRol = new Rol(2, "MEDICO");
            Rol auxiliarRol = new Rol(3, "AUXILIAR");
            
            rolService.createRol(adminRol);
            rolService.createRol(medicoRol);
            rolService.createRol(auxiliarRol);
            
            User adminUser = new User("admin", "admin", "1234", adminRol);
            userService.createUser(adminUser);
            
            // Guardar datos iniciales
            rolRepo.save();
            userRepo.save();
        }

        LoginController loginController = new LoginController(userService, rolService, ownerService, petService, appointmentService);

        // Agregar shutdown hook para guardar datos al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Guardando datos antes de salir...");
            appointmentRepo.save();
            petRepo.save();
            ownerRepo.save();
            userRepo.save();
            rolRepo.save();
            System.out.println("Datos guardados exitosamente.");
        }));

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
