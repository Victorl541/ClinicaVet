package com.clinicavet.controllers;

import com.clinicavet.App;
import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IMedicalRecordService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.views.AttendAppointmentDialog;
import com.clinicavet.views.MedicalRecordsListView;
import com.clinicavet.views.PetHistoryDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para el módulo de Atención Médica
 * Gestiona las citas del médico y permite registrar atenciones
 */
public class MedicalRecordsListController {
    
    private final MedicalRecordsListView view;
    private final IMedicalRecordService medicalRecordService;
    private final IAppointmentService appointmentService;
    private final IPetService petService;
    private List<Appointment> allAppointments;
    
    public MedicalRecordsListController(MedicalRecordsListView view,
                                       IMedicalRecordService medicalRecordService,
                                       IAppointmentService appointmentService,
                                       IPetService petService) {
        this.view = view;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        this.petService = petService;
        
        loadMyAppointments("");
        
        // Búsqueda en tiempo real
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
        
        view.btnSearch.addActionListener(e -> searchAppointments());
        view.btnAttend.addActionListener(e -> attendAppointment());
        view.btnNoAttended.addActionListener(e -> markAsNoAttended());
        view.btnReschedule.addActionListener(e -> rescheduleAppointment());
        view.btnViewHistory.addActionListener(e -> viewPetHistory());
    }
    
    /**
     * Carga las citas del médico actual
     */
    public void loadMyAppointments(String filter) {
        User currentDoctor = App.getMainController().getCurrentUser();
        
        // Filtrar solo citas del médico actual que NO estén canceladas
        allAppointments = appointmentService.listAppointments().stream()
                .filter(a -> a.getMedico().getId() == currentDoctor.getId())
                .filter(a -> a.getEstado() != Estado.CANCELADA)
                .collect(Collectors.toList());
        
        if (filter == null || filter.trim().isEmpty()) {
            view.setTableData(allAppointments);
        } else {
            String q = filter.trim().toLowerCase();
            List<Appointment> filtered = allAppointments.stream()
                    .filter(a -> a.getMascota().getName().toLowerCase().contains(q) ||
                                 a.getMascota().getOwner().getName().toLowerCase().contains(q) ||
                                 a.getMotivo().toLowerCase().contains(q))
                    .collect(Collectors.toList());
            view.setTableData(filtered);
        }
    }
    
    private void searchAppointments() {
        String filter = view.getSearchText();
        loadMyAppointments(filter);
    }
    
    /**
     * Atender una cita - abre diálogo para registrar la atención
     */
    private void attendAppointment() {
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una cita para atender", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (selectedRow >= 0 && selectedRow < allAppointments.size()) {
            Appointment appointment = allAppointments.get(selectedRow);
            
            // Verificar que no esté ya atendida
            if (appointment.getEstado() == Estado.ATENDIDA) {
                JOptionPane.showMessageDialog(view, "Esta cita ya fue atendida", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Abrir diálogo para registrar atención
            AttendAppointmentDialog dialog = new AttendAppointmentDialog(null, appointment);
            new AttendAppointmentDialogController(dialog, appointment, medicalRecordService, appointmentService);
            dialog.setVisible(true);
            
            loadMyAppointments("");
            view.clearSearch();
        }
    }
    
    /**
     * Marcar cita como No Asistió
     */
    private void markAsNoAttended() {
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una cita", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (selectedRow >= 0 && selectedRow < allAppointments.size()) {
            Appointment appointment = allAppointments.get(selectedRow);
            
            int confirm = JOptionPane.showConfirmDialog(view,
                    "¿Marcar como NO ASISTIÓ la cita de " + appointment.getMascota().getName() + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                appointment.setEstado(Estado.NO_ASISTIO);
                appointmentService.updateAppointment(appointment);
                loadMyAppointments("");
                JOptionPane.showMessageDialog(view, "Cita marcada como NO ASISTIÓ", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Marcar cita para reprogramar
     */
    private void rescheduleAppointment() {
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una cita", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (selectedRow >= 0 && selectedRow < allAppointments.size()) {
            Appointment appointment = allAppointments.get(selectedRow);
            
            // Preguntar si desea seleccionar nueva fecha ahora o marcar para más tarde
            String[] options = {"Seleccionar nueva fecha ahora", "Marcar para reprogramar más tarde", "Cancelar"};
            int choice = JOptionPane.showOptionDialog(view,
                    "¿Desea reprogramar la cita de " + appointment.getMascota().getName() + " ahora o más tarde?",
                    "Reprogramar Cita",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            
            if (choice == 0) {
                // Seleccionar nueva fecha ahora - abrir diálogo de edición de cita
                openEditAppointmentForReschedule(appointment);
            } else if (choice == 1) {
                // Marcar para reprogramar más tarde
                appointment.setEstado(Estado.REPROGRAMAR);
                appointmentService.updateAppointment(appointment);
                loadMyAppointments("");
                JOptionPane.showMessageDialog(view, 
                    "Cita marcada para REPROGRAMAR\nComuníquese con el dueño para agendar nueva fecha", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            // Si choice == 2 o cierra el diálogo, no hace nada
        }
    }
    
    /**
     * Abrir diálogo para editar/reprogramar una cita
     */
    private void openEditAppointmentForReschedule(Appointment appointment) {
        // Importar las clases necesarias dinámicamente
        try {
            // Crear instancia del diálogo de edición de citas
            com.clinicavet.views.AppointmentFormDialog dialog = 
                new com.clinicavet.views.AppointmentFormDialog(
                    appointment, 
                    appointmentService, 
                    petService, 
                    App.getMainController().getUserService()
                );
            
            dialog.setVisible(true);
            
            // Recargar la lista después de cerrar el diálogo
            loadMyAppointments("");
            view.clearSearch();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error al abrir el formulario de reprogramación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ver historia clínica de una mascota
     */
    private void viewPetHistory() {
        // Seleccionar una mascota
        List<Pet> pets = petService.listPets();
        if (pets.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay mascotas registradas", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] petNames = pets.stream().map(Pet::getName).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(view,
                "Seleccione una mascota:",
                "Historia Clínica",
                JOptionPane.QUESTION_MESSAGE,
                null,
                petNames,
                petNames[0]);
        
        if (selected != null) {
            Pet pet = pets.stream()
                    .filter(p -> p.getName().equals(selected))
                    .findFirst()
                    .orElse(null);
            
            if (pet != null) {
                List<MedicalRecord> history = medicalRecordService.getMedicalRecordsByMascota(pet);
                PetHistoryDialog historyDialog = new PetHistoryDialog(null, pet, history);
                historyDialog.setVisible(true);
            }
        }
    }
}
