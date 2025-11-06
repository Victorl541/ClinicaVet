package com.clinicavet.model.entities;


import java.util.UUID;

public class Pet {
    
    private int age;
    private UUID id;
    private String name;
    private String sex;
    private String species;
    private String breed;
    private String allergies;
    private String vaccines;
    private String medicalNotes;
    private double weight;
    private Owner owner; 

    public Pet(int age, String name, String sex, String species, String breed, String allergies, String vaccines, String medicalNotes, double weight, Owner owner) {
        this.age = age;
        this.id = UUID.randomUUID(); 
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.breed = breed;
        this.allergies = allergies;
        this.vaccines = vaccines;
        this.medicalNotes = medicalNotes;
        this.weight = weight;
        this.owner = owner;
    }

    //solo deja los setters que san logicamente modificables para una mascota

    public int getAge() {
        return age; 
    }

    public String getVaccines() {
        return vaccines;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMedicalNotes() {
        return medicalNotes;
    }

    public double getWeight() {
        return weight;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setVaccines(String vaccines) {
        this.vaccines = vaccines;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
