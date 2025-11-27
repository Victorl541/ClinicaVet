package com.clinicavet.controllers;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.views.AppointmentFormDialog;
import com.clinicavet.model.entities.Estado;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentFormDialogController {

    private final AppointmentFormDialog view;
    private final Appointment appointmentToEdit;
    private final IAppointmentService appointmentService;
    private final IPetService petService;
    private final IUserService userService;

    public AppointmentFormDialogController(AppointmentFormDialog view, Appointment appointment,
                                          IAppointmentService appointmentService,
                                          IPetService petService, IUserService userService) {
        this.view = view;
        this.appointmentToEdit = appointment;
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.userService = userService;

        loadPets();
        loadVets();
        loadData();
        setupListeners();
    }

    private void loadPets() {
        view.getCbPet().removeAllItems();
        List<Pet> pets = petService.listPets();

        for (Pet p : pets) {
            view.getCbPet().addItem(p.getName());
        }
    }

    private void loadVets() {
        view.getCbVet().removeAllItems();
        List<User> vets = userService.listUsers().stream()
                .filter(u -> u.getRol() != null && 
                       (u.getRol().getName().equals("MEDICO") || u.getRol().getName().equals("AUXILIAR")) &&
                       u.isActivo())
                .toList();

        for (User v : vets) {
            view.getCbVet().addItem(v.getName());
        }
    }

    private void loadData() {
        if (appointmentToEdit != null) {
            view.getCbPet().setSelectedItem(appointmentToEdit.getMascota().getName());
            view.getCbVet().setSelectedItem(appointmentToEdit.getMedico().getName());
            view.getTxtDate().setText(appointmentToEdit.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            view.getTxtTime().setText(appointmentToEdit.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
            view.getTxtReason().setText(appointmentToEdit.getMotivo());
            view.getCbStatus().setSelectedItem(appointmentToEdit.getEstado().toString());
        } else {
            view.getCbStatus().setSelectedItem("Pendiente");
        }
    }

    private void setupListeners() {
        view.getBtnSave().addActionListener(e -> saveAppointment());
    }

    private void saveAppointment() {
        String petName = (String) view.getCbPet().getSelectedItem();
        String vetName = (String) view.getCbVet().getSelectedItem();
        String date = view.getTxtDate().getText().trim();
        String time = view.getTxtTime().getText().trim();
        String reason = view.getTxtReason().getText().trim();
        String status = (String) view.getCbStatus().getSelectedItem();

        // Validaciones
        if (petName == null || vetName == null || date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campos obligatorios vacíos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar formato de fecha y hora
        LocalDate parsedDate;
        LocalTime parsedTime;
        
        try {
            parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Formato de fecha/hora inválido (yyyy-MM-dd HH:mm)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Pet pet = petService.listPets().stream()
                    .filter(p -> p.getName().equals(petName))
                    .findFirst()
                    .orElse(null);

            User vet = userService.listUsers().stream()
                    .filter(u -> u.getName().equals(vetName))
                    .findFirst()
                    .orElse(null);

            if (pet == null || vet == null) {
                JOptionPane.showMessageDialog(view, "Mascota o médico no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (appointmentToEdit == null) {
                // Crear nueva cita - duración por defecto 30 minutos
                Appointment newAppointment = new Appointment(parsedDate, parsedTime, 30, vet, pet, reason, getEstadoFromString("Pendiente"));
                boolean created = appointmentService.createAppointment(newAppointment);
                
                if (created) {
                    JOptionPane.showMessageDialog(view, "Cita creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    view.dispose();
                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo crear la cita (posible solapamiento)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Editar cita existente
                appointmentToEdit.setFecha(parsedDate);
                appointmentToEdit.setHora(parsedTime);
                appointmentToEdit.setMotivo(reason);
                appointmentToEdit.setEstado(getEstadoFromString(status));
                appointmentToEdit.setMascota(pet);
                appointmentToEdit.setMedico(vet);
                
                boolean updated = appointmentService.updateAppointment(appointmentToEdit);
                
                if (updated) {
                    JOptionPane.showMessageDialog(view, "Cita actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    view.dispose();
                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo actualizar la cita (posible solapamiento)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Estado getEstadoFromString(String estado) {
        try {
            return Estado.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Estado.PENDIENTE;
        }
    }
}