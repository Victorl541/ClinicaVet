package com.clinicavet.model.services;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.IAppointmentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AppointmentService implements IAppointmentService {

    private IAppointmentRepository appointmentRepository;

    public AppointmentService(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean createAppointment(Appointment appointment) {
        // Validar solapamiento de citas
        if (appointmentRepository.hasOverlap(appointment.getMedico(), appointment.getFecha(), 
                                            appointment.getHora(), appointment.getDuracion(), null)) {
            return false;
        }
        appointmentRepository.addAppointment(appointment);
        return true;
    }

    @Override
    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByMedico(User medico) {
        return appointmentRepository.findByMedico(medico);
    }

    @Override
    public List<Appointment> getAppointmentsByMascota(Pet mascota) {
        return appointmentRepository.findByMascota(mascota);
    }

    @Override
    public List<Appointment> getAppointmentsByFecha(LocalDate fecha) {
        return appointmentRepository.findByFecha(fecha);
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        // Validar solapamiento excluyendo la cita actual
        if (appointmentRepository.hasOverlap(appointment.getMedico(), appointment.getFecha(), 
                                            appointment.getHora(), appointment.getDuracion(), 
                                            appointment.getId())) {
            return false;
        }
        appointmentRepository.update(appointment);
        return true;
    }

    @Override
    public void deleteAppointment(UUID id) {
        appointmentRepository.delete(id);
    }
}
