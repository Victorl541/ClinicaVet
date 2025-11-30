package com.clinicavet.model.services;

import com.clinicavet.model.entities.Payment;
import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.repositories.IPaymentRepository;
import com.clinicavet.model.repositories.IInvoiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentService implements IPaymentService {
    
    private final IPaymentRepository paymentRepository;
    private final IInvoiceRepository invoiceRepository;
    
    public PaymentService(IPaymentRepository paymentRepository, IInvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }
    
    @Override
    public boolean createPayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        
        // Validar que la factura exista
        Optional<Invoice> invoice = invoiceRepository.getById(payment.getInvoiceId());
        if (invoice.isEmpty()) {
            System.err.println("Factura no encontrada");
            return false;
        }
        
        // Validar que el monto sea positivo
        if (payment.getAmount() <= 0) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }
        
        // Validar que no exceda el total de la factura
        double totalPaid = getTotalPaidByInvoice(payment.getInvoiceId()) + payment.getAmount();
        if (totalPaid > invoice.get().getTotal()) {
            System.err.println("El pago excede el total de la factura");
            return false;
        }
        
        // Validar método de pago
        if (payment.getPaymentMethod() == null) {
            System.err.println("Debe seleccionar un método de pago");
            return false;
        }
        
        boolean created = paymentRepository.create(payment);
        
        if (created) {
            // Actualizar estado de la factura
            updateInvoiceStatusBasedOnPayments(payment.getInvoiceId());
        }
        
        return created;
    }
    
    @Override
    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.getById(id);
    }
    
    @Override
    public List<Payment> listPayments() {
        return paymentRepository.listAll();
    }
    
    @Override
    public List<Payment> findByInvoiceId(UUID invoiceId) {
        if (invoiceId == null) {
            return List.of();
        }
        
        return paymentRepository.findByInvoiceId(invoiceId);
    }
    
    @Override
    public boolean deletePayment(UUID id) {
        Optional<Payment> payment = paymentRepository.getById(id);
        if (payment.isEmpty()) {
            return false;
        }
        
        boolean deleted = paymentRepository.delete(id);
        
        if (deleted) {
            // Actualizar estado de la factura
            updateInvoiceStatusBasedOnPayments(payment.get().getInvoiceId());
        }
        
        return deleted;
    }
    
    @Override
    public double getTotalPaidByInvoice(UUID invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
    
    @Override
    public double getRemainingAmount(UUID invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.getById(invoiceId);
        if (invoice.isEmpty()) {
            return 0;
        }
        
        double totalPaid = getTotalPaidByInvoice(invoiceId);
        return invoice.get().getTotal() - totalPaid;
    }
    
    @Override
    public Invoice.InvoiceStatus updateInvoiceStatusBasedOnPayments(UUID invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.getById(invoiceId);
        if (invoice.isEmpty()) {
            return null;
        }
        
        double totalPaid = getTotalPaidByInvoice(invoiceId);
        double total = invoice.get().getTotal();
        
        Invoice.InvoiceStatus newStatus;
        
        if (totalPaid == 0) {
            newStatus = Invoice.InvoiceStatus.PENDIENTE;
        } else if (totalPaid >= total) {
            newStatus = Invoice.InvoiceStatus.PAGADA;
        } else {
            newStatus = Invoice.InvoiceStatus.PARCIAL;
        }
        
        // Solo actualizar si el estado cambió
        if (!invoice.get().getStatus().equals(newStatus)) {
            invoice.get().setStatus(newStatus);
            invoiceRepository.update(invoice.get());
        }
        
        return newStatus;
    }
    
    @Override
    public void savePayments() {
        paymentRepository.save();
    }
}