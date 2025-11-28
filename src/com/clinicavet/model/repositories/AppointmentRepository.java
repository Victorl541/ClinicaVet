package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
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

public class AppointmentRepository implements IAppointmentRepository {

    private static final String FILE_NAME = "appointments.json";
    private List<Appointment> appointments = new ArrayList<>();
    private IUserRepository userRepository;
    private IPetRepository petRepository;

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setPetRepository(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        System.out.println("Cita agregada: " + appointment.getMascota().getName() + " - " + 
                         appointment.getFecha() + " " + appointment.getHora() + " | Total: " + appointments.size());
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(appointments);
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointments.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public List<Appointment> findByMedico(User medico) {
        return appointments.stream()
            .filter(a -> a.getMedico().getId() == medico.getId())
            .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByMascota(Pet mascota) {
        return appointments.stream()
            .filter(a -> a.getMascota().getId().equals(mascota.getId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByFecha(LocalDate fecha) {
        return appointments.stream()
            .filter(a -> a.getFecha().equals(fecha))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Appointment appointment) {
        findById(appointment.getId()).ifPresent(existing -> {
            existing.setFecha(appointment.getFecha());
            existing.setHora(appointment.getHora());
            existing.setDuracion(appointment.getDuracion());
            existing.setMedico(appointment.getMedico());
            existing.setMascota(appointment.getMascota());
            existing.setMotivo(appointment.getMotivo());
            existing.setEstado(appointment.getEstado());
        });
    }

    @Override
    public void delete(UUID id) {
        appointments.removeIf(a -> a.getId().equals(id));
    }

    @Override
    public boolean hasOverlap(User medico, LocalDate fecha, LocalTime hora, int duracion, UUID excludeId) {
        LocalTime endTime = hora.plusMinutes(duracion);

        return appointments.stream()
                .filter(a -> excludeId == null || !a.getId().equals(excludeId))
                .filter(a -> a.getMedico().getId() == medico.getId())
                .filter(a -> a.getFecha().equals(fecha))
                .anyMatch(a -> {
                    LocalTime aStart = a.getHora();
                    LocalTime aEnd = a.getHora().plusMinutes(a.getDuracion());
                    
                    // Verificar solapamiento: si la nueva cita comienza antes de que termine la existente
                    // Y la nueva cita termina después de que comience la existente
                    return hora.isBefore(aEnd) && endTime.isAfter(aStart);
                });
    }

    public void save() {
        try {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < appointments.size(); i++) {
                Appointment a = appointments.get(i);
                json.append("  {\"id\":").append(JsonHelper.escapeJson(a.getId().toString()))
                    .append(",\"fecha\":").append(JsonHelper.escapeJson(a.getFecha().toString()))
                    .append(",\"hora\":").append(JsonHelper.escapeJson(a.getHora().toString()))
                    .append(",\"duracion\":").append(a.getDuracion())
                    .append(",\"medicoId\":").append(a.getMedico().getId())
                    .append(",\"mascotaId\":").append(JsonHelper.escapeJson(a.getMascota().getId().toString()))
                    .append(",\"motivo\":").append(JsonHelper.escapeJson(a.getMotivo()))
                    .append(",\"estado\":").append(JsonHelper.escapeJson(a.getEstado().name()))
                    .append("}");
                if (i < appointments.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");
            JsonHelper.writeJsonFile(FILE_NAME, json.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
        }
    }

    public void load() {
        try {
            String content = JsonHelper.readJsonFile(FILE_NAME);
            if (content == null) return;

            appointments.clear();
            String[] items = content.replace("[", "").replace("]", "").split("\\},");

            for (String item : items) {
                item = item.trim();
                if (item.isEmpty()) continue;
                if (!item.endsWith("}")) item += "}";

                String idStr = extractString(item, "id");
                UUID id = UUID.fromString(idStr);
                LocalDate fecha = LocalDate.parse(extractString(item, "fecha"));
                LocalTime hora = LocalTime.parse(extractString(item, "hora"));
                int duracion = extractInt(item, "duracion");
                int medicoId = extractInt(item, "medicoId");
                String mascotaIdStr = extractString(item, "mascotaId");
                UUID mascotaId = UUID.fromString(mascotaIdStr);
                String motivo = extractString(item, "motivo");
                Estado estado = Estado.valueOf(extractString(item, "estado"));

                User medico = userRepository.findById(medicoId).orElse(null);
                Pet mascota = petRepository.findById(mascotaId).orElse(null);

                if (medico == null || mascota == null) continue;

                Appointment appointment = new Appointment(fecha, hora, duracion, medico, mascota, 
                                                         motivo, estado);
                java.lang.reflect.Field idField = Appointment.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(appointment, id);
                appointments.add(appointment);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
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
        start = json.indexOf("\"", start + pattern.length()) + 1;
        int end = json.indexOf("\"", start);
        return JsonHelper.unescapeJson(json.substring(start - 1, end + 1));
    }
}
