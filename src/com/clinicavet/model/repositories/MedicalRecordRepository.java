package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.util.JsonHelper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MedicalRecordRepository implements IMedicalRecordRepository {
    
    private static final String FILE_NAME = "medical_records.json";
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    private IAppointmentRepository appointmentRepository;
    private IPetRepository petRepository;
    private IUserRepository userRepository;
    
    public void setAppointmentRepository(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    public void setPetRepository(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }
    
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
        System.out.println("Registro médico agregado: " + record.getMascota().getName() + 
                         " - " + record.getFecha() + " | Total: " + medicalRecords.size());
    }
    
    @Override
    public List<MedicalRecord> findAll() {
        return new ArrayList<>(medicalRecords);
    }
    
    @Override
    public Optional<MedicalRecord> findById(UUID id) {
        return medicalRecords.stream()
                .filter(mr -> mr.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<MedicalRecord> findByMascota(Pet mascota) {
        return medicalRecords.stream()
                .filter(mr -> mr.getMascota().getId().equals(mascota.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MedicalRecord> findByMedico(User medico) {
        return medicalRecords.stream()
                .filter(mr -> mr.getMedico().getId() == medico.getId())
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<MedicalRecord> findByAppointmentId(UUID appointmentId) {
        return medicalRecords.stream()
                .filter(mr -> mr.getAppointment() != null && 
                             mr.getAppointment().getId().equals(appointmentId))
                .findFirst();
    }
    
    @Override
    public void update(MedicalRecord record) {
        findById(record.getId()).ifPresent(existing -> {
            existing.setAppointment(record.getAppointment());
            existing.setMascota(record.getMascota());
            existing.setMedico(record.getMedico());
            existing.setFecha(record.getFecha());
            existing.setHora(record.getHora());
            existing.setSintomas(record.getSintomas());
            existing.setDiagnostico(record.getDiagnostico());
            existing.setProcedimientos(record.getProcedimientos());
            existing.setTratamiento(record.getTratamiento());
            existing.setOrdenes(record.getOrdenes());
            existing.setObservaciones(record.getObservaciones());
        });
    }
    
    @Override
    public void delete(UUID id) {
        medicalRecords.removeIf(mr -> mr.getId().equals(id));
    }
    
    public void save() {
        try {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < medicalRecords.size(); i++) {
                MedicalRecord mr = medicalRecords.get(i);
                json.append("  {\"id\":").append(JsonHelper.escapeJson(mr.getId().toString()))
                    .append(",\"appointmentId\":").append(mr.getAppointment() != null ? 
                        JsonHelper.escapeJson(mr.getAppointment().getId().toString()) : "null")
                    .append(",\"mascotaId\":").append(JsonHelper.escapeJson(mr.getMascota().getId().toString()))
                    .append(",\"medicoId\":").append(mr.getMedico().getId())
                    .append(",\"fecha\":").append(JsonHelper.escapeJson(mr.getFecha().toString()))
                    .append(",\"hora\":").append(JsonHelper.escapeJson(mr.getHora().toString()))
                    .append(",\"sintomas\":").append(JsonHelper.escapeJson(mr.getSintomas()))
                    .append(",\"diagnostico\":").append(JsonHelper.escapeJson(mr.getDiagnostico()))
                    .append(",\"procedimientos\":").append(JsonHelper.escapeJson(mr.getProcedimientos()))
                    .append(",\"tratamiento\":").append(JsonHelper.escapeJson(mr.getTratamiento()))
                    .append(",\"ordenes\":").append(JsonHelper.escapeJson(mr.getOrdenes()))
                    .append(",\"observaciones\":").append(JsonHelper.escapeJson(mr.getObservaciones()))
                    .append("}");
                if (i < medicalRecords.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");
            JsonHelper.writeJsonFile(FILE_NAME, json.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar registros médicos: " + e.getMessage());
        }
    }
    
    public void load() {
        try {
            String content = JsonHelper.readJsonFile(FILE_NAME);
            if (content == null) {
                System.out.println("⚠️ Archivo medical_records.json no encontrado o vacío");
                return;
            }
            
            System.out.println("📋 Cargando registros médicos...");
            medicalRecords.clear();
            String[] items = content.replace("[", "").replace("]", "").split("\\},");
            
            System.out.println("📊 Encontrados " + items.length + " registros en el archivo");
            
            for (String item : items) {
                item = item.trim();
                if (item.isEmpty()) continue;
                if (!item.endsWith("}")) item += "}";
                
                try {
                    String idStr = extractString(item, "id");
                    UUID id = UUID.fromString(idStr);
                    
                    String appointmentIdStr = extractString(item, "appointmentId");
                    Appointment appointment = null;
                    if (appointmentIdStr != null && !appointmentIdStr.equals("null")) {
                        appointment = appointmentRepository.findById(UUID.fromString(appointmentIdStr)).orElse(null);
                    }
                    
                    String mascotaIdStr = extractString(item, "mascotaId");
                    UUID mascotaId = UUID.fromString(mascotaIdStr);
                    Pet mascota = petRepository.findById(mascotaId).orElse(null);
                    
                    int medicoId = extractInt(item, "medicoId");
                    User medico = userRepository.findById(medicoId).orElse(null);
                    
                    LocalDate fecha = LocalDate.parse(extractString(item, "fecha"));
                    LocalTime hora = LocalTime.parse(extractString(item, "hora"));
                    String sintomas = extractString(item, "sintomas");
                    String diagnostico = extractString(item, "diagnostico");
                    String procedimientos = extractString(item, "procedimientos");
                    String tratamiento = extractString(item, "tratamiento");
                    String ordenes = extractString(item, "ordenes");
                    String observaciones = extractString(item, "observaciones");
                    
                    if (mascota == null) {
                        System.err.println("⚠️ Mascota no encontrada con ID: " + mascotaId);
                        continue;
                    }
                    if (medico == null) {
                        System.err.println("⚠️ Médico no encontrado con ID: " + medicoId);
                        continue;
                    }
                    
                    MedicalRecord record = new MedicalRecord(appointment, mascota, medico, fecha, hora,
                        sintomas, diagnostico, procedimientos, tratamiento, ordenes, observaciones);
                    
                    java.lang.reflect.Field idField = MedicalRecord.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(record, id);
                    medicalRecords.add(record);
                    System.out.println("✅ Registro cargado: " + mascota.getName() + " - " + fecha + " - " + diagnostico.substring(0, Math.min(30, diagnostico.length())) + "...");
                } catch (Exception ex) {
                    System.err.println("❌ Error al procesar registro: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.out.println("✅ Total registros médicos cargados: " + medicalRecords.size());
        } catch (Exception e) {
            System.err.println("❌ Error al cargar registros médicos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int extractInt(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return 0;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }
    
    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return null;
        
        // Buscar el valor después de los dos puntos
        int valueStart = start + pattern.length();
        String remaining = json.substring(valueStart).trim();
        
        // Si es null literal
        if (remaining.startsWith("null")) {
            return "null";
        }
        
        // Si es un string entre comillas
        if (remaining.startsWith("\"")) {
            start = json.indexOf("\"", valueStart) + 1;
            int end = json.indexOf("\"", start);
            return JsonHelper.unescapeJson(json.substring(start - 1, end + 1));
        }
        
        return null;
    }
}
