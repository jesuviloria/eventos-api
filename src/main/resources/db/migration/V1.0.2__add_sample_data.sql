-- Insert sample venues (asegurándonos que los timestamps se generen)
INSERT INTO venues (nombre, ciudad, capacidad, direccion, created_at, updated_at) VALUES
('Movistar Arena', 'Bogotá', 15000, 'Calle 63 # 48-45', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Estadio El Campín', 'Bogotá', 36000, 'Carrera 30 # 57-60', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Teatro Colón', 'Bogotá', 1200, 'Calle 10 # 5-32', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Estadio Atanasio Girardot', 'Medellín', 40043, 'Carrera 70 # 49-53', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Plaza de Toros La Macarena', 'Medellín', 12000, 'Calle 44 # 63-80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample events (asegurándonos que los timestamps se generen)
INSERT INTO events (nombre, descripcion, fecha_inicio, fecha_fin, venue_id, ciudad, categoria, created_at, updated_at) VALUES
('Concierto Rock 2026', 'Gran concierto de rock internacional', '2026-03-15 20:00:00', '2026-03-15 23:30:00', 1, 'Bogotá', 'Música', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Festival de Jazz', 'Festival internacional de jazz', '2026-04-20 19:00:00', '2026-04-20 23:00:00', 1, 'Bogotá', 'Música', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Partido de Fútbol', 'Clásico capitalino', '2026-05-10 16:00:00', '2026-05-10 18:00:00', 2, 'Bogotá', 'Deportes', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Obra de Teatro Clásica', 'Representación de teatro español', '2026-06-05 18:00:00', '2026-06-05 21:00:00', 3, 'Bogotá', 'Teatro', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Concierto Pop Medellín', 'Artistas internacionales', '2026-07-12 20:00:00', '2026-07-12 23:00:00', 4, 'Medellín', 'Música', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);