package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IMedicalRecordService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.AppointmentsListView;
import com.clinicavet.views.HomeView;
import com.clinicavet.views.MainWindow;
import com.clinicavet.views.MedicalRecordsListView;
import com.clinicavet.views.OwnersListView;
import com.clinicavet.views.PetsListView;
import com.clinicavet.views.UsersListView;
import java.util.List;
import java.util.Optional;

public class MainController {

    private final IUserService userService;
    private final RolService rolService;
    private final IOwnerService ownerService;
    private final IPetService petService;
    private final IAppointmentService appointmentService;
    private final IMedicalRecordService medicalRecordService;

    private MainWindow mainWindow;
    private User currentUser;

    public MainController(IUserService userService, RolService rolService, IOwnerService ownerService, 
                         IPetService petService, IAppointmentService appointmentService,
                         IMedicalRecordService medicalRecordService) {
        this.userService = userService;
        this.rolService = rolService;
        this.ownerService = ownerService;
        this.petService = petService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public IUserService getUserService() {
        return userService;
    }

    // ========================
    // HOME
    // ========================

    public void openHome() {
        HomeView view = new HomeView();
        new HomeController(view, userService, petService, ownerService, appointmentService);
        mainWindow.showView("home", view);
    }

    // ========================
    // USERS
    // ========================

    public void openUsers() {
        UsersListView view = new UsersListView();
        new UsersListController(view, userService, rolService);
        mainWindow.showView("users", view);
    }

    public List<User> listAllUsers() {
        return userService.listUsers();
    }

    public Optional<User> findUserById(Integer id) {
        return userService.getById(id);
    }

    public Optional<User> findUserByEmailOrName(String query) {
        return userService.listUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(query) || u.getName().equalsIgnoreCase(query))
                .findFirst();
    }

    public void activateUserById(Integer id) {
        Optional<User> opt = userService.getById(id);
        if (opt.isPresent()) {
            User u = opt.get();
            u.setActivo(true);
            userService.update(u);
        }
    }

    public void deactivateUserById(Integer id) {
        Optional<User> opt = userService.getById(id);
        if (opt.isPresent()) {
            User u = opt.get();
            u.setActivo(false);
            userService.update(u);
        }
    }

    // ========================
    // OWNERS
    // ========================

    public void openOwners() {
        OwnersListView view = new OwnersListView();
        new OwnersListController(view, ownerService);
        mainWindow.showView("owners", view);
    }

    // ========================
    // PETS
    // ========================

    public void openPets() {
        PetsListView view = new PetsListView();
        new PetsListController(view, petService, ownerService);
        mainWindow.showView("pets", view);
    }

    // ========================
    // APPOINTMENTS
    // ========================

    public void openAppointments() {
        AppointmentsListView view = new AppointmentsListView();
        new AppointmentsListController(view, appointmentService, petService, userService);
        mainWindow.showView("appointments", view);
    }

    // ========================
    // MEDICAL RECORDS (Solo MEDICO)
    // ========================

    public void openMedicalRecords() {
        MedicalRecordsListView view = new MedicalRecordsListView();
        new MedicalRecordsListController(view, medicalRecordService, appointmentService, petService);
        mainWindow.showView("medicalRecords", view);
    }

    // ========================
    // LOGOUT
    // ========================

    public void logout() {
        if (mainWindow != null) {
            mainWindow.setVisible(false);
            mainWindow.dispose();
        }
        currentUser = null;
    }
}