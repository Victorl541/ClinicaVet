package com.clinicavet.model.services;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.Payment;
import com.clinicavet.model.repositories.IAppointmentRepository;
import com.clinicavet.model.repositories.IInvoiceRepository;
import com.clinicavet.model.repositories.IMedicalRecordRepository;
import com.clinicavet.model.repositories.IPaymentRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de generación de reportes estadísticos.
 */
public class ReportService implements IReportService {
    
    private IAppointmentRepository appointmentRepository;
    private IMedicalRecordRepository medicalRecordRepository;
    private IInvoiceRepository invoiceRepository;
    private IPaymentRepository paymentRepository;
    
    public ReportService(IAppointmentRepository appointmentRepository, 
                        IMedicalRecordRepository medicalRecordRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }
    
    public ReportService(IAppointmentRepository appointmentRepository, 
                        IMedicalRecordRepository medicalRecordRepository,
                        IInvoiceRepository invoiceRepository,
                        IPaymentRepository paymentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }
    
    public void setInvoiceRepository(IInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
    
    public void setPaymentRepository(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    @Override
    public Map<String, Map<String, Integer>> getAppointmentsByDoctorAndStatus() {
        List<Appointment> appointments = appointmentRepository.findAll();
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        
        for (Appointment apt : appointments) {
            if (apt.getMedico() == null) continue;
            
            String doctorName = apt.getMedico().getName();
            String status = apt.getEstado().name();
            
            result.putIfAbsent(doctorName, new LinkedHashMap<>());
            Map<String, Integer> statusMap = result.get(doctorName);
            statusMap.put(status, statusMap.getOrDefault(status, 0) + 1);
        }
        
        return result;
    }
    
    @Override
    public List<Object[]> getTopConsultationReasons(int limit) {
        List<Appointment> appointments = appointmentRepository.findAll();
        
        // Agrupar por motivo y especie
        Map<String, Map<String, Integer>> reasonSpeciesCount = new HashMap<>();
        
        for (Appointment apt : appointments) {
            if (apt.getMotivo() == null || apt.getMotivo().isEmpty()) continue;
            if (apt.getMascota() == null) continue;
            
            String motivo = apt.getMotivo().toLowerCase().trim();
            String especie = apt.getMascota().getSpecies();
            
            reasonSpeciesCount.putIfAbsent(motivo, new HashMap<>());
            Map<String, Integer> speciesMap = reasonSpeciesCount.get(motivo);
            speciesMap.put(especie, speciesMap.getOrDefault(especie, 0) + 1);
        }
        
        // Convertir a lista de [motivo, especie, cantidad] y ordenar
        List<Object[]> results = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Integer>> entry : reasonSpeciesCount.entrySet()) {
            String motivo = entry.getKey();
            
            for (Map.Entry<String, Integer> speciesEntry : entry.getValue().entrySet()) {
                String especie = speciesEntry.getKey();
                Integer count = speciesEntry.getValue();
                results.add(new Object[]{motivo, especie, count});
            }
        }
        
        // Ordenar por cantidad descendente
        results.sort((a, b) -> Integer.compare((Integer)b[2], (Integer)a[2]));
        
        // Limitar resultados
        return results.stream().limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getIncomeByPeriod(LocalDate startDate, LocalDate endDate) {
        // Si no hay repositorios de facturación, retornar datos vacíos
        if (paymentRepository == null || invoiceRepository == null) {
            Map<String, Object> emptyResult = new LinkedHashMap<>();
            emptyResult.put("totalPayments", 0);
            emptyResult.put("totalIncome", 0.0);
            emptyResult.put("paymentDetails", new ArrayList<>());
            emptyResult.put("startDate", startDate);
            emptyResult.put("endDate", endDate);
            emptyResult.put("message", "Sistema de facturación no disponible");
            return emptyResult;
        }
        
        // Obtener todos los pagos
        List<Payment> allPayments = paymentRepository.listAll();
        
        // Si startDate y endDate son null, mostrar TODOS los pagos
        List<Payment> paymentsToShow;
        if (startDate == null && endDate == null) {
            paymentsToShow = allPayments;
        } else {
            // Filtrar por rango de fechas
            paymentsToShow = allPayments.stream()
                .filter(payment -> {
                    if (startDate == null || endDate == null) return true;
                    LocalDate paymentDate = payment.getPaymentDate().toLocalDate();
                    return !paymentDate.isBefore(startDate) && !paymentDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
        }
        
        double totalIncome = 0.0;
        // Lista de detalles: [cliente, numeroFactura, metodoPago, fechaPago, monto]
        List<Object[]> paymentDetails = new ArrayList<>();
        
        for (Payment payment : paymentsToShow) {
            double amount = payment.getAmount();
            totalIncome += amount;
            
            // Obtener información de la factura asociada
            Optional<Invoice> invoiceOpt = invoiceRepository.getById(payment.getInvoiceId());
            String clientName = "Cliente no encontrado";
            String invoiceNumber = "N/A";
            
            if (invoiceOpt.isPresent()) {
                Invoice invoice = invoiceOpt.get();
                if (invoice.getClient() != null) {
                    clientName = invoice.getClient().getName();
                }
                invoiceNumber = invoice.getInvoiceNumber();
            }
            
            String paymentMethod = payment.getPaymentMethod().getDisplayName();
            String paymentDate = payment.getPaymentDate().toLocalDate().toString();
            
            // Agregar fila: [cliente, factura, método, fecha, monto]
            paymentDetails.add(new Object[]{
                clientName,
                invoiceNumber,
                paymentMethod,
                paymentDate,
                amount
            });
        }
        
        // Ordenar por fecha de pago descendente (más reciente primero)
        paymentDetails.sort((a, b) -> ((String)b[3]).compareTo((String)a[3]));
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalPayments", paymentsToShow.size());
        result.put("totalIncome", totalIncome);
        result.put("paymentDetails", paymentDetails);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("filtered", startDate != null && endDate != null);
        
        return result;
    }
}
