# 🧪 DATOS DE PRUEBA - ClinicaVet2

## 🔐 Credenciales de Acceso

### **Administrador:**
- **Usuario:** admin
- **Contraseña:** 1234
- **Rol:** ADMIN
- **Permisos:** Acceso completo a todas las funcionalidades

### **Médicos Veterinarios:**

1. **Dr. Carlos Mendoza**
   - Email: carlos.mendoza@clinica.com
   - Contraseña: medico123
   - Rol: MEDICO

2. **Dra. Ana García**
   - Email: ana.garcia@clinica.com
   - Contraseña: medico123
   - Rol: MEDICO

3. **Dr. Luis Ramírez**
   - Email: luis.ramirez@clinica.com
   - Contraseña: medico123
   - Rol: MEDICO

### **Personal Auxiliar:**
- **María López**
  - Email: maria.lopez@clinica.com
  - Contraseña: aux123
  - Rol: AUXILIAR

---

## 👥 Dueños de Mascotas (6 registrados)

| Cédula     | Nombre            | Teléfono    | Dirección                           |
|------------|-------------------|-------------|-------------------------------------|
| 1010234567 | Juan Pérez        | 3001234567  | Calle 10 #20-30, Pereira            |
| 1020345678 | María Rodríguez   | 3112345678  | Carrera 15 #25-40, Pereira          |
| 1030456789 | Pedro Gómez       | 3223456789  | Avenida 30 de Agosto #45-20, Pereira|
| 1040567890 | Laura Martínez    | 3134567890  | Calle 18 #12-25, Dosquebradas       |
| 1050678901 | Carlos Sánchez    | 3145678901  | Carrera 7 #35-15, Pereira           |
| 1060789012 | Ana Torres        | 3156789012  | Calle 25 #18-30, La Virginia        |

---

## 🐾 Mascotas Registradas (10 mascotas)

### **Perros (5):**

1. **Max** - Golden Retriever (Macho, 3 años, 28.5 kg)
   - Dueño: Juan Pérez
   - Vacunas: Rabia, Parvovirus, Moquillo
   - Notas: Chequeo anual al día

2. **Rocky** - Bulldog Francés (Macho, 4 años, 12.8 kg)
   - Dueño: María Rodríguez
   - Vacunas: Rabia, Parvovirus, Bordetella
   - Notas: Problemas respiratorios leves

3. **Toby** - Beagle (Macho, 1 año, 10.2 kg)
   - Dueño: Laura Martínez
   - Vacunas: Parvovirus, Moquillo
   - Notas: Cachorro muy activo

4. **Bruno** - Pastor Alemán (Macho, 6 años, 35.0 kg)
   - Dueño: Carlos Sánchez
   - Vacunas: Rabia, Parvovirus, Moquillo, Leptospirosis
   - Notas: Displasia de cadera leve

5. **Firulais** - Mestizo (Macho, 7 años, 18.5 kg)
   - Dueño: Ana Torres
   - Vacunas: Rabia, Parvovirus
   - Notas: Perro callejero adoptado

### **Gatos (5):**

1. **Luna** - Siamés (Hembra, 2 años, 4.2 kg)
   - Dueño: Juan Pérez
   - Alergias: Polen
   - Vacunas: Triple felina, Rabia
   - Notas: Sensible al polen en primavera

2. **Mimi** - Persa (Hembra, 5 años, 5.5 kg)
   - Dueño: Pedro Gómez
   - Vacunas: Triple felina, Leucemia felina
   - Notas: Requiere cepillado frecuente

3. **Pelusa** - Angora (Hembra, 3 años, 4.8 kg)
   - Dueño: Laura Martínez
   - Vacunas: Triple felina, Rabia
   - Notas: Muy juguetona

4. **Nala** - Mestizo (Hembra, 2 años, 3.9 kg)
   - Dueño: Carlos Sánchez
   - Vacunas: Triple felina
   - Notas: Rescatada hace 1 año

5. **Garfield** - Naranja (Macho, 4 años, 6.2 kg)
   - Dueño: Ana Torres
   - Vacunas: Triple felina, Rabia
   - Notas: Tendencia a sobrepeso

---

## 📅 Citas Programadas (5 citas de ejemplo)

| Fecha      | Hora  | Duración | Médico           | Mascota | Motivo                  | Estado      |
|------------|-------|----------|------------------|---------|-------------------------|-------------|
| 2025-11-08 | 09:00 | 30 min   | Dr. Carlos Mendoza| Max    | Chequeo general         | CONFIRMADA  |
| 2025-11-08 | 10:00 | 15 min   | Dr. Carlos Mendoza| Rocky  | Vacunación              | PENDIENTE   |
| 2025-11-08 | 09:30 | 30 min   | Dra. Ana García  | Luna   | Control de alergia      | CONFIRMADA  |
| 2025-11-08 | 11:00 | 60 min   | Dr. Luis Ramírez | Bruno  | Revisión de displasia   | PENDIENTE   |
| 2025-11-09 | 14:00 | 30 min   | Dr. Carlos Mendoza| Toby   | Control de cachorro     | PENDIENTE   |

---

## ✅ Funcionalidades para Probar

### **1. Gestión de Usuarios (Solo ADMIN)**
- ✓ Crear nuevos médicos y auxiliares
- ✓ Editar información de usuarios
- ✓ Activar/Desactivar usuarios
- ✓ Restablecer contraseñas

### **2. Gestión de Dueños**
- ✓ Registrar nuevos dueños con cédula, teléfono, dirección
- ✓ Editar información de dueños
- ✓ Buscar dueños por nombre, email, teléfono o cédula
- ✓ Activar/Desactivar dueños

### **3. Gestión de Mascotas**
- ✓ Registrar nuevas mascotas con historial médico completo
- ✓ Asignar mascotas a dueños
- ✓ Actualizar información médica (vacunas, alergias, notas)
- ✓ Buscar mascotas por nombre, especie o raza
- ✓ Eliminar mascotas

### **4. Agenda de Citas (RF5) 🆕**
- ✓ Crear nuevas citas con fecha, hora y duración (15/30/60 min)
- ✓ Asignar médico y mascota a cada cita
- ✓ Validación automática de solapamiento de horarios
- ✓ Cambiar estado de citas: PENDIENTE → CONFIRMADA → COMPLETADA
- ✓ Cancelar citas
- ✓ Buscar citas por médico o mascota
- ✓ **Filtrar agenda por rango de fechas (día/semana)** 🆕
- ✓ **Filtrar agenda por médico específico** 🆕
- ✓ Visualizar agenda completa

---

## 🧪 Casos de Prueba Sugeridos

### **Prueba 1: Crear una cita y validar solapamiento**
1. Intentar crear una cita el 2025-11-08 a las 09:15 con Dr. Carlos Mendoza
2. El sistema debe rechazar porque se solapa con la cita de las 09:00 (30 min)
3. Crear la cita a las 09:30 con Dr. Carlos Mendoza (debe funcionar)

### **Prueba 2: Flujo completo de cita**
1. Crear cita en estado PENDIENTE
2. Confirmar la cita (cambiar a CONFIRMADA)
3. Completar la cita (cambiar a COMPLETADA)

### **Prueba 3: Cancelar cita**
1. Seleccionar una cita existente
2. Cancelar la cita (estado → CANCELADA)
3. Verificar que la cita cancelada no bloquea horarios

### **Prueba 4: Múltiples médicos**
1. Crear citas simultáneas para diferentes médicos
2. Verificar que cada médico puede tener citas al mismo tiempo

### **Prueba 5: Persistencia de datos**
1. Crear nuevas citas
2. Cerrar la aplicación
3. Reabrir y verificar que las citas se mantienen

### **Prueba 6: Filtrar agenda por fecha (día/semana)** 🆕
1. En el panel de filtros, ingresar fecha "Desde: 2025-11-08" y "Hasta: 2025-11-08"
2. Hacer clic en "Filtrar"
3. Solo deben mostrarse las citas del día 8 de noviembre
4. Para ver una semana completa: "Desde: 2025-11-08" y "Hasta: 2025-11-15"
5. Hacer clic en "Ver Todas" para quitar el filtro

### **Prueba 7: Filtrar agenda por médico** 🆕
1. En el filtro de "Médico", seleccionar "Dr. Carlos Mendoza"
2. Hacer clic en "Filtrar"
3. Solo deben mostrarse las citas asignadas a ese médico
4. Combinar filtros: seleccionar médico + rango de fechas para ver la agenda específica

---

## 📋 Notas Importantes

- **Persistencia:** Todos los datos se guardan automáticamente en `data/*.json` al cerrar la aplicación
- **Formato de fecha:** YYYY-MM-DD (ejemplo: 2025-11-08)
- **Formato de hora:** HH:MM (ejemplo: 14:30)
- **Validación de solapamiento:** El sistema previene que un médico tenga dos citas al mismo tiempo
- **Estados de cita:** Las citas canceladas no bloquean horarios futuros

---

