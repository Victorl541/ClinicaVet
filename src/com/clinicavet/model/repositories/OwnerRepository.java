package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OwnerRepository implements IOwnerRepository {
    
    private List<Owner> owners = new ArrayList<>();

    @Override
    public void addOwner(Owner owner) {
        owners.add(owner);
        System.out.println("✓ Dueño agregado: " + owner.getName() + " | Total: " + owners.size());
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
}