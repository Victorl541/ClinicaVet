package com.clinicavet.model.entities;

import java.io.Serializable;
import java.util.UUID;

public class InvoiceItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String description;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    private String category; // SERVICIO, MEDICAMENTO, PRODUCTO
    
    public InvoiceItem() {
        this.id = UUID.randomUUID();
    }
    
    public InvoiceItem(String description, int quantity, double unitPrice, String category) {
        this();
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.category = category;
        calculateSubtotal();
    }
    
    public void calculateSubtotal() {
        this.subtotal = quantity * unitPrice;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}