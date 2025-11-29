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
import com.clinicavet.views.MedicalRecordFormDialog;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordFormDialogController {
    
    private final MedicalRecordFormDialog view;
    private final MedicalRecord recordToEdit;
    private final IMedicalRecordService medicalRecordService;
    private final IAppointmentService appointmentService;
    private final IPetService petService;
    
    public MedicalRecordFormDialogController(MedicalRecordFormDialog view,
                                            MedicalRecord recordToEdit,
                                            IMedicalRecordService medicalRecordService,
                                            IAppointmentService appointmentService,
                                            IPetService petService) {
        this.view = view;
        this.recordToEdit = recordToEdit;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        this.petService = petService;
        
        if (!view.isEditMode()) {
            loadCompletedAppointments();
        } else {
            loadRecordData();
        }
        
        setupListeners();
    }
    
    private void loadCompletedAppointments() {
        List<Appointment> completedAppointments = appointmentService.listAppointments().stream()
                .filter(a -> a.getEstado() == Estado.COMPLETADA)
                .filter(a -> medicalRecordService.getMedicalRecordByAppointmentId(a.getId()).isEmpty())
                .collect(Collectors.toList());
        
        for (Appointment a : completedAppointments) {
            String display = a.getFecha() + " " + a.getHora() + " - " + 
                           a.getMascota().getName() + " (" + a.getMascota().getOwner().getName() + ")";
            view.getCbAppointment().addItem(display);
        }
        
        if (view.getCbAppointment().getItemCount() > 0) {
            view.getCbAppointment().setSelectedIndex(0);
            updateFieldsFromAppointment(completedAppointments.get(0));
            
            view.getCbAppointment().addActionListener(e -> {
                int idx = view.getCbAppointment().getSelectedIndex();
                if (idx >= 0 && idx < completedAppointments.size()) {
                    updateFieldsFromAppointment(completedAppointments.get(idx));
                }
            });
        }
    }
    
    private void updateFieldsFromAppointment(Appointment appointment) {
        view.getTxtDate().setText(appointment.getFecha().toString());
        view.getTxtTime().setText(appointment.getHora().toString());
        view.getTxtPet().setText(appointment.getMascota().getName());
        view.getTxtOwner().setText(appointment.getMascota().getOwner().getName());
    }
    
    private void loadRecordData() {
        if (recordToEdit != null) {
            view.getTxtDate().setText(recordToEdit.getFecha().toString());
            view.getTxtTime().setText(recordToEdit.getHora().toString());
            view.getTxtPet().setText(recordToEdit.getMascota().getName());
            view.getTxtOwner().setText(recordToEdit.getMascota().getOwner().getName());
            view.getTxtSintomas().setText(recordToEdit.getSintomas());
            view.getTxtDiagnostico().setText(recordToEdit.getDiagnostico());
            view.getTxtProcedimientos().setText(recordToEdit.getProcedimientos());
            view.getTxtTratamiento().setText(recordToEdit.getTratamiento());
            view.getTxtOrdenes().setText(recordToEdit.getOrdenes());
            view.getTxtObservaciones().setText(recordToEdit.getObservaciones());
        }
    }
    
    private void setupListeners() {
        view.getBtnSave().addActionListener(e -> saveRecord());
    }
    
    private void saveRecord() {
        String sintomas = view.getTxtSintomas().getText().trim();
        String diagnostico = view.getTxtDiagnostico().getText().trim();
        String procedimientos = view.getTxtProcedimientos().getText().trim();
        String tratamiento = view.getTxtTratamiento().getText().trim();
        String ordenes = view.getTxtOrdenes().getText().trim();
        String observaciones = view.getTxtObservaciones().getText().trim();
        
        if (diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(view, "El diagnóstico es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (recordToEdit == null) {
                // Crear nuevo registro
                int selectedIdx = view.getCbAppointment().getSelectedIndex();
                List<Appointment> completedAppointments = appointmentService.listAppointments().stream()
                        .filter(a -> a.getEstado() == Estado.COMPLETADA)
                        .filter(a -> medicalRecordService.getMedicalRecordByAppointmentId(a.getId()).isEmpty())
                        .collect(Collectors.toList());
                
                if (selectedIdx < 0 || selectedIdx >= completedAppointments.size()) {
                    JOptionPane.showMessageDialog(view, "Seleccione una cita válida", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Appointment appointment = completedAppointments.get(selectedIdx);
                User currentMedico = App.getMainController().getCurrentUser();
                
                MedicalRecord newRecord = new MedicalRecord(
                        appointment,
                        appointment.getMascota(),
                        currentMedico,
                        LocalDate.now(),
                        LocalTime.now(),
                        sintomas,
                        diagnostico,
                        procedimientos,
                        tratamiento,
                        ordenes,
                        observaciones
                );
                
                medicalRecordService.createMedicalRecord(newRecord);
                JOptionPane.showMessageDialog(view, "Atención médica registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            } else {
                // Editar registro existente
                recordToEdit.setSintomas(sintomas);
                recordToEdit.setDiagnostico(diagnostico);
                recordToEdit.setProcedimientos(procedimientos);
                recordToEdit.setTratamiento(tratamiento);
                recordToEdit.setOrdenes(ordenes);
                recordToEdit.setObservaciones(observaciones);
                
                medicalRecordService.updateMedicalRecord(recordToEdit);
                JOptionPane.showMessageDialog(view, "Registro actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
