-- SET client_encoding = 'UTF8'
CREATE TABLE IF NOT EXISTS imagen (
    id SERIAL PRIMARY KEY,
    bytes BYTEA
);

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
--     idImagen  INTEGER,
    ubicacion TEXT,
    posicionActual TEXT,
    educacion TEXT,
    FOREIGN KEY (idRubro) REFERENCES rubro ON DELETE SET NULL
--     FOREIGN KEY (idImagen) REFERENCES imagen (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS experiencia (
    id SERIAL PRIMARY KEY,
    idUsuario INTEGER NOT NULL,
    mesDesde INTEGER NOT NULL,
    anioDesde INTEGER NOT NULL,
    mesHasta INTEGER,
    anioHasta INTEGER,
    empresa VARCHAR(100),
    posicion TEXT NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE,
    CHECK(mesDesde BETWEEN 1 AND 12),
    CHECK(mesHasta BETWEEN 1 AND 12),
    CHECK(anioDesde BETWEEN 1900 AND 2100),
    CHECK(anioHasta BETWEEN 1900 AND 2100),
    CHECK((anioHasta > anioDesde) OR (anioHasta = anioDesde AND mesHasta >= mesDesde))
);

CREATE TABLE IF NOT EXISTS educacion (
    id SERIAL PRIMARY KEY,
    idUsuario INTEGER NOT NULL,
    mesDesde INTEGER NOT NULL,
    anioDesde INTEGER NOT NULL,
    mesHasta INTEGER,
    anioHasta INTEGER,
    titulo TEXT NOT NULL,
    institucion VARCHAR(100),
    descripcion TEXT,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE,
    CHECK(mesDesde BETWEEN 1 AND 12),
    CHECK(mesHasta BETWEEN 1 AND 12),
    CHECK(anioDesde BETWEEN 1900 AND 2100),
    CHECK(anioHasta BETWEEN 1900 AND 2100),
    CHECK((anioHasta > anioDesde) OR (anioHasta = anioDesde AND mesHasta >= mesDesde))
);

CREATE TABLE IF NOT EXISTS ofertaLaboral (
    id SERIAL PRIMARY KEY,
    idEmpresa INTEGER,
    posicion TEXT NOT NULL,
    descripcion TEXT,
    salario DECIMAL(12,2),
    idRubro INTEGER,
    modalidad VARCHAR(20),
    FOREIGN KEY (idEmpresa) REFERENCES empresa ON DELETE CASCADE,
    FOREIGN KEY (idRubro) REFERENCES rubro ON DELETE SET NULL,
    CHECK(salario > 0)
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
    idOferta INTEGER NOT NULL,
    estado VARCHAR(20) NOT NULL,
    PRIMARY KEY (idUsuario, idOferta),
    FOREIGN KEY (idEmpresa) REFERENCES empresa ON DELETE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES usuario ON DELETE CASCADE,
    FOREIGN KEY (idOferta) REFERENCES ofertaLaboral ON DELETE CASCADE
);
