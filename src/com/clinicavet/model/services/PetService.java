package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.repositories.IPetRepository;

public class PetService implements IPetService {

    private IPetRepository petRepository ;

    public PetService(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Optional<Pet> getByName(String name) {
        return petRepository.findByName(name);
    }

    @Override
    public List<Pet> getByOwner(String owner) {
        return petRepository.findByOwner(owner);
    }

    @Override
    public List<Pet> getBySpecies(String species) {
        return petRepository.findBySpecies(species);
    }

    @Override
    public List<Pet> listPets() {
        return petRepository.findAll();
    }

    @Override
    public Optional<Pet> findById(UUID id) {
        return petRepository.findById(id);
    }

    @Override
    public void createPet(Pet pet) {
        petRepository.addPet(pet);
    }

    @Override
    public void updatePet(Pet pet) {
        petRepository.update(pet);
    }

    @Override
    public void deletePet(UUID id) {
        petRepository.delete(id);
    }

}
