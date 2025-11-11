# Esquemas de Base de Datos - CABA Pro

Esta carpeta contiene los esquemas de las bases de datos utilizadas en el proyecto para uso en IDEs y herramientas de diseño.

## Estructura

```
schemas/
├── mysql/
│   └── schema.sql          # Esquema completo MySQL con tablas, índices y relaciones
├── mongodb/
│   ├── schema.json         # Esquema JSON Schema para MongoDB
│   └── schema.md           # Documentación detallada de las colecciones MongoDB
└── README.md               # Este archivo
```

## MySQL Schema

El archivo `mysql/schema.sql` contiene:

- Definición completa de todas las tablas
- Relaciones de claves foráneas
- Índices para optimización de consultas
- Comentarios sobre tipos de enumeraciones esperadas

### Tablas principales:

- `arbitros` - Información de árbitros
- `usuarios` - Usuarios del sistema
- `torneos` - Torneos y competencias
- `partidos` - Partidos programados
- `asignaciones` - Asignación de árbitros a partidos
- `liquidaciones` - Liquidaciones de pagos
- Tablas de colecciones: `arbitro_disponibilidades`, `torneo_tarifas`, `liquidacion_detalles`

### Uso en IDEs:

- **MySQL Workbench**: Importar el archivo SQL para generar el modelo EER
- **DBeaver**: Ejecutar el script para crear el esquema visual
- **IntelliJ IDEA**: Usar Database tool para importar el esquema
- **VS Code**: Usar extensiones como "MySQL" o "Database Client"

## MongoDB Schema

Los archivos en `mongodb/` documentan:

- Estructura de las colecciones `assignments` y `availabilities`
- Índices y validaciones
- Relación con las tablas MySQL
- Ejemplos de consultas

### Colecciones:

- `assignments` - Asignaciones de árbitros (sincronizada con MySQL)
- `availabilities` - Disponibilidades horarias de árbitros

### Uso en IDEs:

- **MongoDB Compass**: Usar `schema.json` como referencia
- **Studio 3T**: Importar el esquema para autocompletado
- **VS Code**: Usar extensiones como "MongoDB for VS Code"

## Notas Importantes

1. **UUIDs**: Los IDs en MySQL se almacenan como `BINARY(16)`, mientras que en MongoDB se usan como strings para facilitar la consulta.

2. **Sincronización**: Las colecciones MongoDB están diseñadas para sincronizarse con las tablas MySQL. Los cambios deben reflejarse en ambos sistemas.

3. **Enumeraciones**: Los valores de enum están documentados en los comentarios del schema SQL y en la documentación de MongoDB.

4. **Migraciones**: Este esquema es para referencia. Las migraciones reales están en `infra/mysql/migrations/`.

## Generación de Diagramas

Puedes usar herramientas como:

- **dbdiagram.io**: Importar el SQL para generar diagramas ER
- **draw.io**: Crear diagramas manualmente basados en estos esquemas
- **PlantUML**: Generar diagramas desde el SQL

## Actualización

Cuando se agreguen nuevas entidades o se modifiquen las existentes:

1. Actualizar `mysql/schema.sql` con las nuevas tablas/campos
2. Actualizar `mongodb/schema.json` y `schema.md` si hay cambios en las colecciones
3. Mantener sincronizados los comentarios y documentación

