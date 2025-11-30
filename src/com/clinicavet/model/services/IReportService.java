package com.clinicavet.model.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de generación de reportes estadísticos.
 */
public interface IReportService {
    
    /**
     * Genera reporte de citas agrupadas por médico y estado.
     * @return Map<String, Map<String, Integer>> donde la clave es el nombre del médico
     *         y el valor es un mapa de estado->cantidad
     */
    Map<String, Map<String, Integer>> getAppointmentsByDoctorAndStatus();
    
    /**
     * Genera reporte de los motivos de consulta más frecuentes con sus especies.
     * @param limit Número de motivos a incluir en el top
     * @return List de arrays [motivo, especie, cantidad]
     */
    List<Object[]> getTopConsultationReasons(int limit);
    
    /**
     * Calcula ingresos totales por período basado en pagos reales.
     * Si startDate y endDate son null, retorna todos los pagos.
     * @param startDate Fecha inicial (puede ser null para mostrar todos)
     * @param endDate Fecha final (puede ser null para mostrar todos)
     * @return Map con información de todos los pagos con detalles de facturación
     */
    Map<String, Object> getIncomeByPeriod(LocalDate startDate, LocalDate endDate);
}
