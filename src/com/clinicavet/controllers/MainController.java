package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IMedicalRecordService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IReportService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.AppointmentsListView;
import com.clinicavet.views.HomeView;
import com.clinicavet.views.InvoicesListView;
import com.clinicavet.views.MainWindow;
import com.clinicavet.views.MedicalRecordsListView;
import com.clinicavet.views.OwnersListView;
import com.clinicavet.views.PaymentsView;
import com.clinicavet.views.PetsListView;
import com.clinicavet.views.ReportsView;
import com.clinicavet.views.UsersListView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainController {

    private final IUserService userService;
    private final RolService rolService;
    private final IOwnerService ownerService;
    private final IPetService petService;
    private final IAppointmentService appointmentService;
    private final IMedicalRecordService medicalRecordService;
    private final IInvoiceService invoiceService;
    private final IPaymentService paymentService;
    private final IReportService reportService;

    private MainWindow mainWindow;
    private User currentUser;
    private LoginController loginController;

    public MainController(IUserService userService, RolService rolService, IOwnerService ownerService, 
                         IPetService petService, IAppointmentService appointmentService,
                         IMedicalRecordService medicalRecordService, IInvoiceService invoiceService,
                         IPaymentService paymentService, IReportService reportService) {
        this.userService = userService;
        this.rolService = rolService;
        this.ownerService = ownerService;
        this.petService = petService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.reportService = reportService;
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

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    // ========================
    // HOME
    // ========================

    public void openHome() {
        HomeView view = new HomeView();
        new HomeController(view, userService, petService, ownerService, appointmentService, this);
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
    // INVOICES (Solo AUXILIAR)
    // ========================

    public void openInvoices() {
        System.out.println("[MainController] Abriendo Facturas...");
        
        // Validar que el usuario sea AUXILIAR
        if (!isUserRoleAuxiliar()) {
            throw new SecurityException("Acceso denegado: Solo AUXILIAR puede acceder a Facturación");
        }
        
        InvoicesListView view = new InvoicesListView();
        new InvoicesController(view, invoiceService, ownerService, paymentService);
        mainWindow.showView("invoices", view);
        
        System.out.println("Facturas abierto");
    }

    // ========================
    // PAYMENTS (Solo AUXILIAR)
    // ========================

    public void openPaymentsTab() {
        System.out.println("[MainController] Abriendo Pagos...");
        
        // Validar que el usuario sea AUXILIAR
        if (!isUserRoleAuxiliar()) {
            throw new SecurityException("Acceso denegado: Solo AUXILIAR puede acceder a Pagos");
        }

        // USAR PaymentsView CON PaymentsViewController
        PaymentsView view = new PaymentsView();
        new PaymentsViewController(view, paymentService, invoiceService);
        mainWindow.showView("payments", view);
        
        System.out.println("Tab de Pagos abierto");
    }

    // ========================
    // INVOICE SERVICES
    // ========================

    public IInvoiceService getInvoiceService() {
        return invoiceService;
    }

    public IPaymentService getPaymentService() {
        return paymentService;
    }

    public List<?> listAllInvoices() {
        return invoiceService.listInvoices();
    }

    public Optional<?> findInvoiceById(UUID id) {
        return invoiceService.getInvoiceById(id);
    }

    public String generateInvoiceNumber() {
        return invoiceService.generateInvoiceNumber();
    }

    // ========================
    // PAYMENT SERVICES
    // ========================

    public double getTotalPaidByInvoice(UUID invoiceId) {
        return paymentService.getTotalPaidByInvoice(invoiceId);
    }

    public double getRemainingAmount(UUID invoiceId) {
        return paymentService.getRemainingAmount(invoiceId);
    }

    public List<?> getPaymentsByInvoice(UUID invoiceId) {
        return paymentService.findByInvoiceId(invoiceId);
    }

    // ========================
    // SECURITY & VALIDATION
    // ========================

    private boolean isUserRoleAuxiliar() {
        if (currentUser == null) {
            System.err.println("Usuario actual es null");
            return false;
        }
        
        boolean isAuxiliar = currentUser.getRol() != null && 
               currentUser.getRol().getName().equalsIgnoreCase("AUXILIAR");
        
        System.out.println("Validación de rol AUXILIAR: " + isAuxiliar);
        return isAuxiliar;
    }

    private boolean isUserRoleMedico() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRol() != null && 
               currentUser.getRol().getName().equalsIgnoreCase("MEDICO");
    }

    private boolean isUserRoleAdmin() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRol() != null && 
               currentUser.getRol().getName().equalsIgnoreCase("ADMIN");
    }

    public boolean canAccessInvoices() {
        return isUserRoleAuxiliar();
    }

    public boolean canAccessMedicalRecords() {
        return isUserRoleMedico();
    }

    public boolean canAccessUsers() {
        return isUserRoleAdmin();
    }

    // ========================
    // REPORTS (Solo ADMIN)
    // ========================

    public void openReports() {
        ReportsView view = new ReportsView();
        new ReportsController(view, reportService);
        mainWindow.showView("reports", view);
    }

    // ========================
    // LOGOUT
    // ========================

    public void logout() {
        System.out.println("🔒 [MainController] Cerrando sesión...");
        
        if (mainWindow != null) {
            mainWindow.setVisible(false);
            mainWindow.dispose();
        }
        currentUser = null;
        
        System.out.println("Sesión cerrada");
    }
}