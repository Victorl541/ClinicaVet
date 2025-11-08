package com.clinicavet.model.services;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAppointmentService {
    boolean createAppointment(Appointment appointment);
    List<Appointment> listAppointments();
    Optional<Appointment> getAppointmentById(UUID id);
    List<Appointment> getAppointmentsByMedico(User medico);
    List<Appointment> getAppointmentsByMascota(Pet mascota);
    List<Appointment> getAppointmentsByFecha(LocalDate fecha);
    boolean updateAppointment(Appointment appointment);
    void deleteAppointment(UUID id);
}
