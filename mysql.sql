/*DATABASE*/

CREATE DATABASE IF NOT EXISTS gestion_de_proyectos;

/*TABLES*/

CREATE TABLE piezas
(
    codigo      VARCHAR(7)  NOT NULL PRIMARY KEY,
    nombre      VARCHAR(20) NOT NULL,
    precio      FLOAT       NOT NULL,
    descripcion TEXT
) ENGINE = INNODB;

CREATE TABLE proveedores
(
    codigo    VARCHAR(7)  NOT NULL PRIMARY KEY,
    nombre    VARCHAR(20) NOT NULL,
    apellidos VARCHAR(30) NOT NULL,
    direccion VARCHAR(40) NOT NULL
) ENGINE = INNODB;

CREATE TABLE proyectos
(
    codigo VARCHAR(7)  NOT NULL PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL,
    ciudad VARCHAR(40)
) ENGINE = INNODB;

CREATE TABLE gestiones
(
    cod_proyecto  VARCHAR(7) NOT NULL,
    cod_proveedor VARCHAR(7) NOT NULL,
    cod_pieza     VARCHAR(7) NOT NULL,
    cantidad      FLOAT,
    PRIMARY KEY (cod_proyecto, cod_proveedor, cod_pieza),
    FOREIGN KEY (cod_proyecto) REFERENCES proyectos (codigo) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (cod_proveedor) REFERENCES proveedores (codigo) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (cod_pieza) REFERENCES piezas (codigo) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = INNODB;