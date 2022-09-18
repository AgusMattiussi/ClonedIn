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
INSERT INTO ofertaLaboral (idEmpresa, posicion, descripcion, salario, idRubro)
    SELECT e.id, 'testPosition', 'testdescription', 1000.99, r.id
    FROM empresa e, rubro r
    WHERE e.email = 'empresaurio@gmail.com'
        AND r.nombre = 'testCategory';
INSERT INTO aptitudOfertaLaboral (idAptitud, idOferta)
    SELECT a.id, o.id
    FROM aptitud a, ofertaLaboral o
    WHERE a.descripcion = 'testskill'
        AND o.posicion = 'testPosition';
INSERT INTO experiencia (idUsuario, fechaDesde, fechaHasta, empresa, posicion, descripcion)
    SELECT u.id, '2011-11-11', null, 'Paw Inc.', 'Ceo de Paw Inc.', 'Era el CEO :)'
    FROM usuario u
    WHERE u.email = 'johnlennon@gmail.com';
