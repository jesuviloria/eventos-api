package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Event;

public interface CreateEventUseCase {
    Event execute(Event event);
}

