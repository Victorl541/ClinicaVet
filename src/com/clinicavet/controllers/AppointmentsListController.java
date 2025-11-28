package com.clinicavet.controllers;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.views.AppointmentFormDialog;
import com.clinicavet.views.AppointmentsListView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentsListController {

    private final AppointmentsListView view;
    private final IAppointmentService appointmentService;
    private final IPetService petService;
    private final IUserService userService;
    private List<Appointment> allAppointments;

    public AppointmentsListController(AppointmentsListView view, IAppointmentService appointmentService,
                                      IPetService petService, IUserService userService) {
        this.view = view;
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.userService = userService;

        // Cargar tabla inicial
        loadAppointments("");

        // Listener de búsqueda EN TIEMPO REAL
        view.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchAppointments();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchAppointments();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchAppointments();
            }
        });

        // Listeners en la vista
        view.btnSearch.addActionListener(e -> searchAppointments());
        view.btnNew.addActionListener(e -> openNewAppointmentForm());
        view.btnEdit.addActionListener(e -> openEditAppointmentForm());
        view.btnCancel.addActionListener(e -> cancelAppointment());
    }

    public void loadAppointments(String filter) {
        // Siempre cargar la lista completa
        allAppointments = appointmentService.listAppointments();

        List<Appointment> appointments;

        if (filter == null || filter.trim().isEmpty()) {
            appointments = allAppointments;
        } else {
            String q = filter.trim().toLowerCase();
            appointments = allAppointments.stream()
                    .filter(a -> a.getMascota().getName().toLowerCase().contains(q)
                            || a.getMascota().getOwner().getName().toLowerCase().contains(q)
                            || a.getMedico().getName().toLowerCase().contains(q)
                            || a.getEstado().toString().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        view.setTableData(appointments);
    }

    private void searchAppointments() {
        String filter = view.getSearchText();
        loadAppointments(filter);
    }

    private void openNewAppointmentForm() {
        AppointmentFormDialog dialog = new AppointmentFormDialog(null, appointmentService, petService, userService);
        dialog.setVisible(true);

        // Recargar tabla después de cerrar el diálogo
        loadAppointments("");
        view.clearSearch();
    }

    private void openEditAppointmentForm() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una cita para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Usar la lista completa, no la filtrada
        if (selectedRow >= 0 && selectedRow < allAppointments.size()) {
            Appointment appointment = allAppointments.get(selectedRow);

            AppointmentFormDialog dialog = new AppointmentFormDialog(appointment, appointmentService, petService, userService);
            dialog.setVisible(true);

            // Recargar tabla después de editar
            loadAppointments("");
            view.clearSearch();
        }
    }

    private void cancelAppointment() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una cita para cancelar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Usar la lista completa, no la filtrada
        if (selectedRow >= 0 && selectedRow < allAppointments.size()) {
            Appointment appointment = allAppointments.get(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(view,
                    "¿Cancelar la cita de " + appointment.getMascota().getName() + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                appointment.setEstado(Estado.CANCELADA);
                boolean updated = appointmentService.updateAppointment(appointment);
                
                if (updated) {
                    loadAppointments("");
                    JOptionPane.showMessageDialog(view, "Cita cancelada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo cancelar la cita", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}