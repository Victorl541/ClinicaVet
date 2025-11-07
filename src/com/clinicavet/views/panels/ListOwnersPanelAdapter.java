package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.Owner;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ListOwnersPanelAdapter extends JPanel {

    private final MainController controller;

    private final JTextField txtId = new JTextField(12);
    public final JButton btnAdd = new JButton("Agregar");
    public final JButton btnEdit = new JButton("Editar");
    public final JButton btnDeactivate = new JButton("Desactivar");
    public final JButton btnSearch = new JButton("Buscar");
    public final JButton btnClear = new JButton("Limpiar");

    private final JTextField txtName = new JTextField(15);
    private final JTextField txtPhone = new JTextField(12);
    private final JTextField txtAddress = new JTextField(20);
    private final JTextField txtEmail = new JTextField(18);
    private final JTextField txtSearch = new JTextField(18);

    public JTable table;
    public DefaultTableModel tableModel;

    private String selectedId = null;

    public ListOwnersPanelAdapter(MainController controller) {
        this.controller = controller;
        initUI();
        load();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // formulario
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.setBackground(Color.WHITE);
        form.add(new JLabel("ID (Cédula):"));
        form.add(txtId);
        form.add(new JLabel("Nombre:"));
        form.add(txtName);
        form.add(new JLabel("Teléfono:"));
        form.add(txtPhone);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);
        form.add(new JLabel("Dirección:"));
        form.add(txtAddress);

        form.add(btnAdd);
        form.add(btnEdit);
        form.add(btnDeactivate);
        form.add(btnClear);

        // contenedor superior con altura fija para evitar superposición
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(Color.WHITE);
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.setPreferredSize(new Dimension(0, 120)); // ajustar si necesita más espacio
        add(topContainer, BorderLayout.NORTH);

        // tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Email", "Teléfono", "Dirección", "Activo"}, 0) {
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

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(scroll, BorderLayout.CENTER);

        // panel de búsqueda y botones de control
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Buscar (nombre / email / teléfono / id):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        add(searchPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public void load() {
        tableModel.setRowCount(0);
        List<Owner> owners = controller.listAllOwners();
        for (Owner o : owners) {
            tableModel.addRow(new Object[]{
                    o.getId(),
                    o.getName(),
                    o.getEmail(),
                    o.getPhone(),
                    o.getAddress(),
                    o.isActivo() ? "Sí" : "No"
            });
        }
    }

    public void reload() {
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
                        selectedId = idStr;
                        Optional<Owner> opt = controller.findOwnerById(selectedId);
                        if (opt.isPresent()) {
                            Owner o = opt.get();
                            txtId.setText(o.getId());
                            txtId.setEditable(false); // no permitir cambiar la cédula
                            txtName.setText(o.getName());
                            txtPhone.setText(o.getPhone());
                            txtEmail.setText(o.getEmail());
                            txtAddress.setText(o.getAddress());
                        } else {
                            selectedId = null;
                        }
                    } else {
                        selectedId = null;
                    }
                }
            }
        });

        // agregar nuevo dueño
        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();
            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.createOwner(id, name, phone, address, email);
            reload();
            clearFields();
        });

        // editar (no cambia cédula)
        btnEdit.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un dueño de la tabla para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<Owner> opt = controller.findOwnerById(selectedId);
            if (opt.isPresent()) {
                Owner existing = opt.get();
                existing.setName(txtName.getText().trim());
                existing.setPhone(txtPhone.getText().trim());
                existing.setEmail(txtEmail.getText().trim());
                existing.setAddress(txtAddress.getText().trim());
                controller.updateOwner(existing);
                reload();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Dueño no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // desactivar
        btnDeactivate.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un dueño de la tabla para desactivar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desactivar dueño seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deactivateOwnerById(selectedId);
                reload();
                clearFields();
            }
        });

        // buscar y seleccionar fila
        btnSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim();
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<Owner> opt = controller.findOwnerByQuery(q);
            if (opt.isPresent()) {
                Owner o = opt.get();
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    if (tableModel.getValueAt(r, 0).toString().equals(o.getId())) {
                        table.setRowSelectionInterval(r, r);
                        Rectangle rect = table.getCellRect(r, 0, true);
                        table.scrollRectToVisible(rect);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró dueño", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // limpiar formulario
        btnClear.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        txtId.setText("");
        txtId.setEditable(true);
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        table.clearSelection();
        selectedId = null;
    }
}