/*INSERT INTO users (email, password) VALUES ('foo@bar.com', 'secret');*/
INSERT INTO rubro (nombre) VALUES ('testCategory');
INSERT INTO aptitud (descripcion) VALUES ('testskill');
INSERT INTO usuario (nombre, email, contrasenia, descripcion, idRubro, ubicacion, posicionActual, educacion) VALUES ('John Lennon', 'johnlennon@gmail.com', 'imagineAPassword', null, 1, null, null, null);
INSERT INTO empresa (nombre, email, contrasenia, descripcion, idRubro, ubicacion) VALUES ('Empresaurio', 'empresaurio@gmail.com', '12345678', null, null, null);
INSERT INTO aptitudUsuario (idAptitud, idUsuario)
    SELECT a.id, u.id
    FROM aptitud a, usuario u
    WHERE a.descripcion = 'testskill'
        AND u.email = 'johnlennon@gmail.com';
INSERT INTO ofertaLaboral (idEmpresa, posicion, descripcion, salario, idRubro, modalidad)
    SELECT e.id, 'testPosition', 'testdescription', 1000.99, r.id, 'Remoto'
    FROM empresa e, rubro r
    WHERE e.email = 'empresaurio@gmail.com'
        AND r.nombre = 'testCategory';
INSERT INTO contactado (idEmpresa, idUsuario, idOferta, estado)
    SELECT e.id, u.id, ol.id, 'pendiente'
    FROM empresa e, usuario u, ofertaLaboral ol
    WHERE e.email = 'empresaurio@gmail.com'
        AND u.email = 'johnlennon@gmail.com'
        AND ol.posicion = 'testPosition';
INSERT INTO aptitudOfertaLaboral (idAptitud, idOferta)
    SELECT a.id, o.id
    FROM aptitud a, ofertaLaboral o
    WHERE a.descripcion = 'testskill'
        AND o.posicion = 'testPosition';
INSERT INTO experiencia (idUsuario, mesDesde, anioDesde, mesHasta, anioHasta, empresa, posicion, descripcion)
    SELECT u.id, 11, 2011, 12, 2012, 'Paw Inc.', 'Ceo de Paw Inc.', 'Era el CEO :)'
    FROM usuario u
    WHERE u.email = 'johnlennon@gmail.com';
INSERT INTO educacion (idUsuario, mesDesde, anioDesde, mesHasta, anioHasta, titulo, institucion, descripcion)
    SELECT u.id, 11, 2011, 12, 2012, 'Licenciado en Paw', 'PAW University', 'Una linda facultad'
    FROM usuario u
    WHERE u.email = 'johnlennon@gmail.com';
