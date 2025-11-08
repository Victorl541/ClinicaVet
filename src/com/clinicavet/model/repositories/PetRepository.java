package com.clinicavet.model.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.util.JsonHelper;
import java.io.IOException;

public class PetRepository implements IPetRepository {

    private static final String FILE_NAME = "pets.json";
    private List<Pet> pets = new ArrayList<>();
    private IOwnerRepository ownerRepository;
    
    public void setOwnerRepository(IOwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void addPet(Pet pet) {
        pets.add(pet);
    }   

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(pets);
    }

    @Override
    public Optional<Pet> findByName(String name) {
        return pets.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<Pet> findByOwner(String owner) {
        return pets.stream().filter(p -> p.getOwner().getName().equalsIgnoreCase(owner)).collect(Collectors.toList());
    }

    @Override
    public List<Pet> findBySpecies(String species) {
        return pets.stream().filter(p -> p.getSpecies().equalsIgnoreCase(species)).collect(Collectors.toList());
    }

    @Override
    public void update(Pet pet) {
        findByName(pet.getName()).ifPresent(existing -> {
            existing.setAge(pet.getAge());
            existing.setName(pet.getName()); 
            existing.setSex(pet.getSex()); 
            existing.setSpecies(pet.getSpecies());
            existing.setBreed(pet.getBreed());
            existing.setAllergies(pet.getAllergies());
            existing.setMedicalNotes(pet.getMedicalNotes());
            existing.setWeight(pet.getWeight());
            existing.setOwner(pet.getOwner());
        });
    }

    @Override
    public Optional<Pet> findById(UUID id) {
        return pets.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(UUID id) {
        pets.removeIf(p -> p.getId().equals(id));
    }
    
    public void save() {
        try {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < pets.size(); i++) {
                Pet p = pets.get(i);
                json.append("  {\"id\":").append(JsonHelper.escapeJson(p.getId().toString()))
                    .append(",\"name\":").append(JsonHelper.escapeJson(p.getName()))
                    .append(",\"species\":").append(JsonHelper.escapeJson(p.getSpecies()))
                    .append(",\"breed\":").append(JsonHelper.escapeJson(p.getBreed()))
                    .append(",\"sex\":").append(JsonHelper.escapeJson(p.getSex()))
                    .append(",\"age\":").append(p.getAge())
                    .append(",\"weight\":").append(p.getWeight())
                    .append(",\"ownerId\":").append(JsonHelper.escapeJson(p.getOwner().getId()))
                    .append(",\"allergies\":").append(JsonHelper.escapeJson(p.getAllergies()))
                    .append(",\"vaccines\":").append(JsonHelper.escapeJson(p.getVaccines()))
                    .append(",\"medicalNotes\":").append(JsonHelper.escapeJson(p.getMedicalNotes()))
                    .append("}");
                if (i < pets.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");
            JsonHelper.writeJsonFile(FILE_NAME, json.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar mascotas: " + e.getMessage());
        }
    }
    
    public void load() {
        try {
            String content = JsonHelper.readJsonFile(FILE_NAME);
            if (content == null) return;
            
            pets.clear();
            String[] items = content.replace("[", "").replace("]", "").split("\\},");
            
            for (String item : items) {
                item = item.trim();
                if (item.isEmpty()) continue;
                if (!item.endsWith("}")) item += "}";
                
                String idStr = extractString(item, "id");
                UUID id = UUID.fromString(idStr);
                String name = extractString(item, "name");
                String species = extractString(item, "species");
                String breed = extractString(item, "breed");
                String sex = extractString(item, "sex");
                int age = extractInt(item, "age");
                double weight = extractDouble(item, "weight");
                String ownerId = extractString(item, "ownerId");
                String allergies = extractString(item, "allergies");
                String vaccines = extractString(item, "vaccines");
                String medicalNotes = extractString(item, "medicalNotes");
                
                Owner owner = ownerRepository.findById(ownerId).orElse(null);
                if (owner == null) continue;
                
                Pet pet = new Pet(age, name, sex, species, breed, allergies, vaccines, medicalNotes, weight, owner);
                java.lang.reflect.Field idField = Pet.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(pet, id);
                pets.add(pet);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar mascotas: " + e.getMessage());
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
    
    private double extractDouble(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return 0.0;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Double.parseDouble(json.substring(start, end).trim());
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
