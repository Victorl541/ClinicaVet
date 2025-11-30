# DATOS DE PRUEBA - ClinicaVet2

## Credenciales de Acceso

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

## Dueños de Mascotas (6 registrados)

| Cédula     | Nombre            | Teléfono    | Dirección                           |
|------------|-------------------|-------------|-------------------------------------|
| 1010234567 | Juan Pérez        | 3001234567  | Calle 10 #20-30, Pereira            |
| 1020345678 | María Rodríguez   | 3112345678  | Carrera 15 #25-40, Pereira          |
| 1030456789 | Pedro Gómez       | 3223456789  | Avenida 30 de Agosto #45-20, Pereira|
| 1040567890 | Laura Martínez    | 3134567890  | Calle 18 #12-25, Dosquebradas       |
| 1050678901 | Carlos Sánchez    | 3145678901  | Carrera 7 #35-15, Pereira           |
| 1060789012 | Ana Torres        | 3156789012  | Calle 25 #18-30, La Virginia        |

---

## Mascotas Registradas (10 mascotas)

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
-
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

## Citas Programadas (5 citas de ejemplo)

| Fecha      | Hora  | Duración | Médico           | Mascota | Motivo                  | Estado      |
|------------|-------|----------|------------------|---------|-------------------------|-------------|
| 2025-11-08 | 09:00 | 30 min   | Dr. Carlos Mendoza| Max    | Chequeo general         | CONFIRMADA  |
| 2025-11-08 | 10:00 | 15 min   | Dr. Carlos Mendoza| Rocky  | Vacunación              | PENDIENTE   |
| 2025-11-08 | 09:30 | 30 min   | Dra. Ana García  | Luna   | Control de alergia      | CONFIRMADA  |
| 2025-11-08 | 11:00 | 60 min   | Dr. Luis Ramírez | Bruno  | Revisión de displasia   | PENDIENTE   |
| 2025-11-09 | 14:00 | 30 min   | Dr. Carlos Mendoza| Toby   | Control de cachorro     | PENDIENTE   |

---

## Funcionalidades para Probar

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

### **5. Atención Médica e Historia Clínica (RF6) 🆕 - SOLO MÉDICOS**
- ✓ Ver todas las citas asignadas al médico actual
- ✓ Abrir cita y registrar atención médica (síntomas, diagnóstico, procedimientos, tratamiento, órdenes)
- ✓ Cerrar cita con estado: ATENDIDA / NO_ASISTIÓ / REPROGRAMAR
- ✓ Consultar historia clínica completa por mascota
- ✓ Búsqueda de citas por mascota o dueño
- ✓ Persistencia automática en JSON

---

## Historias Clínicas de Ejemplo

### **Max (Golden Retriever) - Dueño: Juan Pérez**

#### Atención #1 - 2024-01-15 a las 10:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Tos seca persistente, estornudos frecuentes, ligera secreción nasal
- **Diagnóstico:** Chequeo general de rutina. Mascota en buen estado de salud. Se detectó ligera irritación en vías respiratorias superiores, posiblemente por alergia estacional.
- **Procedimientos:** Examen físico completo, auscultación cardiopulmonar, revisión de oídos y boca, palpación abdominal
- **Tratamiento:** Antihistamínico canino 10mg cada 12 horas por 7 días, mantener hidratación adecuada
- **Órdenes:** Control en 15 días si persisten los síntomas. Evitar exposición a ambientes con mucho polvo.
- **Observaciones:** Peso: 15.2kg. Temperatura: 38.5°C. Frecuencia cardíaca: 95 lpm. Vacunas al día.

#### Atención #2 - 2024-02-28 a las 09:15
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Control post-tratamiento. Propietario reporta mejoría completa.
- **Diagnóstico:** Resolución completa del cuadro alérgico. Vías respiratorias normales. Mascota completamente recuperada.
- **Procedimientos:** Auscultación pulmonar, inspección de mucosas, revisión general
- **Tratamiento:** No requiere tratamiento adicional. Suspender antihistamínico.
- **Órdenes:** Mantener cuidados preventivos. Próximo control en 6 meses para chequeo de rutina.
- **Observaciones:** Peso: 15.5kg. Temperatura: 38.3°C. Excelente evolución. Propietario muy satisfecho con la atención.

---

### **Rocky (Bulldog Francés) - Dueño: María Rodríguez**

#### Atención #1 - 2024-03-20 a las 14:00
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Cojera en pata trasera izquierda, dolor al apoyar, inflamación leve en articulación
- **Diagnóstico:** Esguince grado I en articulación tarsiana izquierda. Sin fractura. Posible traumatismo por actividad física intensa.
- **Procedimientos:** Examen ortopédico, palpación de extremidades, pruebas de movilidad articular, radiografía de miembro posterior
- **Tratamiento:** Carprofeno 50mg cada 12 horas por 5 días, reposo relativo, aplicar hielo local 3 veces al día por 15 minutos
- **Órdenes:** Limitar actividad física por 2 semanas. Control en 7 días para evaluar evolución. Si empeora, acudir inmediatamente.
- **Observaciones:** Peso: 22.3kg. Sin dolor a la palpación de otras articulaciones. Radiografía sin alteraciones óseas. Pronóstico favorable.

#### Atención #2 - 2024-04-03 a las 11:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Control post-esguince. Propietario reporta recuperación total, sin cojera.
- **Diagnóstico:** Resolución completa del esguince. Articulación sin inflamación ni dolor. Movilidad normal recuperada.
- **Procedimientos:** Examen ortopédico, pruebas de movilidad, palpación articular
- **Tratamiento:** No requiere tratamiento. Alta médica.
- **Órdenes:** Puede retomar actividad física gradualmente. Evitar saltos bruscos por 1 semana más.
- **Observaciones:** Peso: 22.5kg. Excelente recuperación. Se recomienda mantener peso ideal para evitar sobrecarga articular.

---

### **🐈 Luna (Siamés) - Dueño: Juan Pérez**

#### Atención #1 - 2024-05-10 a las 16:00
- **Médico:** Dra. Ana García
- **Síntomas:** Estornudos frecuentes, secreción nasal y ocular bilateral, conjuntivitis leve, inapetencia parcial
- **Diagnóstico:** Rinotraqueítis viral felina (herpesvirus). Cuadro respiratorio superior leve-moderado. Sin complicaciones secundarias.
- **Procedimientos:** Examen físico completo, evaluación de vías respiratorias, limpieza de secreciones, instilación de colirio
- **Tratamiento:** Lisina 250mg cada 12 horas por 14 días, colirio oftálmico cada 6 horas, amoxicilina 50mg cada 12 horas por 7 días (prevención)
- **Órdenes:** Aislar de otros gatos si los hay. Mantener hidratación. Alimento húmedo tibio para estimular apetito. Control en 5 días.
- **Observaciones:** Peso: 4.2kg. Temperatura: 39.1°C. Sin distrés respiratorio. Pronóstico favorable. Enfermedad común y manejable.

#### Atención #2 - 2024-05-20 a las 10:00
- **Médico:** Dra. Ana García
- **Síntomas:** Control. Propietario reporta mejoría notable, menos estornudos, secreción disminuida.
- **Diagnóstico:** Evolución favorable del cuadro viral. Mejoría clínica evidente. Continuar tratamiento.
- **Procedimientos:** Examen físico general, evaluación de vías respiratorias
- **Tratamiento:** Continuar con lisina otros 7 días, suspender antibiótico, reducir frecuencia de colirio.
- **Órdenes:** Control final en 1 semana. Completar tratamiento con lisina.
- **Observaciones:** Peso: 4.3kg. Temperatura: 38.7°C. Buena evolución.

---

### **Bruno (Pastor Alemán) - Dueño: Carlos Sánchez**

#### Atención #1 - 2024-06-05 a las 15:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Cojera leve en miembro posterior derecho, dificultad para levantarse después de descansar
- **Diagnóstico:** Displasia de cadera grado leve-moderado. Cambios degenerativos incipientes. Requiere manejo a largo plazo.
- **Procedimientos:** Examen ortopédico completo, pruebas de dolor articular, radiografía de caderas, evaluación de masa muscular
- **Tratamiento:** Condroprotectores (glucosamina + condroitina) diariamente, carprofeno 75mg cada 12 horas por 10 días, control de peso
- **Órdenes:** Ejercicio moderado regular (natación ideal), evitar sobrepeso, suplementación permanente. Control cada 3 meses.
- **Observaciones:** Peso: 35.0kg (ideal 32-33kg). Displasia conocida, ahora sintomática. Pronóstico bueno con manejo adecuado.

#### Atención #2 - 2024-09-10 a las 09:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Control trimestral. Propietario reporta mejoría con tratamiento, más activo, menos cojera.
- **Diagnóstico:** Displasia de cadera estable con tratamiento. Buena respuesta a condroprotectores. Continuar manejo.
- **Procedimientos:** Examen ortopédico, evaluación de masa muscular, pesaje
- **Tratamiento:** Continuar condroprotectores indefinidamente, carprofeno solo si hay brotes de dolor.
- **Órdenes:** Mantener peso ideal, ejercicio regular, próximo control en 3 meses.
- **Observaciones:** Peso: 33.5kg (excelente). Masa muscular mejorada. Propietario muy comprometido con cuidados.

---

### **Toby (Beagle) - Dueño: Laura Martínez**

#### Atención #1 - 2024-07-01 a las 11:00
- **Médico:** Dra. Ana García
- **Síntomas:** Vómitos intermitentes, diarrea leve, inapetencia, ligero letargo
- **Diagnóstico:** Gastroenteritis aguda leve, posiblemente por indiscreción alimentaria. Sin deshidratación severa.
- **Procedimientos:** Examen físico completo, palpación abdominal, evaluación de hidratación
- **Tratamiento:** Metoclopramida gotas cada 8 horas por 3 días, dieta blanda (pollo y arroz) por 5 días, probióticos
- **Órdenes:** Monitorear frecuencia de vómitos. Si persiste o empeora, regresar. Hidratación constante.
- **Observaciones:** Peso: 10.2kg. Temperatura: 38.8°C. Cachorro curioso que come cosas del suelo.

---

### **🐈 Garfield (Naranja) - Dueño: Ana Torres**

#### Atención #1 - 2024-08-15 a las 14:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Sobrepeso evidente, letargo, dificultad para saltar, jadeo excesivo
- **Diagnóstico:** Obesidad grado II. Peso 6.2kg (ideal 4.5-5kg). Riesgo de diabetes y problemas articulares. Requiere plan de reducción de peso.
- **Procedimientos:** Pesaje, evaluación de condición corporal, palpación abdominal, análisis de sangre (glucosa, perfil lipídico)
- **Tratamiento:** Dieta hipocalórica específica para gatos, raciones controladas, incrementar actividad física con juegos
- **Órdenes:** Reducir ingesta calórica 30%, NO dar premios extra. Control de peso mensual. Meta: perder 0.2kg por mes.
- **Observaciones:** Peso: 6.2kg. Análisis: glucosa límite alto. Propietario confiesa dar muchos premios. Plan educativo sobre alimentación.

#### Atención #2 - 2024-10-15 a las 10:00
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Control de peso. Propietario reporta cumplimiento de dieta y más actividad.
- **Diagnóstico:** Progreso en reducción de peso. Pérdida de 0.4kg en 2 meses. Continuar con plan nutricional.
- **Procedimientos:** Pesaje, evaluación de condición corporal
- **Tratamiento:** Continuar dieta hipocalórica, mantener actividad física.
- **Órdenes:** Objetivo: alcanzar 5.5kg en 2 meses más. Control mensual.
- **Observaciones:** Peso: 5.8kg. Excelente progreso. Propietario motivado y comprometido.

---

### **🐈 Mimi (Persa) - Dueño: Pedro Gómez**

#### Atención #1 - 2024-09-05 a las 16:30
- **Médico:** Dra. Ana García
- **Síntomas:** Pelo enmarañado con nudos severos, piel irritada debajo, mal olor, ligera dermatitis
- **Diagnóstico:** Dermatitis por falta de higiene y cuidado del pelaje. Nudos severos que causan tracción y malestar. Requiere rasurado parcial.
- **Procedimientos:** Rasurado de zonas con nudos irrecuperables, baño medicado, aplicación de crema calmante, limpieza de piel
- **Tratamiento:** Champú medicado semanal por 1 mes, cepillado diario obligatorio, omega 3 para salud del pelaje
- **Órdenes:** Cepillado diario sin excepción. Baño cada 2 semanas. Control en 1 mes para evaluar crecimiento de pelo.
- **Observaciones:** Peso: 5.5kg. Piel irritada en varias zonas. Se educa a propietario sobre cuidados de razas persas. Compromiso de cepillado diario.

---

### **🐈 Fifi (Gato mixfelino) - Dueño: María López**

#### Atención #1 - 2024-10-05 a las 11:00
- **Médico:** Dra. Ana García
- **Síntomas:** Primera consulta. Propietario solicita chequeo general y asesoría sobre alimentación y vacunación.
- **Diagnóstico:** Gatita joven en excelente estado general. Primera valoración veterinaria. Peso adecuado para edad. Sin alteraciones físicas.
- **Procedimientos:** Examen físico completo, revisión de mucosas, auscultación cardiopulmonar, palpación abdominal, revisión dental
- **Tratamiento:** Aplicación de primera dosis de vacuna triple felina, desparasitación interna con fenbendazol
- **Órdenes:** Regresar en 21 días para refuerzo de vacuna. Mantener desparasitación cada 3 meses. Alimentación balanceada específica para gatitos.
- **Observaciones:** Peso: 5.0kg. Temperatura: 38.6°C. Carácter tranquilo. Se entrega cartilla de vacunación y calendario de próximas vacunas.

#### Atención #2 - 2024-10-28 a las 14:30
- **Médico:** Dra. Ana García
- **Síntomas:** Control de vacunación. Propietaria reporta que Fifi está muy activa y comiendo bien.
- **Diagnóstico:** Excelente evolución. Gatita saludable. Aplicación de refuerzo de vacuna triple felina completada exitosamente.
- **Procedimientos:** Examen físico de control, revisión de mucosas, aplicación de segunda dosis de vacuna triple felina
- **Tratamiento:** Refuerzo de vacuna triple felina completado. Próxima vacuna: rabia en 1 mes.
- **Órdenes:** Regresar en 1 mes para vacuna antirrábica. Continuar con alimentación balanceada. Próximo control en 3 meses.
- **Observaciones:** Peso: 5.2kg. Temperatura: 38.5°C. Ganancia de peso adecuada. Propietaria muy comprometida con cuidados. Esquema de vacunación en curso.

---

## Notas Importantes

- **Persistencia:** Todos los datos se guardan automáticamente en `data/*.json` al cerrar la aplicación
- **Formato de fecha:** YYYY-MM-DD (ejemplo: 2025-11-08)
- **Formato de hora:** HH:MM (ejemplo: 14:30)
- **Validación de solapamiento:** El sistema previene que un médico tenga dos citas al mismo tiempo
- **Estados de cita:** Las citas canceladas no bloquean horarios futuros

---


#### Atención #1 - 2025-10-15 a las 10:00
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Tos persistente hace 3 días, falta de apetito, decaimiento
- **Diagnóstico:** Traqueobronquitis infecciosa canina (Tos de las Perreras)
- **Procedimientos:** Auscultación cardiopulmonar, examen físico general, toma de temperatura (38.9°C)
- **Tratamiento:** 
  - Amoxicilina 500mg cada 12 horas por 7 días
  - Dextrometorfano (jarabe antitusígeno) 5ml cada 8 horas
  - Reposo en casa, evitar contacto con otros perros
- **Órdenes:** Control en 7 días. Si persiste la tos, solicitar radiografía de tórax
- **Estado:** ATENDIDA

#### Atención #2 - 2025-10-22 a las 09:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Tos ha disminuido considerablemente, recuperó el apetito
- **Diagnóstico:** Evolución favorable de traqueobronquitis
- **Procedimientos:** Auscultación pulmonar normal, peso recuperado (28.5kg)
- **Tratamiento:** Suspender antibiótico. Continuar jarabe antitusígeno 3 días más
- **Órdenes:** No requiere controles adicionales. Vacuna Bordetella recomendada para prevención
- **Estado:** ATENDIDA

---

### **Rocky (Bulldog Francés) - Dueño: María Rodríguez**

#### Atención #1 - 2025-09-20 a las 14:00
- **Médico:** Dra. Ana García
- **Síntomas:** Dificultad respiratoria moderada, jadeo excesivo, intolerancia al ejercicio
- **Diagnóstico:** Síndrome braquiocefálico - estenosis de narinas moderada
- **Procedimientos:** Examen de vías aéreas superiores, auscultación cardíaca, saturación de oxígeno 92%
- **Tratamiento:** 
  - Evitar ejercicio en horas de calor
  - Mantener peso ideal (12.8kg - sin sobrepeso)
  - Arnés en lugar de collar
- **Órdenes:** 
  - Considerar cirugía correctiva (rinoplastia) si empeoran los síntomas
  - Control en 3 meses
  - Radiografía de tórax y electrocardiograma antes de cirugía
- **Estado:** ATENDIDA

#### Atención #2 - 2025-11-01 a las 10:30
- **Médico:** Dra. Ana García
- **Síntomas:** Sin cambios significativos, jadeo leve pero manejable
- **Diagnóstico:** Síndrome braquiocefálico estable
- **Procedimientos:** Peso estable 12.8kg, saturación 93%
- **Tratamiento:** Continuar con medidas preventivas
- **Órdenes:** Control en 6 meses o antes si empeora
- **Estado:** ATENDIDA

---

### **🐈 Luna (Siamés) - Dueño: Juan Pérez**

#### Atención #1 - 2025-08-10 a las 11:00
- **Médico:** Dr. Luis Ramírez
- **Síntomas:** Estornudos frecuentes, secreción nasal clara, ojos llorosos (primavera)
- **Diagnóstico:** Rinitis alérgica estacional - alergia al polen
- **Procedimientos:** Examen físico, descarte de infección respiratoria
- **Tratamiento:** 
  - Cetirizina 5mg (1/2 tableta) cada 24 horas por 15 días
  - Limpieza de ojos con suero fisiológico 3 veces al día
  - Mantener ventanas cerradas en días de alta polinización
- **Órdenes:** Si no mejora en 5 días, pruebas de alergia. Control en 15 días
- **Estado:** ATENDIDA

#### Atención #2 - 2025-08-25 a las 09:00
- **Médico:** Dr. Luis Ramírez
- **Síntomas:** Mejoría del 80%, estornudos ocasionales solamente
- **Diagnóstico:** Respuesta favorable al tratamiento antihistamínico
- **Procedimientos:** Examen físico normal
- **Tratamiento:** Suspender Cetirizina. Reiniciar si reaparecen síntomas en próxima primavera
- **Órdenes:** No requiere control. Monitoreo en casa
- **Estado:** ATENDIDA

---

### **Bruno (Pastor Alemán) - Dueño: Carlos Sánchez**

#### Atención #1 - 2025-07-05 a las 15:00
- **Médico:** Dra. Ana García
- **Síntomas:** Cojera en pata trasera derecha, dificultad para levantarse después de descansar
- **Diagnóstico:** Displasia de cadera bilateral - grado leve (estadio I-II)
- **Procedimientos:** 
  - Examen ortopédico - signo de Ortolani positivo bilateral
  - Radiografía de cadera - confirmación de displasia
  - Evaluación de rango de movimiento
- **Tratamiento:** 
  - Carprofeno 75mg cada 12 horas por 10 días (antiinflamatorio)
  - Suplemento condroprotector (glucosamina + condroitina) diario indefinido
  - Control de peso estricto (mantener 35kg máximo)
  - Ejercicio moderado (natación recomendada)
- **Órdenes:** 
  - Control radiográfico en 6 meses
  - Fisioterapia veterinaria 2 veces por semana
  - Considerar cirugía si progresa la displasia
- **Estado:** ATENDIDA

#### Atención #2 - 2025-10-10 a las 16:00
- **Médico:** Dra. Ana García
- **Síntomas:** Mejoría notable, solo cojera leve ocasional
- **Diagnóstico:** Displasia de cadera estable con manejo conservador
- **Procedimientos:** Radiografía de control - sin progresión, peso estable 34.5kg
- **Tratamiento:** 
  - Continuar condroprotector indefinidamente
  - Carprofeno solo en crisis (cada 3-4 meses según necesidad)
- **Órdenes:** Control en 6 meses, mantener fisioterapia 1 vez por semana
- **Estado:** ATENDIDA

---

### **Toby (Beagle cachorro) - Dueño: Laura Martínez**

#### Atención #1 - 2025-10-01 a las 08:30
- **Médico:** Dr. Carlos Mendoza
- **Síntomas:** Primera consulta de cachorro, vacunación pendiente, desparasitación
- **Diagnóstico:** Cachorro sano, control preventivo
- **Procedimientos:** 
  - Examen físico completo - sin alteraciones
  - Peso: 10.2kg, temperatura: 38.5°C
  - Revisión de mucosas, auscultación, palpación abdominal
- **Tratamiento:** 
  - Vacuna múltiple (Parvovirus + Moquillo + Hepatitis + Parainfluenza)
  - Desparasitante Milbemicina oxima 1 tableta dosis única
  - Próxima vacuna en 21 días
- **Órdenes:** 
  - Refuerzo vacunal el 22 de octubre
  - Vacuna antirrábica a los 4 meses
  - Evitar contacto con perros no vacunados por 10 días
- **Estado:** ATENDIDA

---

### **🐈 Garfield (Mestizo naranja) - Dueño: Ana Torres**

#### Atención #1 - 2025-09-15 a las 13:00
- **Médico:** Dr. Luis Ramírez
- **Síntomas:** Control de rutina, peso elevado
- **Diagnóstico:** Sobrepeso - obesidad grado I (peso ideal: 5.0kg, actual: 6.2kg)
- **Procedimientos:** Examen físico, evaluación de condición corporal (7/9)
- **Tratamiento:** 
  - Dieta hipocalórica (alimento light) 40g cada 12 horas
  - Reducir premios y golosinas al mínimo
  - Estimular actividad física con juguetes interactivos
- **Órdenes:** 
  - Control de peso en 30 días (meta: reducir 200g)
  - Examen de glucosa si no baja de peso
- **Estado:** ATENDIDA

#### Atención #2 - 2025-10-20 a las 14:30
- **Médico:** Dr. Luis Ramírez
- **Síntomas:** Control de peso
- **Diagnóstico:** Progreso lento en reducción de peso (6.0kg, bajó 200g)
- **Procedimientos:** Peso actual 6.0kg, condición corporal 6/9
- **Tratamiento:** Continuar dieta estricta, aumentar actividad
- **Órdenes:** Control en 30 días
- **Estado:** ATENDIDA

---



## Notas Importantes

- **Persistencia:** Todos los datos se guardan automáticamente en `data/*.json` al cerrar la aplicación
- **Formato de fecha:** YYYY-MM-DD (ejemplo: 2025-11-08)
- **Formato de hora:** HH:MM (ejemplo: 14:30)
- **Validación de solapamiento:** El sistema previene que un médico tenga dos citas al mismo tiempo
- **Estados de cita:** Las citas canceladas no bloquean horarios futuros

---

