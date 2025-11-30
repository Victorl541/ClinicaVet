package com.clinicavet.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private UUID invoiceId;
    private double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private String reference; // Referencia de transferencia, número de autorización, etc.
    private String notes;
    
    public enum PaymentMethod {
        EFECTIVO("Efectivo"),
        TARJETA("Tarjeta de Crédito/Débito"),
        TRANSFERENCIA("Transferencia Bancaria");
        
        private final String displayName;
        
        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Payment() {
        this.id = UUID.randomUUID();
        this.paymentDate = LocalDateTime.now();
    }
    
    public Payment(UUID invoiceId, double amount, PaymentMethod paymentMethod) {
        this();
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}