package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMedicalRecordRepository {
    void addMedicalRecord(MedicalRecord record);
    List<MedicalRecord> findAll();
    Optional<MedicalRecord> findById(UUID id);
    List<MedicalRecord> findByMascota(Pet mascota);
    List<MedicalRecord> findByMedico(User medico);
    Optional<MedicalRecord> findByAppointmentId(UUID appointmentId);
    void update(MedicalRecord record);
    void delete(UUID id);
}
