package com.clinicavet.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Appointment {
    private UUID id;
    private LocalDate fecha;
    private LocalTime hora;
    private int duracion; // 15, 30 o 60 minutos
    private User medico;
    private Pet mascota;
    private String motivo;
    private Estado estado;

    public Appointment(LocalDate fecha, LocalTime hora, int duracion, User medico, Pet mascota, 
                      String motivo, Estado estado) {
        this.id = UUID.randomUUID();
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.medico = medico;
        this.mascota = mascota;
        this.motivo = motivo;
        this.estado = estado;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getDuracion() {
        return duracion;
    }

    public User getMedico() {
        return medico;
    }

    public Pet getMascota() {
        return mascota;
    }

    public String getMotivo() {
        return motivo;
    }

    public Estado getEstado() {
        return estado;
    }

    // Setters
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setMedico(User medico) {
        this.medico = medico;
    }

    public void setMascota(Pet mascota) {
        this.mascota = mascota;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
