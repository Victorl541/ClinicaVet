package com.clinicavet.model.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.clinicavet.model.entities.Pet;

public class PetRepository implements IPetRepository {

    private List<Pet> pets = new ArrayList<>();

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


}
