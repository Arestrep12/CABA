# CABA Pro - MongoDB Schema Documentation

## Base de Datos: `agenda_db`

Este documento describe el esquema de las colecciones MongoDB utilizadas en el `agenda-service`.

---

## Colección: `assignments`

**Descripción:** Almacena las asignaciones de árbitros a partidos, sincronizadas con el sistema principal MySQL.

### Estructura del Documento

```typescript
{
  assignmentId: string;        // ID único de la asignación (UUID)
  refereeId: string;           // ID del árbitro (UUID del sistema principal)
  matchId: string;             // ID del partido (UUID del sistema principal)
  scheduledAt: Date;           // Fecha y hora programada del partido
  role: string;                // Rol: "PRIMER_ARBITRO", "SEGUNDO_ARBITRO", "APUNTADOR", "CRONOMETRADOR"
  status: string;              // Estado: "PENDING", "ACCEPTED", "REJECTED", "CANCELLED"
  createdAt: Date;             // Timestamp automático
  updatedAt: Date;             // Timestamp automático
}
```

### Índices

1. **Índice único en `assignmentId`**
   ```javascript
   { assignmentId: 1 } // unique: true
   ```

2. **Índice en `refereeId`**
   ```javascript
   { refereeId: 1 }
   ```

3. **Índice compuesto para consultas por árbitro y fecha**
   ```javascript
   { refereeId: 1, scheduledAt: 1 }
   ```

### Validaciones

- `assignmentId`: Requerido, único
- `refereeId`: Requerido, indexado
- `matchId`: Requerido
- `scheduledAt`: Requerido, tipo Date
- `role`: Requerido, debe ser uno de los valores enum
- `status`: Requerido, default "PENDING", se convierte a mayúsculas

---

## Colección: `availabilities`

**Descripción:** Almacena las disponibilidades horarias de los árbitros por día de la semana.

### Estructura del Documento

```typescript
{
  refereeId: string;           // ID del árbitro (UUID del sistema principal)
  dayOfWeek: number;            // 0=Domingo, 1=Lunes, 2=Martes, ..., 6=Sábado
  startTime: string;            // Hora de inicio en formato "HH:mm" (ej: "09:00")
  endTime: string;              // Hora de fin en formato "HH:mm" (ej: "18:00")
  createdAt: Date;              // Timestamp automático
  updatedAt: Date;              // Timestamp automático
}
```

### Índices

1. **Índice en `refereeId`**
   ```javascript
   { refereeId: 1 }
   ```

2. **Índice único compuesto para evitar duplicados**
   ```javascript
   { refereeId: 1, dayOfWeek: 1, startTime: 1, endTime: 1 } // unique: true
   ```

### Validaciones

- `refereeId`: Requerido, indexado
- `dayOfWeek`: Requerido, número entre 0 y 6
- `startTime`: Requerido, formato "HH:mm" (24 horas)
- `endTime`: Requerido, formato "HH:mm" (24 horas)

### Notas

- El índice único compuesto previene que un árbitro tenga múltiples registros con la misma combinación de día/horario
- Los valores de `dayOfWeek` siguen el estándar JavaScript Date: 0=Domingo, 6=Sábado

---

## Relación con MySQL

Las colecciones MongoDB están diseñadas para sincronizarse con las tablas MySQL:

- `assignments` ↔ `asignaciones` (MySQL)
- `availabilities` ↔ `arbitro_disponibilidades` (MySQL)

Los IDs (`refereeId`, `matchId`) son UUIDs que corresponden a los `BINARY(16)` en MySQL, almacenados como strings en MongoDB para facilitar la consulta.

---

## Ejemplos de Consultas

### Obtener asignaciones pendientes de un árbitro

```javascript
db.assignments.find({
  refereeId: "uuid-del-arbitro",
  status: "PENDING"
}).sort({ scheduledAt: 1 });
```

### Obtener disponibilidades de un árbitro para un día específico

```javascript
db.availabilities.find({
  refereeId: "uuid-del-arbitro",
  dayOfWeek: 1  // Lunes
});
```

### Buscar conflictos de horario

```javascript
db.assignments.find({
  refereeId: "uuid-del-arbitro",
  scheduledAt: {
    $gte: ISODate("2024-01-15T00:00:00Z"),
    $lte: ISODate("2024-01-15T23:59:59Z")
  },
  status: { $in: ["PENDING", "ACCEPTED"] }
});
```

