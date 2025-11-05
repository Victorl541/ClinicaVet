package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.repositories.IOwnerRepository;

public class OwnerService implements IOwnerService {

    private IOwnerRepository ownerRepository;   

    public OwnerService(IOwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void createOwner(String id, String name, String phone, String address, String email) {
        if(id.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        Owner owner = new Owner(id, name, phone, address, email);
        ownerRepository.addOwner(owner);
    }

    @Override
    public Optional<Owner> findById(String id) {
        return ownerRepository.findById(id);
    }

    @Override
    public Optional<Owner> findByName(String name) {
        return ownerRepository.findByName(name);
    }

    @Override
    public Optional<Owner> findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public void updateOwner(Owner owner) {
        ownerRepository.update(owner);
    }

    @Override
    public void deactivateOwner(String id) {
        Optional<Owner> owner = ownerRepository.findById(id);
        if (owner.isPresent()) {
            owner.get().setActivo(false);
        }
    }

    @Override
    public void activateOwner(String id) {
        Optional<Owner> owner = ownerRepository.findById(id);
        if (owner.isPresent()) {
            owner.get().setActivo(true);
        }
    }
}