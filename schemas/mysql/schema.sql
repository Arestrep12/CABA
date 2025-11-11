-- ============================================
-- CABA Pro - MySQL Schema
-- ============================================
-- Esquema completo de la base de datos MySQL
-- Generado para uso en IDEs y herramientas de diseño
-- ============================================

-- Base de datos
CREATE DATABASE IF NOT EXISTS `caba_pro` 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE `caba_pro`;

-- ============================================
-- TABLA: arbitros
-- ============================================
CREATE TABLE IF NOT EXISTS `arbitros` (
    `id` BINARY(16) NOT NULL,
    `nombres` VARCHAR(120) NOT NULL,
    `apellidos` VARCHAR(120) NOT NULL,
    `tipo_documento` VARCHAR(32) NOT NULL,
    `numero_documento` VARCHAR(32) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `especialidad` VARCHAR(32) NOT NULL,
    `escalafon` VARCHAR(32) NOT NULL,
    `foto_url` VARCHAR(500),
    `activo` BOOLEAN NOT NULL DEFAULT TRUE,
    `fecha_ingreso` DATE,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_arbitros_numero_documento` (`numero_documento`),
    UNIQUE KEY `uk_arbitros_email` (`email`),
    INDEX `idx_arbitros_activo` (`activo`),
    INDEX `idx_arbitros_escalafon` (`escalafon`),
    INDEX `idx_arbitros_especialidad` (`especialidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: arbitro_disponibilidades
-- ============================================
CREATE TABLE IF NOT EXISTS `arbitro_disponibilidades` (
    `arbitro_id` BINARY(16) NOT NULL,
    `dia_semana` VARCHAR(32) NOT NULL,
    `hora_inicio` TIME NOT NULL,
    `hora_fin` TIME NOT NULL,
    PRIMARY KEY (`arbitro_id`, `dia_semana`, `hora_inicio`, `hora_fin`),
    CONSTRAINT `fk_arbitro_disponibilidades_arbitro` 
        FOREIGN KEY (`arbitro_id`) 
        REFERENCES `arbitros` (`id`) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE IF NOT EXISTS `usuarios` (
    `id` BINARY(16) NOT NULL,
    `username` VARCHAR(80) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `rol` VARCHAR(32) NOT NULL,
    `arbitro_id` BINARY(16),
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_usuarios_username` (`username`),
    UNIQUE KEY `uk_usuarios_arbitro_id` (`arbitro_id`),
    CONSTRAINT `fk_usuarios_arbitro` 
        FOREIGN KEY (`arbitro_id`) 
        REFERENCES `arbitros` (`id`) 
        ON DELETE SET NULL,
    INDEX `idx_usuarios_rol` (`rol`),
    INDEX `idx_usuarios_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: torneos
-- ============================================
CREATE TABLE IF NOT EXISTS `torneos` (
    `id` BINARY(16) NOT NULL,
    `nombre` VARCHAR(150) NOT NULL,
    `descripcion` VARCHAR(500),
    `fecha_inicio` DATE NOT NULL,
    `fecha_fin` DATE NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_torneos_periodo` (`fecha_inicio`, `fecha_fin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: torneo_tarifas
-- ============================================
CREATE TABLE IF NOT EXISTS `torneo_tarifas` (
    `torneo_id` BINARY(16) NOT NULL,
    `tarifa_escalafon` VARCHAR(32) NOT NULL,
    `tarifa_rol` VARCHAR(32) NOT NULL,
    `tarifa_monto` DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (`torneo_id`, `tarifa_escalafon`, `tarifa_rol`),
    CONSTRAINT `fk_torneo_tarifas_torneo` 
        FOREIGN KEY (`torneo_id`) 
        REFERENCES `torneos` (`id`) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: partidos
-- ============================================
CREATE TABLE IF NOT EXISTS `partidos` (
    `id` BINARY(16) NOT NULL,
    `torneo_id` BINARY(16) NOT NULL,
    `fecha_programada` DATETIME NOT NULL,
    `sede` VARCHAR(150) NOT NULL,
    `categoria` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_partidos_torneo` 
        FOREIGN KEY (`torneo_id`) 
        REFERENCES `torneos` (`id`) 
        ON DELETE RESTRICT,
    INDEX `idx_partidos_fecha_programada` (`fecha_programada`),
    INDEX `idx_partidos_torneo` (`torneo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: asignaciones
-- ============================================
CREATE TABLE IF NOT EXISTS `asignaciones` (
    `id` BINARY(16) NOT NULL,
    `partido_id` BINARY(16) NOT NULL,
    `arbitro_id` BINARY(16) NOT NULL,
    `rol` VARCHAR(32) NOT NULL,
    `estado` VARCHAR(32) NOT NULL DEFAULT 'PENDIENTE',
    `respondido_en` DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_asignaciones_partido` 
        FOREIGN KEY (`partido_id`) 
        REFERENCES `partidos` (`id`) 
        ON DELETE CASCADE,
    CONSTRAINT `fk_asignaciones_arbitro` 
        FOREIGN KEY (`arbitro_id`) 
        REFERENCES `arbitros` (`id`) 
        ON DELETE RESTRICT,
    INDEX `idx_asignaciones_partido` (`partido_id`),
    INDEX `idx_asignaciones_arbitro` (`arbitro_id`),
    INDEX `idx_asignaciones_estado` (`estado`),
    INDEX `idx_asignaciones_arbitro_estado` (`arbitro_id`, `estado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: liquidaciones
-- ============================================
CREATE TABLE IF NOT EXISTS `liquidaciones` (
    `id` BINARY(16) NOT NULL,
    `arbitro_id` BINARY(16) NOT NULL,
    `fecha_inicio` DATE NOT NULL,
    `fecha_fin` DATE NOT NULL,
    `estado` VARCHAR(32) NOT NULL DEFAULT 'GENERADA',
    `total` DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_liquidaciones_arbitro` 
        FOREIGN KEY (`arbitro_id`) 
        REFERENCES `arbitros` (`id`) 
        ON DELETE RESTRICT,
    INDEX `idx_liquidaciones_arbitro` (`arbitro_id`),
    INDEX `idx_liquidaciones_estado` (`estado`),
    INDEX `idx_liquidaciones_periodo` (`fecha_inicio`, `fecha_fin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: liquidacion_detalles
-- ============================================
CREATE TABLE IF NOT EXISTS `liquidacion_detalles` (
    `liquidacion_id` BINARY(16) NOT NULL,
    `partido_id` BINARY(16) NOT NULL,
    `rol_asignacion` VARCHAR(32) NOT NULL,
    `monto` DECIMAL(10,2) NOT NULL,
    `fecha_partido` DATE NOT NULL,
    PRIMARY KEY (`liquidacion_id`, `partido_id`, `rol_asignacion`),
    CONSTRAINT `fk_liquidacion_detalles_liquidacion` 
        FOREIGN KEY (`liquidacion_id`) 
        REFERENCES `liquidaciones` (`id`) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COMENTARIOS Y DOCUMENTACIÓN
-- ============================================

-- Tipos de enumeraciones esperadas:
-- 
-- tipo_documento: DNI, PASAPORTE, CEDULA, etc.
-- especialidad: CAMPO, MESA
-- escalafon: FIBA, PRIMERA, SEGUNDA, TERCERA, etc.
-- rol (usuarios): ADMIN, ARBITRO
-- rol (asignaciones): PRIMER_ARBITRO, SEGUNDO_ARBITRO, APUNTADOR, CRONOMETRADOR, etc.
-- estado (asignaciones): PENDIENTE, ACEPTADA, RECHAZADA, CANCELADA
-- estado (liquidaciones): GENERADA, PAGADA
-- dia_semana: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY

