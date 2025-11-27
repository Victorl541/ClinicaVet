package com.clinicavet.controllers;

import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.views.HomeView;

public class HomeController {

    private final HomeView view;
    private final IUserService userService;
    private final IPetService petService;
    private final IOwnerService ownerService;
    private final IAppointmentService appointmentService;

    public HomeController(HomeView view, IUserService userService, IPetService petService,
                        IOwnerService ownerService, IAppointmentService appointmentService) {
        this.view = view;
        this.userService = userService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.appointmentService = appointmentService;

        loadStats();
    }

    private void loadStats() {
        // Contar médicos activos
        long medicosActivos = userService.listUsers().stream()
                .filter(u -> u.isActivo() && u.getRol() != null && u.getRol().getName().equals("MEDICO"))
                .count();

        // Contar auxiliares activos
        long auxiliaresActivos = userService.listUsers().stream()
                .filter(u -> u.isActivo() && u.getRol() != null && u.getRol().getName().equals("AUXILIAR"))
                .count();

        // Contar mascotas creadas
        long mascotasCreadas = petService.listPets().size();

        // Contar dueños registrados
        long duenosRegistrados = ownerService.findAll().size();

        // Actualizar vista
        view.setMedicosActivos((int) medicosActivos);
        view.setAuxiliaresActivos((int) auxiliaresActivos);
        view.setMascotasCreadas((int) mascotasCreadas);
        view.setDuenosRegistrados((int) duenosRegistrados);
    }
}