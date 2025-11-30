package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Invoice;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInvoiceRepository {
    
    boolean create(Invoice invoice);
    Optional<Invoice> getById(UUID id);
    List<Invoice> listAll();
    boolean update(Invoice invoice);
    boolean delete(UUID id);
    List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    List<Invoice> findByClientId(UUID clientId);
    List<Invoice> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Invoice.InvoiceStatus status);
    String generateInvoiceNumber();
    void save();
    void load();
}