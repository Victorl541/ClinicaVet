package com.clinicavet.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String invoiceNumber;
    private Owner client;
    private LocalDateTime createdAt;
    private LocalDate invoiceDate;
    private List<InvoiceItem> items;
    private double subtotal;
    private double tax;
    private double total;
    private InvoiceStatus status;
    private String notes;
    private double totalPaid;
    
    
    public enum InvoiceStatus {
        PENDIENTE,
        PAGADA,
        PARCIAL,
        ANULADA
    }
    
    public Invoice() {
        this.id = UUID.randomUUID();
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.invoiceDate = LocalDate.now();
        this.status = InvoiceStatus.PENDIENTE;
    }
    
    public Invoice(String invoiceNumber, Owner client, LocalDate invoiceDate) {
        this();
        this.invoiceNumber = invoiceNumber;
        this.client = client;
        this.invoiceDate = invoiceDate;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    /**
     * Obtener el saldo pendiente de pago
     */
    public double getRemaining() {
        return this.total - this.totalPaid;
    }
    
    public void addItem(InvoiceItem item) {
        this.items.add(item);
        calculateTotals();
    }
    
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            this.items.remove(index);
            calculateTotals();
        }
    }
    
    public void calculateTotals() {
        this.subtotal = items.stream().mapToDouble(InvoiceItem::getSubtotal).sum();
        this.tax = subtotal * 0.19; // IVA 19%
        this.total = subtotal + tax;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public Owner getClient() {
        return client;
    }
    
    public void setClient(Owner client) {
        this.client = client;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
        calculateTotals();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getTax() {
        return tax;
    }
    
    public void setTax(double tax) {
        this.tax = tax;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public InvoiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}