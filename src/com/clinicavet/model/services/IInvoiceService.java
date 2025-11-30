package com.clinicavet.model.services;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.InvoiceItem;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInvoiceService {
    
    boolean createInvoice(Invoice invoice);
    Optional<Invoice> getInvoiceById(UUID id);
    List<Invoice> listInvoices();
    boolean updateInvoice(Invoice invoice);
    boolean deleteInvoice(UUID id);
    List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
    List<Invoice> findByClientId(UUID clientId);
    List<Invoice> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Invoice.InvoiceStatus status);
    String generateInvoiceNumber();
    void addItemToInvoice(UUID invoiceId, InvoiceItem item);
    void removeItemFromInvoice(UUID invoiceId, int itemIndex);
    void saveInvoices();
}