package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Venue;

public interface CreateVenueUseCase {
    Venue execute(Venue venue);
}
