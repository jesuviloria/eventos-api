package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Venue;

public interface UpdateVenueUseCase {
    Venue execute(Long id, Venue venue);
}
