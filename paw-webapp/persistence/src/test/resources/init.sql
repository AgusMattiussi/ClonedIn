/*INSERT INTO users (email, password) VALUES ('foo@bar.com', 'secret');*/
INSERT INTO rubro (nombre) VALUES ('testCategory');
INSERT INTO aptitud (descripcion) VALUES ('testskill');
INSERT INTO usuario (nombre, email, contrasenia, descripcion, idRubro, ubicacion, posicionActual, educacion) VALUES ('John Lennon', 'johnlennon@gmail.com', 'imagineAPassword', null, null, null, null, null);
INSERT INTO empresa (nombre, email, contrasenia, descripcion, idRubro, ubicacion) VALUES ('Empresaurio', 'empresaurio@gmail.com', '12345678', null, null, null);
INSERT INTO aptitudUsuario (idAptitud, idUsuario)
    SELECT a.id, u.id
    FROM aptitud a, usuario u
    WHERE a.descripcion = 'testskill'
        AND u.email = 'johnlennon@gmail.com';
INSERT INTO contactado (idEmpresa, idUsuario)
    SELECT e.id, u.id
    FROM empresa e, usuario u
    WHERE e.email = 'empresaurio@gmail.com'
        AND u.email = 'johnlennon@gmail.com';
-- INSERT INTO experienca (idEmpresa, fechaDesde, fechaHasta, empresa, posicion, descipcion) VALUES (1, '2010-10-10', null, null, 'CEO', null);


