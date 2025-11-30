package com.clinicavet;

import com.clinicavet.controllers.LoginController;
import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.AppointmentRepository;
import com.clinicavet.model.repositories.InvoiceRepository;
import com.clinicavet.model.repositories.MedicalRecordRepository;
import com.clinicavet.model.repositories.OwnerRepository;
import com.clinicavet.model.repositories.PaymentRepository;
import com.clinicavet.model.repositories.PetRepository;
import com.clinicavet.model.repositories.RolRepository;
import com.clinicavet.model.repositories.UserRepository;
import com.clinicavet.model.services.AppointmentService;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IMedicalRecordService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IReportService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.InvoiceService;
import com.clinicavet.model.services.MedicalRecordService;
import com.clinicavet.model.services.OwnerService;
import com.clinicavet.model.services.PaymentService;
import com.clinicavet.model.services.PetService;
import com.clinicavet.model.services.ReportService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;
import com.clinicavet.util.ThemeManager;
import com.clinicavet.views.Login;
import java.io.File;
import javax.swing.*;

public class App {

    private static MainController mainController;

    public static void main(String[] args) {
        System.out.println("Iniciando Clínica Veterinaria...\n");
        
        // Crear directorio data/ si no existe
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
            System.out.println("Directorio 'data' creado");
        }

        // --- REPOSITORIOS ---
        System.out.println("Inicializando repositorios...");
        RolRepository rolRepo = new RolRepository();
        UserRepository userRepo = new UserRepository();
        OwnerRepository ownerRepo = new OwnerRepository();
        PetRepository petRepo = new PetRepository();
        AppointmentRepository appointmentRepo = new AppointmentRepository();
        MedicalRecordRepository medicalRecordRepo = new MedicalRecordRepository();
        InvoiceRepository invoiceRepo = new InvoiceRepository();
        PaymentRepository paymentRepo = new PaymentRepository();

        // Configurar dependencias entre repositorios
        userRepo.setRolRepository(rolRepo);
        petRepo.setOwnerRepository(ownerRepo);
        appointmentRepo.setPetRepository(petRepo);
        appointmentRepo.setUserRepository(userRepo);
        medicalRecordRepo.setAppointmentRepository(appointmentRepo);
        medicalRecordRepo.setPetRepository(petRepo);
        medicalRecordRepo.setUserRepository(userRepo);
        invoiceRepo.setOwnerRepository(ownerRepo);
        paymentRepo.setInvoiceRepository(invoiceRepo);

        // Cargar datos desde archivos JSON
        System.out.println("Cargando datos desde JSON...");
        rolRepo.load();
        userRepo.load();
        ownerRepo.load();
        petRepo.load();
        appointmentRepo.load();
        medicalRecordRepo.load();
        invoiceRepo.load();
        paymentRepo.load();
        System.out.println();

        // --- SERVICIOS ---
        System.out.println("Inicializando servicios...");
        RolService rolService = new RolService(rolRepo);
        IUserService userService = new UserService(userRepo, rolService);
        IOwnerService ownerService = new OwnerService(ownerRepo);
        IPetService petService = new PetService(petRepo);
        IAppointmentService appointmentService = new AppointmentService(appointmentRepo);
        IMedicalRecordService medicalRecordService = new MedicalRecordService(medicalRecordRepo);
        IInvoiceService invoiceService = new InvoiceService(invoiceRepo, paymentRepo);
        IPaymentService paymentService = new PaymentService(paymentRepo, invoiceRepo);
        IReportService reportService = new ReportService(appointmentRepo, medicalRecordRepo, invoiceRepo, paymentRepo);

        // Crear roles y usuario admin solo si no existen
        if (rolService.listRoles().isEmpty()) {
            System.out.println("Roles no encontrados. Creando roles iniciales...");
            
            Rol adminRol = new Rol(1, "ADMIN");
            Rol medicoRol = new Rol(2, "MEDICO");
            Rol auxiliarRol = new Rol(3, "AUXILIAR");

            rolService.createRol(adminRol);
            rolService.createRol(medicoRol);
            rolService.createRol(auxiliarRol);
            rolRepo.save();

            System.out.println("Usuario admin no encontrado. Creando usuario inicial...");
            User adminUser = new User("admin", "admin@clinic.com", "1234", adminRol);
            userService.createUser(adminUser);
            userRepo.save();
            
            System.out.println("Roles y usuario admin creados\n");
        }

        // --- CONTROLLERS ---
        System.out.println("Inicializando controladores...");
        mainController = new MainController(
                userService,
                rolService,
                ownerService,
                petService,
                appointmentService,
                medicalRecordService,
                invoiceService,
                paymentService,
                reportService
        );
        
        LoginController loginController = new LoginController(userService, rolService, mainController);

        // Agregar shutdown hook para guardar datos al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nGuardando datos antes de salir...");
            try {
                rolRepo.save();
                userRepo.save();
                ownerRepo.save();
                petRepo.save();
                appointmentRepo.save();
                medicalRecordRepo.save();
                invoiceRepo.save();
                paymentRepo.save();
                System.out.println("Datos guardados exitosamente.");
            } catch (Exception e) {
                System.err.println("Error al guardar datos: " + e.getMessage());
                e.printStackTrace();
            }
        }));

        // --- MOSTRAR LOGIN EN EDT ---
        System.out.println("Abriendo interfaz gráfica...\n");
        SwingUtilities.invokeLater(() -> {
            // Aplicar tema responsivo multiplataforma
            ThemeManager.applyTheme();
            ThemeManager.applyResponsiveScale();

            Login loginView = new Login(loginController);
            loginView.setVisible(true);
        });
    }

    public static MainController getMainController() {
        return mainController;
    }
}