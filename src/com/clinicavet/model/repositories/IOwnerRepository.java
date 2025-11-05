package com.clinicavet.model.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Owner;

//clase interfaz para el repositorio de dueños de mascotas
public interface IOwnerRepository {

    void addOwner(Owner owner);
    Optional<Owner> findById(String id);
    List<Owner> findAll();
    Optional<Owner> findByName(String name);
    Optional<Owner> findByEmail(String email);
    void update(Owner owner);
    void delete(String id);
    
}
