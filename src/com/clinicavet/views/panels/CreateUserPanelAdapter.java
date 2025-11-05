package com.clinicavet.views.panels; 

import com.clinicavet.controllers.MainController; 
import javax.swing.*; import java.awt.*;

public class CreateUserPanelAdapter extends JPanel { 

    private final MainController controller; 
    private JTextField txtName, txtEmail; 
    private JPasswordField txtPassword; 
    private JComboBox<String> cmbRoles; 

    public CreateUserPanelAdapter(MainController controller) { 
        this.controller = controller; 
        initUI();
    } 
     
     private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        main.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        main.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        main.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        main.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        main.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        main.add(txtPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        main.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        cmbRoles = new JComboBox<>();

        controller.rolService.listRoles().forEach(r -> {
            if (!"admin".equalsIgnoreCase(r.getName())) {
                cmbRoles.addItem(r.getName());
            }
        });

        main.add(cmbRoles, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton btnCreate = new JButton("Crear Usuario");
        btnCreate.setBackground(new Color(39, 174, 96));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.addActionListener(e -> onCreate());
        main.add(btnCreate, gbc);
        add(main, BorderLayout.NORTH);
    }

    private void onCreate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRoles.getSelectedItem();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        controller.createUser(name, email, password, role);
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
}