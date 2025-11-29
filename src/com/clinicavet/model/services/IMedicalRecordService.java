package com.clinicavet.model.services;

import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMedicalRecordService {
    void createMedicalRecord(MedicalRecord record);
    List<MedicalRecord> listMedicalRecords();
    Optional<MedicalRecord> getMedicalRecordById(UUID id);
    List<MedicalRecord> getMedicalRecordsByMascota(Pet mascota);
    List<MedicalRecord> getMedicalRecordsByMedico(User medico);
    Optional<MedicalRecord> getMedicalRecordByAppointmentId(UUID appointmentId);
    void updateMedicalRecord(MedicalRecord record);
    void deleteMedicalRecord(UUID id);
}
