# 🐶 ClínicaVet - Aplicación de Escritorio en Java

Aplicación de escritorio desarrollada en **Java** para la gestión de una **Clínica Veterinaria**.  
Permite administrar usuarios, roles, dueños, mascotas y **agenda de citas**, facilitando la organización y control de la información interna del centro veterinario.

---

## Características principales

- 🧩 **Arquitectura modular** basada en paquetes (`controllers`, `model`, `repositories`, `services`, `views`).
- 👩‍⚕️ **Gestión de usuarios y roles:** permite crear, editar y deshabilitar usuarios según su rol (ADMIN, MEDICO, AUXILIAR).
- **Gestión de dueños:** registro completo de propietarios de mascotas con cédula, contacto y dirección.
- **Gestión de mascotas:** registro de mascotas con historial médico, vacunas, alergias y notas.
- **Agenda de citas (RF5):** 
  - Programación de citas médicas con fecha, hora y duración (15, 30 o 60 minutos)
  - Asignación de médico y mascota
  - Estados de cita: PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
  - Validación automática de solapamiento de horarios para cada médico
  - Campos de motivo y observaciones para seguimiento
- 🖥️ **Interfaz gráfica (GUI)** construida en Java Swing con FlatLaf para un diseño moderno.
- **Persistencia de datos en JSON** sin dependencias externas (serialización manual).
- **Compatible con Visual Studio Code** y otros IDEs Java.
