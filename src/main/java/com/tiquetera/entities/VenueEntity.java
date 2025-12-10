package com.tiquetera.entities;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues", uniqueConstraints = {
    @UniqueConstraint(name = "uk_venue_nombre", columnNames = "nombre")
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
    
    @NotBlank(message = "Venue name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 50)
    private String ciudad;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacidad;
    
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(length = 200)
    private String direccion;
    
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventEntity> events = new ArrayList<>();
    
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
    
    // Helper methods for bidirectional relationship
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }
    
    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}