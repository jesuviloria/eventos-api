package com.tiquetera.infrastructures.adapters.out.persistences.entities;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "events",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_event_nombre", columnNames = "nombre")
    },
    indexes = {
        @Index(name = "idx_events_venue_id", columnList = "venue_id"),
        @Index(name = "idx_events_ciudad", columnList = "ciudad"),
        @Index(name = "idx_events_categoria", columnList = "categoria"),
        @Index(name = "idx_events_fecha_inicio", columnList = "fecha_inicio")
    }
)
@NamedQueries({
    @NamedQuery(
        name = "EventEntity.findUpcomingEvents",
        query = "SELECT e FROM EventEntity e WHERE e.fechaInicio > CURRENT_TIMESTAMP ORDER BY e.fechaInicio"
    ),
    @NamedQuery(
        name = "EventEntity.findByCiudadAndCategoria",
        query = "SELECT e FROM EventEntity e WHERE e.ciudad = :ciudad AND e.categoria = :categoria"
    ),
    @NamedQuery(
        name = "EventEntity.findWithVenue",
        query = "SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.id = :id"
    )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "venue")
@EqualsAndHashCode(of = "id")
public class EventEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @NotBlank
    @Size(max = 500)
    @Column(length = 500, nullable = false)
    private String descripcion;
    
    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @NotNull
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private VenueEntity venue;
    
    @NotBlank
    @Column(nullable = false, length = 50)
    private String ciudad;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
