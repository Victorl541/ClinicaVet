package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Owner;

//clase interfaz para el servicio de dueños de mascotas
public interface IOwnerService {

    void createOwner(String id, String name, String phone, String address, String email);
    Optional<Owner> findById(String id);
    Optional<Owner> findByName(String name);
    Optional<Owner> findByEmail(String email);
    List<Owner> findAll();
    void updateOwner(Owner owner);
    void deactivateOwner(String id);
    void activateOwner(String id);

}