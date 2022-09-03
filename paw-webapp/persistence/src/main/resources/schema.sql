CREATE TABLE IF NOT EXISTS aptitud (
    id SERIAL PRIMARY KEY,
    descripcion TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rubro (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS empresa (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenia VARCHAR(100) NOT NULL,
    descripcion TEXT,
    idRubro INTEGER,
    ubicacion TEXT,
    FOREIGN KEY (idRubro) REFERENCES rubro ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenia VARCHAR(100) NOT NULL,
    descripcion TEXT,
    idRubro INTEGER,
    ubicacion TEXT,
    posicionActual TEXT,
    educacion TEXT,
    FOREIGN KEY (idRubro) REFERENCES rubro ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS experiencia (
    id SERIAL PRIMARY KEY,
    idUsuario INTEGER NOT NULL,
    fechaDesde DATE NOT NULL,
    fechaHasta DATE,
    empresa VARCHAR(100),
    posicion TEXT NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ofertaLaboral (
    id SERIAL PRIMARY KEY,
    idEmpresa INTEGER,
    posicion TEXT NOT NULL,
    descripcion TEXT,
    salario DECIMAL(12,2), /* Equivalente a DECIMAL(10,2). Limite: 999.999.999,99 */
    idRubro INTEGER,
    FOREIGN KEY (idEmpresa) REFERENCES empresa ON DELETE CASCADE,
    FOREIGN KEY (idRubro) REFERENCES rubro ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS aptitudOfertaLaboral (
    idOferta INTEGER NOT NULL,
    idAptitud INTEGER NOT NULL,
    PRIMARY KEY (idOferta, idAptitud),
    FOREIGN KEY (idOferta) REFERENCES ofertaLaboral ON DELETE CASCADE,
    FOREIGN KEY (idAptitud) REFERENCES aptitud ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS aptitudUsuario (
    idUsuario INTEGER NOT NULL,
    idAptitud INTEGER NOT NULL,
    PRIMARY KEY (idUsuario, idAptitud),
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE,
    FOREIGN KEY (idAptitud) REFERENCES aptitud ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS solicitud (
    idOferta INTEGER NOT NULL,
    idUsuario INTEGER NOT NULL,
    PRIMARY KEY (idOferta, idUsuario),
    FOREIGN KEY (idOferta) REFERENCES ofertaLaboral ON DELETE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contactado (
    idEmpresa INTEGER NOT NULL,
    idUsuario INTEGER NOT NULL,
    PRIMARY KEY (idEmpresa, idUsuario),
    FOREIGN KEY (idEmpresa) REFERENCES empresa ON DELETE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE
);