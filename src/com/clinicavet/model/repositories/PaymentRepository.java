package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Payment;
import com.clinicavet.util.JsonHelper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaymentRepository implements IPaymentRepository {
    
    private List<Payment> payments;
    private InvoiceRepository invoiceRepository;
    private final String FILE_NAME = "payments.json";
    
    public PaymentRepository() {
        this.payments = new ArrayList<>();
    }
    
    public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
    
    @Override
    public boolean create(Payment payment) {
        if (payment == null) {
            return false;
        }
        return payments.add(payment);
    }
    
    @Override
    public Optional<Payment> getById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return payments.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Payment> listAll() {
        return new ArrayList<>(payments);
    }
    
    @Override
    public List<Payment> findByInvoiceId(UUID invoiceId) {
        if (invoiceId == null) {
            return new ArrayList<>();
        }
        return payments.stream()
                .filter(p -> p.getInvoiceId().equals(invoiceId))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            return false;
        }
        return payments.removeIf(p -> p.getId().equals(id));
    }
    
    @Override
    public void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            
            for (int i = 0; i < payments.size(); i++) {
                Payment payment = payments.get(i);
                sb.append("  {\n");
                sb.append("    \"id\": ").append(JsonHelper.escapeJson(payment.getId().toString())).append(",\n");
                sb.append("    \"invoiceId\": ").append(JsonHelper.escapeJson(payment.getInvoiceId().toString())).append(",\n");
                sb.append("    \"amount\": ").append(payment.getAmount()).append(",\n");
                sb.append("    \"paymentMethod\": ").append(JsonHelper.escapeJson(payment.getPaymentMethod().toString())).append(",\n");
                sb.append("    \"paymentDate\": ").append(JsonHelper.escapeJson(payment.getPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))).append(",\n");
                sb.append("    \"reference\": ").append(JsonHelper.escapeJson(payment.getReference() != null ? payment.getReference() : "")).append(",\n");
                sb.append("    \"notes\": ").append(JsonHelper.escapeJson(payment.getNotes() != null ? payment.getNotes() : "")).append("\n");
                sb.append("  }");
                if (i < payments.size() - 1) sb.append(",");
                sb.append("\n");
            }
            
            sb.append("]\n");
            JsonHelper.writeJsonFile(FILE_NAME, sb.toString());
            System.out.println("Pagos guardados: " + payments.size());
        } catch (Exception ex) {
            System.err.println("Error al guardar pagos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public void load() {
        try {
            String content = JsonHelper.readJsonFile(FILE_NAME);
            
            // Si el archivo no existe o está vacío
            if (content == null || content.trim().isEmpty() || content.trim().equals("[]")) {
                payments.clear();
                System.out.println("ℹ️ No hay pagos guardados");
                return;
            }
            
            payments.clear();
            content = content.trim();
            
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();
            }
            
            if (content.isEmpty()) {
                System.out.println("ℹ️ No hay pagos guardados");
                return;
            }
            
            String[] paymentStrings = splitByObject(content);
            System.out.println("Cargando " + paymentStrings.length + " pagos...");
            
            for (String paymentStr : paymentStrings) {
                try {
                    Payment payment = parsePaymentJson(paymentStr);
                    if (payment != null) {
                        payments.add(payment);
                    }
                } catch (Exception ex) {
                    System.err.println("Error parsing payment: " + ex.getMessage());
                }
            }
            
            System.out.println("Pagos cargados: " + payments.size());
        } catch (Exception ex) {
            System.err.println("Error al cargar pagos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private Payment parsePaymentJson(String jsonStr) {
        try {
            UUID id = UUID.fromString(extractString(jsonStr, "id"));
            String invoiceIdStr = extractString(jsonStr, "invoiceId");
            UUID invoiceId = UUID.fromString(invoiceIdStr);
            double amount = Double.parseDouble(extractValue(jsonStr, "amount"));
            String methodStr = extractString(jsonStr, "paymentMethod");
            Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.valueOf(methodStr);
            String paymentDateStr = extractString(jsonStr, "paymentDate");
            LocalDateTime paymentDate = LocalDateTime.parse(paymentDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String reference = extractString(jsonStr, "reference");
            String notes = extractString(jsonStr, "notes");
            
            Payment payment = new Payment();
            payment.setId(id);
            payment.setInvoiceId(invoiceId);
            payment.setAmount(amount);
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentDate(paymentDate);
            payment.setReference(reference);
            payment.setNotes(notes);
            
            return payment;
        } catch (Exception ex) {
            System.err.println("Error parsing payment JSON: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "";
        
        int startIndex = keyIndex + searchKey.length();
        // Saltar espacios
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        // Encontrar el final del valor (coma o cierre de objeto)
        int endIndex = startIndex;
        while (endIndex < json.length()) {
            char c = json.charAt(endIndex);
            if (c == ',' || c == '}' || c == '\n' || c == '\r') {
                break;
            }
            endIndex++;
        }
        
        return json.substring(startIndex, endIndex).trim();
    }
    
    private String extractString(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "";
        
        int startIndex = json.indexOf("\"", keyIndex + searchKey.length());
        if (startIndex == -1) return "";
        
        int endIndex = json.indexOf("\"", startIndex + 1);
        if (endIndex == -1) return "";
        
        return json.substring(startIndex + 1, endIndex);
    }
    
    private String[] splitByObject(String json) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    objects.add(json.substring(start, i + 1));
                }
            }
        }
        
        return objects.toArray(new String[0]);
    }
}