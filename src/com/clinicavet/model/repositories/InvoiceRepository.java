package com.clinicavet.model.repositories;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.InvoiceItem;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.util.JsonHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InvoiceRepository implements IInvoiceRepository {
    
    private List<Invoice> invoices;
    private OwnerRepository ownerRepository;
    private final String FILE_NAME = "invoices.json";
    
    public InvoiceRepository() {
        this.invoices = new ArrayList<>();
    }
    
    public void setOwnerRepository(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }
    
    @Override
    public boolean create(Invoice invoice) {
        if (invoice == null) {
            return false;
        }
        return invoices.add(invoice);
    }
    
    @Override
    public Optional<Invoice> getById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return invoices.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Invoice> listAll() {
        return new ArrayList<>(invoices);
    }
    
    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            return false;
        }
        return invoices.removeIf(i -> i.getId().equals(id));
    }
    
    @Override
    public void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                sb.append("  {\n");
                sb.append("    \"id\": ").append(JsonHelper.escapeJson(invoice.getId().toString())).append(",\n");
                sb.append("    \"invoiceNumber\": ").append(JsonHelper.escapeJson(invoice.getInvoiceNumber())).append(",\n");
                sb.append("    \"invoiceDate\": ").append(JsonHelper.escapeJson(invoice.getInvoiceDate().format(DateTimeFormatter.ISO_LOCAL_DATE))).append(",\n");
                sb.append("    \"clientId\": ").append(JsonHelper.escapeJson(invoice.getClient().getId().toString())).append(",\n");
                sb.append("    \"items\": [\n");
                
                for (int j = 0; j < invoice.getItems().size(); j++) {
                    InvoiceItem item = invoice.getItems().get(j);
                    sb.append("      {\n");
                    sb.append("        \"id\": ").append(JsonHelper.escapeJson(item.getId().toString())).append(",\n");
                    sb.append("        \"description\": ").append(JsonHelper.escapeJson(item.getDescription())).append(",\n");
                    sb.append("        \"quantity\": ").append(JsonHelper.toJson(item.getQuantity())).append(",\n");
                    sb.append("        \"unitPrice\": ").append(JsonHelper.toJson(item.getUnitPrice())).append(",\n");
                    sb.append("        \"category\": ").append(JsonHelper.escapeJson(item.getCategory())).append("\n");
                    sb.append("      }");
                    if (j < invoice.getItems().size() - 1) sb.append(",");
                    sb.append("\n");
                }
                
                sb.append("    ],\n");
                sb.append("    \"notes\": ").append(JsonHelper.escapeJson(invoice.getNotes() != null ? invoice.getNotes() : "")).append(",\n");
                sb.append("    \"status\": ").append(JsonHelper.escapeJson(invoice.getStatus().toString())).append(",\n");
                sb.append("    \"subtotal\": ").append(JsonHelper.toJson(invoice.getSubtotal())).append(",\n");
                sb.append("    \"tax\": ").append(JsonHelper.toJson(invoice.getTax())).append(",\n");
                sb.append("    \"total\": ").append(JsonHelper.toJson(invoice.getTotal())).append(",\n");
                sb.append("    \"totalPaid\": ").append(JsonHelper.toJson(invoice.getTotalPaid())).append("\n");
                sb.append("  }");
                if (i < invoices.size() - 1) sb.append(",");
                sb.append("\n");
            }
            
            sb.append("]\n");
            JsonHelper.writeJsonFile(FILE_NAME, sb.toString());
            System.out.println("[InvoiceRepository] Facturas guardadas: " + invoices.size());
        } catch (Exception ex) {
            System.err.println("[InvoiceRepository] Error al guardar facturas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public void load() {
        try {
            System.out.println("[InvoiceRepository] Iniciando carga de facturas...");
            
            String content = JsonHelper.readJsonFile(FILE_NAME);
            
            if (content == null || content.trim().isEmpty() || content.trim().equals("[]")) {
                System.out.println("ℹ️ [InvoiceRepository] No hay facturas guardadas");
                invoices.clear();
                return;
            }
            
            invoices.clear();
            
            content = content.trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();
            }
            
            if (content.isEmpty()) {
                System.out.println("ℹ️ [InvoiceRepository] Array vacío");
                return;
            }
            
            String[] invoiceStrings = splitByObject(content);
            System.out.println("[InvoiceRepository] Encontrados " + invoiceStrings.length + " facturas");
            
            for (String invoiceStr : invoiceStrings) {
                try {
                    Invoice invoice = parseInvoiceJson(invoiceStr);
                    if (invoice != null) {
                        invoices.add(invoice);
                        System.out.println("✓ Factura cargada: " + invoice.getInvoiceNumber());
                    }
                } catch (Exception ex) {
                    System.err.println("Error parsing factura: " + ex.getMessage());
                }
            }
            
            System.out.println("[InvoiceRepository] Total cargado: " + invoices.size());
        } catch (Exception ex) {
            System.err.println("Error al cargar facturas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private Invoice parseInvoiceJson(String jsonStr) {
        try {
            String id = extractJsonValue(jsonStr, "id");
            String invoiceNumber = extractJsonValue(jsonStr, "invoiceNumber");
            String invoiceDateStr = extractJsonValue(jsonStr, "invoiceDate");
            String clientId = extractJsonValue(jsonStr, "clientId");
            String notes = extractJsonValue(jsonStr, "notes");
            String statusStr = extractJsonValue(jsonStr, "status");
            String subtotalStr = extractJsonValue(jsonStr, "subtotal");
            String taxStr = extractJsonValue(jsonStr, "tax");
            String totalStr = extractJsonValue(jsonStr, "total");
            String totalPaidStr = extractJsonValue(jsonStr, "totalPaid");
            
            if (id.isEmpty() || invoiceNumber.isEmpty() || clientId.isEmpty()) {
                return null;
            }
            
            Owner client = null;
            if (ownerRepository != null) {
                Optional<Owner> owner = ownerRepository.findById(clientId);
                if (owner.isPresent()) {
                    client = owner.get();
                } else {
                    System.err.println("Cliente no encontrado: " + clientId);
                    return null;
                }
            }
            
            Invoice invoice = new Invoice();
            invoice.setId(UUID.fromString(id));
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setInvoiceDate(LocalDate.parse(invoiceDateStr, DateTimeFormatter.ISO_LOCAL_DATE));
            invoice.setClient(client);
            invoice.setNotes(JsonHelper.unescapeJson(notes));
            invoice.setStatus(Invoice.InvoiceStatus.valueOf(statusStr));
            invoice.setSubtotal(Double.parseDouble(subtotalStr));
            invoice.setTax(Double.parseDouble(taxStr));
            invoice.setTotal(Double.parseDouble(totalStr));
            
            // Cargar totalPaid si existe
            if (!totalPaidStr.isEmpty()) {
                invoice.setTotalPaid(Double.parseDouble(totalPaidStr));
            } else {
                invoice.setTotalPaid(0.0);
            }
            
            List<InvoiceItem> items = parseItems(jsonStr);
            invoice.setItems(items);
            
            return invoice;
        } catch (Exception ex) {
            System.err.println("Error parsing invoice: " + ex.getMessage());
            return null;
        }
    }
    
    private List<InvoiceItem> parseItems(String jsonStr) {
        List<InvoiceItem> items = new ArrayList<>();
        
        try {
            int itemsStart = jsonStr.indexOf("\"items\"");
            if (itemsStart == -1) return items;
            
            int arrayStart = jsonStr.indexOf("[", itemsStart);
            int arrayEnd = findMatchingBracket(jsonStr, arrayStart);
            
            if (arrayStart == -1 || arrayEnd == -1) return items;
            
            String itemsArray = jsonStr.substring(arrayStart + 1, arrayEnd);
            String[] itemStrings = splitByObject(itemsArray);
            
            for (String itemStr : itemStrings) {
                try {
                    InvoiceItem item = new InvoiceItem();
                    item.setId(UUID.fromString(extractJsonValue(itemStr, "id")));
                    item.setDescription(JsonHelper.unescapeJson(extractJsonValue(itemStr, "description")));
                    item.setQuantity(Integer.parseInt(extractJsonValue(itemStr, "quantity")));
                    item.setUnitPrice(Double.parseDouble(extractJsonValue(itemStr, "unitPrice")));
                    item.setCategory(extractJsonValue(itemStr, "category"));
                    items.add(item);
                } catch (Exception ex) {
                    System.err.println("Error parsing item: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en parseItems: " + ex.getMessage());
        }
        
        return items;
    }
    
    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\"";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return "";
            
            int colonIndex = json.indexOf(":", keyIndex);
            if (colonIndex == -1) return "";
            
            int startIndex = colonIndex + 1;
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
                startIndex++;
            }
            
            if (startIndex >= json.length()) return "";
            
            char firstChar = json.charAt(startIndex);
            
            if (firstChar == '"') {
                int endIndex = json.indexOf('"', startIndex + 1);
                if (endIndex == -1) return "";
                return json.substring(startIndex + 1, endIndex);
            } else {
                int endIndex = startIndex;
                while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}' && json.charAt(endIndex) != ']') {
                    endIndex++;
                }
                return json.substring(startIndex, endIndex).trim();
            }
        } catch (Exception ex) {
            return "";
        }
    }
    
    private int findMatchingBracket(String str, int openIndex) {
        int count = 1;
        for (int i = openIndex + 1; i < str.length(); i++) {
            if (str.charAt(i) == '[') count++;
            else if (str.charAt(i) == ']') count--;
            if (count == 0) return i;
        }
        return -1;
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
    
    @Override
    public boolean update(Invoice invoice) {
        if (invoice == null || invoice.getId() == null) {
            return false;
        }
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId().equals(invoice.getId())) {
                invoices.set(i, invoice);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Invoice> findByClientId(UUID clientId) {
        List<Invoice> result = new ArrayList<>();
        if (clientId == null) {
            return result;
        }
        for (Invoice invoice : invoices) {
            if (invoice.getClient() != null && invoice.getClient().getId().equals(clientId)) {
                result.add(invoice);
            }
        }
        return result;
    }
    
    @Override
    public List<Invoice> findByStatus(Invoice.InvoiceStatus status) {
        List<Invoice> result = new ArrayList<>();
        if (status == null) {
            return result;
        }
        for (Invoice invoice : invoices) {
            if (invoice.getStatus().equals(status)) {
                result.add(invoice);
            }
        }
        return result;
    }
    
    @Override
    public List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Invoice> result = new ArrayList<>();
        if (startDate == null || endDate == null) {
            return result;
        }
        for (Invoice invoice : invoices) {
            LocalDate invoiceDate = invoice.getInvoiceDate();
            if ((invoiceDate.isEqual(startDate) || invoiceDate.isAfter(startDate)) &&
                (invoiceDate.isEqual(endDate) || invoiceDate.isBefore(endDate))) {
                result.add(invoice);
            }
        }
        return result;
    }
    
    @Override
    public List<Invoice> findByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Invoice.InvoiceStatus status) {
        List<Invoice> result = new ArrayList<>();
        if (startDate == null || endDate == null || status == null) {
            return result;
        }
        for (Invoice invoice : invoices) {
            LocalDate invoiceDate = invoice.getInvoiceDate();
            if (((invoiceDate.isEqual(startDate) || invoiceDate.isAfter(startDate)) &&
                (invoiceDate.isEqual(endDate) || invoiceDate.isBefore(endDate))) &&
                invoice.getStatus().equals(status)) {
                result.add(invoice);
            }
        }
        return result;
    }
    
    @Override
    public String generateInvoiceNumber() {
        int nextNumber = invoices.size() + 1;
        return "INV-" + String.format("%05d", nextNumber);
    }
}