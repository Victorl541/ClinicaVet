package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ListAppointmentsPanelAdapter extends JPanel {

    private final MainController controller;

    public final JButton btnAdd = new JButton("Agregar");
    public final JButton btnEdit = new JButton("Editar");
    public final JButton btnCancel = new JButton("Cancelar Cita");
    public final JButton btnConfirm = new JButton("Confirmar");
    public final JButton btnComplete = new JButton("Completar");
    public final JButton btnClear = new JButton("Limpiar");
    public final JButton btnSearch = new JButton("Buscar");

    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtHora = new JTextField(8);
    private final JComboBox<String> cbDuracion = new JComboBox<>(new String[]{"15", "30", "60"});
    private final JComboBox<String> cbMedico = new JComboBox<>();
    private final JComboBox<String> cbMascota = new JComboBox<>();
    private final JTextField txtMotivo = new JTextField(20);
    private final JTextField txtSearch = new JTextField(18);
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"});
    
    // Filtros de agenda
    private final JTextField txtFechaInicio = new JTextField(10);
    private final JTextField txtFechaFin = new JTextField(10);
    private final JComboBox<String> cbFiltroMedico = new JComboBox<>();
    private final JButton btnFiltrar = new JButton("Filtrar");
    private final JButton btnLimpiarFiltro = new JButton("Ver Todas");

    public JTable table;
    public DefaultTableModel tableModel;

    private UUID selectedId = null;

    public ListAppointmentsPanelAdapter(MainController controller) {
        this.controller = controller;
        initUI();
        loadCombos();
        load();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Fecha, Hora, Duración
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        form.add(txtFecha, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("Hora (HH:MM):"), gbc);
        gbc.gridx = 3;
        form.add(txtHora, gbc);
        gbc.gridx = 4;
        form.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 5;
        form.add(cbDuracion, gbc);

        // Fila 2: Médico, Mascota
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Médico:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        form.add(cbMedico, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        form.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        form.add(cbMascota, gbc);
        gbc.gridwidth = 1;

        // Fila 3: Motivo, Estado
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        form.add(txtMotivo, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        form.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        form.add(cbEstado, gbc);
        gbc.gridwidth = 1;

        // Fila 4: Botones
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnConfirm);
        btnPanel.add(btnComplete);
        btnPanel.add(btnCancel);
        btnPanel.add(btnClear);
        form.add(btnPanel, gbc);

        // contenedor superior con altura ajustable
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(Color.WHITE);
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.setPreferredSize(new Dimension(0, 180));
        add(topContainer, BorderLayout.NORTH);

        // tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Fecha", "Hora", "Duración", "Médico", "Mascota", "Motivo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Ocultar columna ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(scroll, BorderLayout.CENTER);

        // panel de búsqueda y filtros
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Panel de filtros (izquierda)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtros de Agenda"));
        filterPanel.add(new JLabel("Desde:"));
        filterPanel.add(txtFechaInicio);
        filterPanel.add(new JLabel("Hasta:"));
        filterPanel.add(txtFechaFin);
        filterPanel.add(new JLabel("Médico:"));
        filterPanel.add(cbFiltroMedico);
        filterPanel.add(btnFiltrar);
        filterPanel.add(btnLimpiarFiltro);
        
        // Panel de búsqueda (derecha)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Buscar (mascota / médico):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        bottomPanel.add(filterPanel, BorderLayout.WEST);
        bottomPanel.add(searchPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void loadCombos() {
        cbMedico.removeAllItems();
        cbMascota.removeAllItems();
        cbFiltroMedico.removeAllItems();

        // Cargar médicos
        cbFiltroMedico.addItem("Todos los médicos");
        List<User> users = controller.listAllUsers();
        for (User u : users) {
            if (u.getRol() != null && u.getRol().getName().equals("MEDICO") && u.isActivo()) {
                cbMedico.addItem(u.getId() + " - " + u.getName());
                cbFiltroMedico.addItem(u.getId() + " - " + u.getName());
            }
        }

        // Cargar mascotas (solo nombre y dueño, sin ID)
        List<Pet> pets = controller.listAllPets();
        for (Pet p : pets) {
            cbMascota.addItem(p.getName() + " - Dueño: " + p.getOwner().getName());
        }
    }

    public void load() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = controller.listAllAppointments();
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getId().toString(),
                    a.getFecha().toString(),
                    a.getHora().toString(),
                    a.getDuracion() + " min",
                    a.getMedico().getName(),
                    a.getMascota().getName(),
                    a.getMotivo(),
                    a.getEstado().name()
            });
        }
    }

    public void reload() {
        loadCombos();
        load();
    }

    private void initListeners() {
        // selección en la tabla
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String idStr = tableModel.getValueAt(row, 0).toString();
                        selectedId = UUID.fromString(idStr);
                        Optional<Appointment> opt = controller.findAppointmentById(selectedId);
                        if (opt.isPresent()) {
                            Appointment a = opt.get();
                            txtFecha.setText(a.getFecha().toString());
                            txtHora.setText(a.getHora().toString());
                            cbDuracion.setSelectedItem(String.valueOf(a.getDuracion()));
                            
                            // Seleccionar médico
                            for (int i = 0; i < cbMedico.getItemCount(); i++) {
                                String item = cbMedico.getItemAt(i);
                                if (item.startsWith(a.getMedico().getId() + " - ")) {
                                    cbMedico.setSelectedIndex(i);
                                    break;
                                }
                            }
                            
                            // Seleccionar mascota por nombre
                            for (int i = 0; i < cbMascota.getItemCount(); i++) {
                                String item = cbMascota.getItemAt(i);
                                if (item.startsWith(a.getMascota().getName() + " - Dueño: ")) {
                                    cbMascota.setSelectedIndex(i);
                                    break;
                                }
                            }
                            
                            txtMotivo.setText(a.getMotivo());
                            cbEstado.setSelectedItem(a.getEstado().name());
                        } else {
                            selectedId = null;
                        }
                    } else {
                        selectedId = null;
                    }
                }
            }
        });

        // agregar nueva cita
        btnAdd.addActionListener(e -> {
            if (!validateFields()) return;
            
            try {
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
                LocalTime hora = LocalTime.parse(txtHora.getText().trim());
                int duracion = Integer.parseInt(cbDuracion.getSelectedItem().toString());
                int medicoId = extractIdFromCombo(cbMedico.getSelectedItem().toString());
                UUID mascotaId = findPetIdByNameAndOwner(cbMascota.getSelectedItem().toString());
                String motivo = txtMotivo.getText().trim();
                
                boolean success = controller.createAppointment(fecha, hora, duracion, medicoId, mascotaId, 
                                                              motivo, Estado.PENDIENTE);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cita creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    reload();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "El médico ya tiene una cita en ese horario", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // editar cita
        btnEdit.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!validateFields()) return;
            
            try {
                LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
                LocalTime hora = LocalTime.parse(txtHora.getText().trim());
                int duracion = Integer.parseInt(cbDuracion.getSelectedItem().toString());
                int medicoId = extractIdFromCombo(cbMedico.getSelectedItem().toString());
                UUID mascotaId = findPetIdByNameAndOwner(cbMascota.getSelectedItem().toString());
                String motivo = txtMotivo.getText().trim();
                Estado estado = Estado.valueOf(cbEstado.getSelectedItem().toString());
                
                boolean success = controller.updateAppointment(selectedId, fecha, hora, duracion, medicoId, 
                                                              mascotaId, motivo, estado);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    reload();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "El médico ya tiene una cita en ese horario", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // confirmar cita
        btnConfirm.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.changeAppointmentStatus(selectedId, Estado.CONFIRMADA);
            reload();
            clearFields();
        });

        // completar cita
        btnComplete.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.changeAppointmentStatus(selectedId, Estado.COMPLETADA);
            reload();
            clearFields();
        });

        // cancelar cita
        btnCancel.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Cancelar la cita seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.changeAppointmentStatus(selectedId, Estado.CANCELADA);
                reload();
                clearFields();
            }
        });

        // buscar
        btnSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim();
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Buscar en la tabla
            boolean found = false;
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                String mascota = tableModel.getValueAt(r, 5).toString().toLowerCase();
                String medico = tableModel.getValueAt(r, 4).toString().toLowerCase();
                if (mascota.contains(q.toLowerCase()) || medico.contains(q.toLowerCase())) {
                    table.setRowSelectionInterval(r, r);
                    Rectangle rect = table.getCellRect(r, 0, true);
                    table.scrollRectToVisible(rect);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(this, "No se encontró cita", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // limpiar formulario
        btnClear.addActionListener(e -> clearFields());
        
        // filtrar agenda
        btnFiltrar.addActionListener(e -> filtrarAgenda());
        
        // limpiar filtros y ver todas las citas
        btnLimpiarFiltro.addActionListener(e -> {
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            cbFiltroMedico.setSelectedIndex(0);
            load();
        });
        
        // Filtrado automático 
        cbFiltroMedico.addActionListener(e -> {
            if (cbFiltroMedico.getSelectedIndex() != -1) {
                filtrarAgenda();
            }
        });
        
        // Filtrado automático al cambiar las fechas 
        txtFechaInicio.addActionListener(e -> filtrarAgenda());
        txtFechaFin.addActionListener(e -> filtrarAgenda());
    }

    private boolean validateFields() {
        if (txtFecha.getText().trim().isEmpty() || txtHora.getText().trim().isEmpty() || 
            txtMotivo.getText().trim().isEmpty() || cbMedico.getSelectedItem() == null || 
            cbMascota.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private int extractIdFromCombo(String comboText) {
        return Integer.parseInt(comboText.split(" - ")[0]);
    }

    private UUID findPetIdByNameAndOwner(String comboText) {
       
        String petName = comboText.split(" - Dueño: ")[0].trim();
        List<Pet> pets = controller.listAllPets();
        for (Pet p : pets) {
            if (p.getName().equals(petName)) {
                return p.getId();
            }
        }
        throw new IllegalArgumentException("No se encontró mascota: " + petName);
    }

    private void clearFields() {
        txtFecha.setText("");
        txtHora.setText("");
        cbDuracion.setSelectedIndex(0);
        if (cbMedico.getItemCount() > 0) cbMedico.setSelectedIndex(0);
        if (cbMascota.getItemCount() > 0) cbMascota.setSelectedIndex(0);
        txtMotivo.setText("");
        cbEstado.setSelectedItem("PENDIENTE");
        txtSearch.setText("");
        table.clearSelection();
        selectedId = null;
    }
    
    private void filtrarAgenda() {
        String fechaInicioStr = txtFechaInicio.getText().trim();
        String fechaFinStr = txtFechaFin.getText().trim();
        String medicoSeleccionado = (String) cbFiltroMedico.getSelectedItem();
        
        // Si no hay filtros aplicados, mostrar todas las citas
        if (fechaInicioStr.isEmpty() && fechaFinStr.isEmpty() && 
            (medicoSeleccionado == null || medicoSeleccionado.equals("Todos los médicos"))) {
            load();
            return;
        }
        
        // Obtener todas las citas
        List<Appointment> todasLasCitas = controller.listAllAppointments();
        List<Appointment> citasFiltradas = new ArrayList<>();
        
        try {
            LocalDate fechaInicio = fechaInicioStr.isEmpty() ? null : LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = fechaFinStr.isEmpty() ? null : LocalDate.parse(fechaFinStr);
            
            for (Appointment a : todasLasCitas) {
                boolean cumpleFecha = true;
                boolean cumpleMedico = true;
                
                // Filtro por fecha
                if (fechaInicio != null && a.getFecha().isBefore(fechaInicio)) {
                    cumpleFecha = false;
                }
                if (fechaFin != null && a.getFecha().isAfter(fechaFin)) {
                    cumpleFecha = false;
                }
                
                // Filtro por médico
                if (medicoSeleccionado != null && !medicoSeleccionado.equals("Todos los médicos")) {
                    int medicoId = extractIdFromCombo(medicoSeleccionado);
                    if (a.getMedico().getId() != medicoId) {
                        cumpleMedico = false;
                    }
                }
                
                if (cumpleFecha && cumpleMedico) {
                    citasFiltradas.add(a);
                }
            }
            
            // Mostrar resultados filtrados
            tableModel.setRowCount(0);
            for (Appointment a : citasFiltradas) {
                tableModel.addRow(new Object[]{
                        a.getId().toString(),
                        a.getFecha().toString(),
                        a.getHora().toString(),
                        a.getDuracion() + " min",
                        a.getMedico().getName(),
                        a.getMascota().getName(),
                        a.getMotivo(),
                        a.getEstado().name()
                });
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error en el formato de fecha. Use YYYY-MM-DD", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
