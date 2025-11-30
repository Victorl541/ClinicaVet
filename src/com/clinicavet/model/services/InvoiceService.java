package com.clinicavet.model.services;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.InvoiceItem;
import com.clinicavet.model.repositories.IInvoiceRepository;
import com.clinicavet.model.repositories.IPaymentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvoiceService implements IInvoiceService {
    
    private final IInvoiceRepository invoiceRepository;
    private final IPaymentRepository paymentRepository;
    
    public InvoiceService(IInvoiceRepository invoiceRepository, IPaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }
    
    @Override
    public boolean createInvoice(Invoice invoice) {
        if (invoice == null) {
            return false;
        }
        
        // Validar que tenga al menos un ítem
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            System.err.println("No se puede crear factura sin ítems");
            return false;
        }
        
        // Validar que tenga cliente
        if (invoice.getClient() == null) {
            System.err.println("No se puede crear factura sin cliente");
            return false;
        }
        
        // Calcular totales
        invoice.calculateTotals();
        
        // Generar número de factura si no existe
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
            invoice.setInvoiceNumber(generateInvoiceNumber());
        }
        
        return invoiceRepository.create(invoice);
    }
    
    @Override
    public Optional<Invoice> getInvoiceById(UUID id) {
        return invoiceRepository.getById(id);
    }
    
    @Override
    public List<Invoice> listInvoices() {
        return invoiceRepository.listAll();
    }
    
    @Override
    public boolean updateInvoice(Invoice invoice) {
        if (invoice == null) {
            return false;
        }
        
        // Recalcular totales
        invoice.calculateTotals();
        
        return invoiceRepository.update(invoice);
    }
    
    @Override
    public boolean deleteInvoice(UUID id) {
        // Verificar que no tenga pagos asociados
        List<com.clinicavet.model.entities.Payment> payments = paymentRepository.findByInvoiceId(id);
        if (!payments.isEmpty()) {
            System.err.println("No se puede eliminar factura con pagos asociados");
            return false;
        }
        
        return invoiceRepository.delete(id);
    }
    
    @Override
    public List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return List.of();
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return invoiceRepository.findByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Invoice> findByStatus(Invoice.InvoiceStatus status) {
        if (status == null) {
            return List.of();
        }
        
        return invoiceRepository.findByStatus(status);
    }
    
    @Override
    public List<Invoice> findByClientId(UUID clientId) {
        if (clientId == null) {
            return List.of();
        }
        
        return invoiceRepository.findByClientId(clientId);
    }
    
    @Override
    public List<Invoice> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Invoice.InvoiceStatus status) {
        if (startDate == null || endDate == null || status == null) {
            return List.of();
        }
        
        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        
        return invoiceRepository.findByDateRangeAndStatus(startDate, endDate, status);
    }
    
    @Override
    public String generateInvoiceNumber() {
        return invoiceRepository.generateInvoiceNumber();
    }
    
    @Override
    public void addItemToInvoice(UUID invoiceId, InvoiceItem item) {
        Optional<Invoice> invoice = invoiceRepository.getById(invoiceId);
        if (invoice.isPresent() && item != null) {
            item.calculateSubtotal();
            invoice.get().addItem(item);
            invoiceRepository.update(invoice.get());
        }
    }
    
    @Override
    public void removeItemFromInvoice(UUID invoiceId, int itemIndex) {
        Optional<Invoice> invoice = invoiceRepository.getById(invoiceId);
        if (invoice.isPresent()) {
            invoice.get().removeItem(itemIndex);
            invoiceRepository.update(invoice.get());
        }
    }
    
    @Override
    public void saveInvoices() {
        invoiceRepository.save();
    }
}