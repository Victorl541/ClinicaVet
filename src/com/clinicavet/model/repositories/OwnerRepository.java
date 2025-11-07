package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.util.JsonHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerRepository implements IOwnerRepository {
    
    private static final String FILE_NAME = "owners.json";
    private List<Owner> owners = new ArrayList<>();

    @Override
    public void addOwner(Owner owner) {
        owners.add(owner);
        System.out.println("âœ“ DueÃ±o agregado: " + owner.getName() + " | Total: " + owners.size());
    }

    @Override
    public Optional<Owner> findById(String id) {
        return owners.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Owner> findByName(String name) {
        return owners.stream().filter(o -> o.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Optional<Owner> findByEmail(String email) {
        return owners.stream().filter(o -> o.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public List<Owner> findAll() {
        return new ArrayList<>(owners);
    }

    @Override
    public void update(Owner owner) {
        findById(owner.getId()).ifPresent(existing -> {
            existing.setName(owner.getName());
            existing.setPhone(owner.getPhone());
            existing.setAddress(owner.getAddress());
            existing.setEmail(owner.getEmail());
            existing.setActivo(owner.isActivo());
        });
    }

    @Override
    public void delete(String id) {
        owners.removeIf(o -> o.getId().equals(id));
    }
    
    /**
     * Guarda los dueños en archivo JSON.
     */
    public void save() {
        try {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < owners.size(); i++) {
                Owner o = owners.get(i);
                json.append("  {\"id\":").append(JsonHelper.escapeJson(o.getId()))
                    .append(",\"name\":").append(JsonHelper.escapeJson(o.getName()))
                    .append(",\"phone\":").append(JsonHelper.escapeJson(o.getPhone()))
                    .append(",\"address\":").append(JsonHelper.escapeJson(o.getAddress()))
                    .append(",\"email\":").append(JsonHelper.escapeJson(o.getEmail()))
                    .append(",\"activo\":").append(o.isActivo())
                    .append("}");
                if (i < owners.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");
            JsonHelper.writeJsonFile(FILE_NAME, json.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar dueños: " + e.getMessage());
        }
    }
    
    /**
     * Carga los dueños desde archivo JSON.
     */
    public void load() {
        try {
            String content = JsonHelper.readJsonFile(FILE_NAME);
            if (content == null) return;
            
            owners.clear();
            String[] items = content.replace("[", "").replace("]", "").split("\\},");
            
            for (String item : items) {
                item = item.trim();
                if (item.isEmpty()) continue;
                if (!item.endsWith("}")) item += "}";
                
                String id = extractString(item, "id");
                String name = extractString(item, "name");
                String phone = extractString(item, "phone");
                String address = extractString(item, "address");
                String email = extractString(item, "email");
                boolean activo = extractBoolean(item, "activo");
                
                Owner owner = new Owner(id, name, phone, address, email);
                owner.setActivo(activo);
                owners.add(owner);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar dueños: " + e.getMessage());
        }
    }
    
    private boolean extractBoolean(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return true;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Boolean.parseBoolean(json.substring(start, end).trim());
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
