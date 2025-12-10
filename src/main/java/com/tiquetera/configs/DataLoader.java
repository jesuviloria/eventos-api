package com.tiquetera.configs;

import com.tiquetera.dtos.EventDTO;
import com.tiquetera.dtos.VenueDTO;
import com.tiquetera.services.EventService;
import com.tiquetera.services.VenueService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class DataLoader {
    
    @Inject
    VenueService venueService;
    
    @Inject
    EventService eventService;
    
    void onStart(@Observes StartupEvent ev) {
        // Crear venues de ejemplo
        VenueDTO venue1 = venueService.create(VenueDTO.builder()
            .nombre("Estadio Nacional")
            .ciudad("Bogotá")
            .capacidad(50000)
            .direccion("Calle 57 # 30-10")
            .build());
        
        VenueDTO venue2 = venueService.create(VenueDTO.builder()
            .nombre("Movistar Arena")
            .ciudad("Bogotá")
            .capacidad(15000)
            .direccion("Calle 63 # 48-45")
            .build());
        
        VenueDTO venue3 = venueService.create(VenueDTO.builder()
            .nombre("Teatro Colón")
            .ciudad("Bogotá")
            .capacidad(1200)
            .direccion("Calle 10 # 5-32")
            .build());
        
        // Crear eventos de ejemplo
        eventService.create(EventDTO.builder()
            .nombre("Concierto Rock en Vivo")
            .descripcion("Gran concierto de rock con las mejores bandas nacionales")
            .fechaInicio(LocalDateTime.now().plusDays(30))
            .fechaFin(LocalDateTime.now().plusDays(30).plusHours(5))
            .venueId(venue1.getId())
            .ciudad("Bogotá")
            .categoria("Música")
            .build());
        
        eventService.create(EventDTO.builder()
            .nombre("Festival de Jazz")
            .descripcion("Festival internacional de jazz con artistas de renombre")
            .fechaInicio(LocalDateTime.now().plusDays(45))
            .fechaFin(LocalDateTime.now().plusDays(45).plusHours(6))
            .venueId(venue2.getId())
            .ciudad("Bogotá")
            .categoria("Música")
            .build());
        
        eventService.create(EventDTO.builder()
            .nombre("Obra de Teatro Clásica")
            .descripcion("Presentación de obras maestras del teatro universal")
            .fechaInicio(LocalDateTime.now().plusDays(15))
            .fechaFin(LocalDateTime.now().plusDays(15).plusHours(3))
            .venueId(venue3.getId())
            .ciudad("Bogotá")
            .categoria("Teatro")
            .build());
        
        System.out.println("✅ Datos de ejemplo cargados exitosamente");
    }
}
