package com.clinicavet.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Representa una atención médica o registro de historia clínica.
 * Se crea cuando un médico atiende una cita.
 */
public class MedicalRecord {
    
    private UUID id;
    private Appointment appointment; // Cita asociada
    private Pet mascota;
    private User medico;
    private LocalDate fecha;
    private LocalTime hora;
    private String sintomas;
    private String diagnostico;
    private String procedimientos;
    private String tratamiento; // Medicación, dosis, frecuencia, duración
    private String ordenes; // Exámenes, controles
    private String observaciones;
    
    public MedicalRecord(Appointment appointment, Pet mascota, User medico, 
                        LocalDate fecha, LocalTime hora, String sintomas, 
                        String diagnostico, String procedimientos, 
                        String tratamiento, String ordenes, String observaciones) {
        this.id = UUID.randomUUID();
        this.appointment = appointment;
        this.mascota = mascota;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.procedimientos = procedimientos;
        this.tratamiento = tratamiento;
        this.ordenes = ordenes;
        this.observaciones = observaciones;
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public Pet getMascota() {
        return mascota;
    }
    
    public User getMedico() {
        return medico;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public LocalTime getHora() {
        return hora;
    }
    
    public String getSintomas() {
        return sintomas;
    }
    
    public String getDiagnostico() {
        return diagnostico;
    }
    
    public String getProcedimientos() {
        return procedimientos;
    }
    
    public String getTratamiento() {
        return tratamiento;
    }
    
    public String getOrdenes() {
        return ordenes;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    // Setters
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public void setMascota(Pet mascota) {
        this.mascota = mascota;
    }
    
    public void setMedico(User medico) {
        this.medico = medico;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }
    
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    public void setProcedimientos(String procedimientos) {
        this.procedimientos = procedimientos;
    }
    
    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
    
    public void setOrdenes(String ordenes) {
        this.ordenes = ordenes;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
