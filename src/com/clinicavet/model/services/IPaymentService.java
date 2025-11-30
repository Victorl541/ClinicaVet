package com.clinicavet.model.services;

import com.clinicavet.model.entities.Payment;
import com.clinicavet.model.entities.Invoice;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPaymentService {
    
    boolean createPayment(Payment payment);
    Optional<Payment> getPaymentById(UUID id);
    List<Payment> listPayments();
    List<Payment> findByInvoiceId(UUID invoiceId);
    boolean deletePayment(UUID id);
    double getTotalPaidByInvoice(UUID invoiceId);
    double getRemainingAmount(UUID invoiceId);
    Invoice.InvoiceStatus updateInvoiceStatusBasedOnPayments(UUID invoiceId);
    void savePayments();
}