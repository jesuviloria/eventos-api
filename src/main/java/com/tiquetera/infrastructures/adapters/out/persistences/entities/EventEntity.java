package com.tiquetera.infrastructures.adapters.out.persistences.entities;

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
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @NotBlank
    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;
    
    @NotNull
    @Future
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @NotNull
    @Future
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @NotNull
    private VenueEntity venue;
    
    @NotBlank
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
