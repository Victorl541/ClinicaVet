package com.clinicavet.controllers;

import com.clinicavet.App;
import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IMedicalRecordService;
import com.clinicavet.views.AttendAppointmentDialog;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controlador para el diálogo de atención médica
 */
public class AttendAppointmentDialogController {
    
    private final AttendAppointmentDialog dialog;
    private final Appointment appointment;
    private final IMedicalRecordService medicalRecordService;
    private final IAppointmentService appointmentService;
    
    public AttendAppointmentDialogController(AttendAppointmentDialog dialog,
                                            Appointment appointment,
                                            IMedicalRecordService medicalRecordService,
                                            IAppointmentService appointmentService) {
        this.dialog = dialog;
        this.appointment = appointment;
        this.medicalRecordService = medicalRecordService;
        this.appointmentService = appointmentService;
        
        dialog.getBtnSave().addActionListener(e -> saveAttention());
    }
    
    /**
     * Guarda la atención médica y cierra la cita
     */
    private void saveAttention() {
        // Validar que el diagnóstico no esté vacío
        String diagnostico = dialog.getTxtDiagnostico().getText().trim();
        if (diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(dialog,
                    "El diagnóstico es obligatorio",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dialog.getTxtDiagnostico().requestFocus();
            return;
        }
        
        // Crear el registro médico
        MedicalRecord record = new MedicalRecord(
                appointment,
                appointment.getMascota(),
                App.getMainController().getCurrentUser(),
                LocalDate.now(),
                LocalTime.now(),
                dialog.getTxtSintomas().getText().trim(),
                diagnostico,
                dialog.getTxtProcedimientos().getText().trim(),
                dialog.getTxtTratamiento().getText().trim(),
                dialog.getTxtOrdenes().getText().trim(),
                "" // Observaciones adicionales
        );
        
        // Guardar el registro
        medicalRecordService.createMedicalRecord(record);
        
        // Cambiar el estado de la cita a ATENDIDA
        appointment.setEstado(Estado.ATENDIDA);
        appointmentService.updateAppointment(appointment);
        
        JOptionPane.showMessageDialog(dialog,
                "Atención médica registrada correctamente\nCita marcada como ATENDIDA",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        
        dialog.dispose();
    }
}
