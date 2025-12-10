package com.tiquetera.infrastructures.adapters.out.persistences.entities;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "venues",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_venue_nombre", columnNames = "nombre")
    },
    indexes = {
        @Index(name = "idx_venues_ciudad", columnList = "ciudad"),
        @Index(name = "idx_venues_capacidad", columnList = "capacidad")
    }
)
@NamedQueries({
    @NamedQuery(
        name = "VenueEntity.findByCiudadOrderByCapacidad",
        query = "SELECT v FROM VenueEntity v WHERE v.ciudad = :ciudad ORDER BY v.capacidad DESC"
    ),
    @NamedQuery(
        name = "VenueEntity.findLargeVenues",
        query = "SELECT v FROM VenueEntity v WHERE v.capacidad >= :minCapacidad ORDER BY v.capacidad DESC"
    )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "events")
@EqualsAndHashCode(of = "id")
public class VenueEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @NotBlank
    @Column(nullable = false, length = 50)
    private String ciudad;
    
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacidad;
    
    @Size(max = 200)
    @Column(length = 200)
    private String direccion;
    
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<EventEntity> events = new ArrayList<>();
    
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
    
    // Helper methods
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }
    
    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}