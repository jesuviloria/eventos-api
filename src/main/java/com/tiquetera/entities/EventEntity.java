package com.tiquetera.entities;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", uniqueConstraints = {
    @UniqueConstraint(name = "uk_event_nombre", columnNames = "nombre")
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
    
    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String descripcion;
    
    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @NotNull(message = "Venue is required")
    private VenueEntity venue;
    
    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 50)
    private String ciudad;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
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