package com.clinicavet.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Pet;

public interface IPetRepository {
    void addPet(Pet pet);
    Optional<Pet> findById(UUID id);
    Optional<Pet> findByName(String name);
    List<Pet> findByOwner(String owner);
    List<Pet> findBySpecies(String species);
    List<Pet> findAll();
    void update(Pet pet);
    void delete(UUID id);
}