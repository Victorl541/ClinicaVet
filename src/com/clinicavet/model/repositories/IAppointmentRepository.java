package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAppointmentRepository {
    void addAppointment(Appointment appointment);
    List<Appointment> findAll();
    Optional<Appointment> findById(UUID id);
    List<Appointment> findByMedico(User medico);
    List<Appointment> findByMascota(Pet mascota);
    List<Appointment> findByFecha(LocalDate fecha);
    void update(Appointment appointment);
    void delete(UUID id);
    boolean hasOverlap(User medico, LocalDate fecha, LocalTime hora, int duracion, UUID excludeId);
}
