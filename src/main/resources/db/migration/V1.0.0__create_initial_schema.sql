-- Create venues table
CREATE TABLE venues (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    ciudad VARCHAR(50) NOT NULL,
    capacidad INTEGER NOT NULL CHECK (capacidad > 0),
    direccion VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create events table
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500) NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    venue_id BIGINT NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    categoria VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
    CONSTRAINT chk_dates CHECK (fecha_fin > fecha_inicio)
);

-- Create indexes for better performance
CREATE INDEX idx_events_venue_id ON events(venue_id);
CREATE INDEX idx_events_ciudad ON events(ciudad);
CREATE INDEX idx_events_categoria ON events(categoria);
CREATE INDEX idx_events_fecha_inicio ON events(fecha_inicio);
CREATE INDEX idx_venues_ciudad ON venues(ciudad);
CREATE INDEX idx_venues_capacidad ON venues(capacidad);

-- Comments
COMMENT ON TABLE venues IS 'Table storing venue information';
COMMENT ON TABLE events IS 'Table storing event information';
COMMENT ON COLUMN events.venue_id IS 'Foreign key to venues table';