package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Payment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepository {
    
    boolean create(Payment payment); 
    Optional<Payment> getById(UUID id);
    List<Payment> listAll();
    List<Payment> findByInvoiceId(UUID invoiceId);
    boolean delete(UUID id);
    void save();
    void load();
}