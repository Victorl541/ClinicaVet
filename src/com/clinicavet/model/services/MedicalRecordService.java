package com.clinicavet.model.services;

import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.IMedicalRecordRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MedicalRecordService implements IMedicalRecordService {
    
    private IMedicalRecordRepository medicalRecordRepository;
    
    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }
    
    @Override
    public void createMedicalRecord(MedicalRecord record) {
        medicalRecordRepository.addMedicalRecord(record);
    }
    
    @Override
    public List<MedicalRecord> listMedicalRecords() {
        return medicalRecordRepository.findAll();
    }
    
    @Override
    public Optional<MedicalRecord> getMedicalRecordById(UUID id) {
        return medicalRecordRepository.findById(id);
    }
    
    @Override
    public List<MedicalRecord> getMedicalRecordsByMascota(Pet mascota) {
        return medicalRecordRepository.findByMascota(mascota);
    }
    
    @Override
    public List<MedicalRecord> getMedicalRecordsByMedico(User medico) {
        return medicalRecordRepository.findByMedico(medico);
    }
    
    @Override
    public Optional<MedicalRecord> getMedicalRecordByAppointmentId(UUID appointmentId) {
        return medicalRecordRepository.findByAppointmentId(appointmentId);
    }
    
    @Override
    public void updateMedicalRecord(MedicalRecord record) {
        medicalRecordRepository.update(record);
    }
    
    @Override
    public void deleteMedicalRecord(UUID id) {
        medicalRecordRepository.delete(id);
    }
}
