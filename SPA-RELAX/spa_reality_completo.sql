-- Script de Base de Datos: SPA ENTRE DEDOS
-- Grupo 8: Pablo Mango, Milagros Alaniz, Alejo Mango
-- Fecha: Noviembre 2025

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS `spa_reality` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `spa_reality`;

-- =====================================================
-- TABLA: cliente
-- =====================================================
CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` int(11) NOT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos de ejemplo
INSERT INTO `cliente` (`nombre`, `apellido`, `dni`, `telefono`, `correo`, `estado`) VALUES
('Ana', 'Gómez', 12345678, '2664000000', 'ana@example.com', 1),
('Luis', 'Pérez', 87654321, '2664111111', 'luis@example.com', 1),
('María', 'Rodríguez', 23456789, '2664222222', 'maria@example.com', 1);

-- =====================================================
-- TABLA: masajista
-- =====================================================
CREATE TABLE `masajista` (
  `idMasajista` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `matricula` varchar(50) DEFAULT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `especialidad` varchar(50) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idMasajista`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos de ejemplo
INSERT INTO `masajista` (`nombre`, `matricula`, `telefono`, `especialidad`, `estado`) VALUES
('Carla', 'MAT001', '2664222222', 'Masajes relajantes', 1),
('Mauro', 'MAT002', '2664333333', 'Terapias faciales', 1),
('Sandra', 'MAT003', '2664444444', 'Masajes deportivos', 1);

-- =====================================================
-- TABLA: tratamiento
-- =====================================================
CREATE TABLE `tratamiento` (
  `id_tratamiento` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `duracion_min` int(11) NOT NULL,
  `costo` decimal(10,2) NOT NULL,
  `estado` varchar(10) DEFAULT 'activo',
  PRIMARY KEY (`id_tratamiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos de ejemplo
INSERT INTO `tratamiento` (`nombre`, `tipo`, `duracion_min`, `costo`, `estado`) VALUES
('Masaje Relajante', 'Corporal', 60, 5000.00, 'activo'),
('Tratamiento Facial', 'Facial', 45, 4500.00, 'activo'),
('Masaje Descontracturante', 'Corporal', 90, 7500.00, 'activo'),
('Exfoliación Corporal', 'Corporal', 60, 6000.00, 'activo');

-- =====================================================
-- TABLA: consultorio
-- =====================================================
CREATE TABLE `consultorio` (
  `idConsultorio` int(11) NOT NULL AUTO_INCREMENT,
  `usos` varchar(100) DEFAULT NULL,
  `equipamiento` varchar(100) DEFAULT NULL,
  `apto` tinyint(1) DEFAULT 1,
  `estado` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idConsultorio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos de ejemplo
INSERT INTO `consultorio` (`usos`, `equipamiento`, `apto`, `estado`) VALUES
('Masajes', 'Camilla, aceites, música ambiente', 1, 1),
('Tratamientos faciales', 'Sillón reclinable, espejo, productos estéticos', 1, 1),
('Sala VIP', 'Hidromasaje, aromaterapia, sonido envolvente', 1, 1);

-- =====================================================
-- TABLA: dia_de_spa
-- =====================================================
CREATE TABLE `dia_de_spa` (
  `idDiaSpa` int(11) NOT NULL AUTO_INCREMENT,
  `codPack` varchar(50) DEFAULT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `preferencias` varchar(255) DEFAULT NULL,
  `idCliente` int(11) NOT NULL,
  `estado` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idDiaSpa`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `dia_de_spa_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =====================================================
-- TABLA: sesion_turno (TRONCAL)
-- =====================================================
CREATE TABLE `sesion_turno` (
  `idSesion` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idMasajista` int(11) NOT NULL,
  `id_tratamiento` int(11) NOT NULL,
  `idConsultorio` int(11) NOT NULL,
  `fecha_hora_inicio` datetime NOT NULL,
  `fecha_hora_fin` datetime DEFAULT NULL,
  `observaciones` varchar(200) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idSesion`),
  KEY `idCliente` (`idCliente`),
  KEY `idMasajista` (`idMasajista`),
  KEY `id_tratamiento` (`id_tratamiento`),
  KEY `idConsultorio` (`idConsultorio`),
  CONSTRAINT `sesion_turno_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `sesion_turno_ibfk_2` FOREIGN KEY (`idMasajista`) REFERENCES `masajista` (`idMasajista`),
  CONSTRAINT `sesion_turno_ibfk_3` FOREIGN KEY (`id_tratamiento`) REFERENCES `tratamiento` (`id_tratamiento`),
  CONSTRAINT `sesion_turno_ibfk_4` FOREIGN KEY (`idConsultorio`) REFERENCES `consultorio` (`idConsultorio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos de ejemplo
INSERT INTO `sesion_turno` (`idCliente`, `idMasajista`, `id_tratamiento`, `idConsultorio`, `fecha_hora_inicio`, `fecha_hora_fin`, `observaciones`, `estado`) VALUES
(1, 1, 1, 1, '2025-11-20 10:00:00', '2025-11-20 11:00:00', 'Cliente prefiere música suave', 1),
(2, 2, 2, 2, '2025-11-20 14:00:00', '2025-11-20 14:45:00', 'Primera sesión', 1);

COMMIT;

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================