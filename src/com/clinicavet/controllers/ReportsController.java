package com.clinicavet.controllers;

import com.clinicavet.model.services.IReportService;
import com.clinicavet.views.ReportsView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la vista de reportes.
 */
public class ReportsController {
    
    private ReportsView view;
    private IReportService reportService;
    
    public ReportsController(ReportsView view, IReportService reportService) {
        this.view = view;
        this.reportService = reportService;
        
        initController();
        loadDoctorStatusReport(); // Cargar reporte inicial
    }
    
    private void initController() {
        // Listener para cambio de pestañas - cargar reportes automáticamente
        view.getTabbedPane().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = view.getTabbedPane().getSelectedIndex();
                switch (selectedIndex) {
                    case 0: // Citas por médico
                        loadDoctorStatusReport();
                        break;
                    case 1: // Top motivos
                        loadTopReasonsReport();
                        break;
                    case 2: // Ingresos
                        loadAllIncomeReport(); // Cargar TODOS los pagos automáticamente
                        break;
                }
            }
        });
        
        // Listener para cambios en el spinner de top motivos
        view.getSpinnerTopLimit().addChangeListener(e -> {
            if (view.getTabbedPane().getSelectedIndex() == 1) {
                loadTopReasonsReport();
            }
        });
        
        // Listener para el botón "Filtrar por Período"
        view.getBtnFilterByPeriod().addActionListener(e -> loadFilteredIncomeReport());
    }
    
    private void loadDoctorStatusReport() {
        try {
            Map<String, Map<String, Integer>> data = reportService.getAppointmentsByDoctorAndStatus();
            
            DefaultTableModel model = view.getModelDoctorStatus();
            model.setRowCount(0);
            
            String[] estados = {"PENDIENTE", "CONFIRMADA", "CANCELADA", "ATENDIDA", "NO_ASISTIO", "REPROGRAMAR"};
            
            for (Map.Entry<String, Map<String, Integer>> entry : data.entrySet()) {
                String doctor = entry.getKey();
                Map<String, Integer> statusMap = entry.getValue();
                
                Object[] row = new Object[8]; // 1 médico + 6 estados + 1 total
                row[0] = doctor;
                
                int total = 0;
                for (int i = 0; i < estados.length; i++) {
                    int count = statusMap.getOrDefault(estados[i], 0);
                    row[i + 1] = count;
                    total += count;
                }
                row[7] = total;
                
                model.addRow(row);
            }
            
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "No hay datos de citas disponibles.", 
                    "Sin datos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar reporte: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadTopReasonsReport() {
        try {
            int limit = (Integer) view.getSpinnerTopLimit().getValue();
            List<Object[]> data = reportService.getTopConsultationReasons(limit);
            
            DefaultTableModel model = view.getModelTopReasons();
            model.setRowCount(0);
            
            int rank = 1;
            for (Object[] row : data) {
                String motivo = (String) row[0];
                String especie = (String) row[1];
                Integer cantidad = (Integer) row[2];
                
                // Capitalizar motivo
                motivo = motivo.substring(0, 1).toUpperCase() + motivo.substring(1);
                
                model.addRow(new Object[]{rank++, motivo, especie, cantidad});
            }
            
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "No hay datos de motivos de consulta disponibles.", 
                    "Sin datos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar reporte: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadAllIncomeReport() {
        try {
            // Llamar al servicio con null, null para obtener TODOS los pagos
            Map<String, Object> result = reportService.getIncomeByPeriod(null, null);
            
            int totalPayments = (Integer) result.get("totalPayments");
            double totalIncome = (Double) result.get("totalIncome");
            @SuppressWarnings("unchecked")
            List<Object[]> paymentDetails = (List<Object[]>) result.get("paymentDetails");
            
            // Actualizar etiquetas de resumen
            view.getLblTotalAppointments().setText("Total Pagos Registrados: " + totalPayments);
            view.getLblTotalIncome().setText(String.format("Ingresos Totales: $%,.0f", totalIncome));
            
            // Actualizar tabla - mostrar cada pago
            DefaultTableModel model = view.getModelIncome();
            model.setRowCount(0);
            
            // Agregar cada pago como una fila
            for (Object[] detail : paymentDetails) {
                String cliente = (String) detail[0];
                String factura = (String) detail[1];
                String metodoPago = (String) detail[2];
                String fechaPago = (String) detail[3];
                double monto = (Double) detail[4];
                
                model.addRow(new Object[]{
                    cliente,
                    factura,
                    metodoPago,
                    fechaPago,
                    String.format("$%,.0f", monto)
                });
            }
            
            if (totalPayments == 0) {
                JOptionPane.showMessageDialog(view, 
                    "No hay pagos registrados en el sistema.", 
                    "Sin datos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar reporte: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadFilteredIncomeReport() {
        try {
            // Obtener fechas desde los campos de texto
            String dateFromText = view.getDateFrom().getText().trim();
            String dateToText = view.getDateTo().getText().trim();
            
            if (dateFromText.isEmpty() || dateToText.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "Por favor ingrese ambas fechas en formato yyyy-MM-dd.", 
                    "Fechas requeridas", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate startDate;
            LocalDate endDate;
            
            try {
                startDate = LocalDate.parse(dateFromText);
                endDate = LocalDate.parse(dateToText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    "Formato de fecha inválido. Use yyyy-MM-dd (ejemplo: 2025-11-01).", 
                    "Error de formato", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(view, 
                    "La fecha inicial debe ser anterior o igual a la fecha final.", 
                    "Fechas inválidas", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Generar reporte FILTRADO
            Map<String, Object> result = reportService.getIncomeByPeriod(startDate, endDate);
            
            int totalPayments = (Integer) result.get("totalPayments");
            double totalIncome = (Double) result.get("totalIncome");
            @SuppressWarnings("unchecked")
            List<Object[]> paymentDetails = (List<Object[]>) result.get("paymentDetails");
            
            // Actualizar etiquetas de resumen
            view.getLblTotalAppointments().setText("Total Pagos Registrados: " + totalPayments + " (Período: " + startDate + " al " + endDate + ")");
            view.getLblTotalIncome().setText(String.format("Ingresos Totales: $%,.0f", totalIncome));
            
            // Actualizar tabla - mostrar cada pago
            DefaultTableModel model = view.getModelIncome();
            model.setRowCount(0);
            
            // Agregar cada pago como una fila
            for (Object[] detail : paymentDetails) {
                String cliente = (String) detail[0];
                String factura = (String) detail[1];
                String metodoPago = (String) detail[2];
                String fechaPago = (String) detail[3];
                double monto = (Double) detail[4];
                
                model.addRow(new Object[]{
                    cliente,
                    factura,
                    metodoPago,
                    fechaPago,
                    String.format("$%,.0f", monto)
                });
            }
            
            if (totalPayments == 0) {
                JOptionPane.showMessageDialog(view, 
                    "No hay pagos registrados en el período seleccionado.", 
                    "Sin datos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar reporte: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadIncomeReport() {
        try {
            // Obtener fechas desde los campos de texto
            String dateFromText = view.getDateFrom().getText().trim();
            String dateToText = view.getDateTo().getText().trim();
            
            if (dateFromText.isEmpty() || dateToText.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "Por favor ingrese ambas fechas en formato yyyy-MM-dd.", 
                    "Fechas requeridas", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate startDate;
            LocalDate endDate;
            
            try {
                startDate = LocalDate.parse(dateFromText);
                endDate = LocalDate.parse(dateToText);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    "Formato de fecha inválido. Use yyyy-MM-dd (ejemplo: 2025-11-01).", 
                    "Error de formato", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(view, 
                    "La fecha inicial debe ser anterior o igual a la fecha final.", 
                    "Fechas inválidas", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Generar reporte
            Map<String, Object> result = reportService.getIncomeByPeriod(startDate, endDate);
            
            int totalPayments = (Integer) result.get("totalPayments");
            double totalIncome = (Double) result.get("totalIncome");
            @SuppressWarnings("unchecked")
            List<Object[]> paymentDetails = (List<Object[]>) result.get("paymentDetails");
            
            // Actualizar etiquetas de resumen
            view.getLblTotalAppointments().setText("Total Pagos Registrados: " + totalPayments);
            view.getLblTotalIncome().setText(String.format("Ingresos Totales: $%,.0f", totalIncome));
            
            // Actualizar tabla - mostrar cada pago
            DefaultTableModel model = view.getModelIncome();
            model.setRowCount(0);
            
            // Agregar cada pago como una fila
            for (Object[] detail : paymentDetails) {
                String cliente = (String) detail[0];
                String factura = (String) detail[1];
                String metodoPago = (String) detail[2];
                String fechaPago = (String) detail[3];
                double monto = (Double) detail[4];
                
                model.addRow(new Object[]{
                    cliente,
                    factura,
                    metodoPago,
                    fechaPago,
                    String.format("$%,.0f", monto)
                });
            }
            
            if (totalPayments == 0) {
                JOptionPane.showMessageDialog(view, 
                    "No hay pagos registrados en el período seleccionado.", 
                    "Sin datos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al cargar reporte: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
