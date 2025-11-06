package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Pet;

public interface IPetService {

    Optional<Pet> getByName(String name);
    Optional<Pet> findById(UUID id);
    List<Pet> getByOwner(String owner);
    List<Pet> getBySpecies(String species);
    List<Pet> listPets();
    void createPet(Pet pet);
    void updatePet(Pet pet);
    void deletePet(java.util.UUID id);  

}
