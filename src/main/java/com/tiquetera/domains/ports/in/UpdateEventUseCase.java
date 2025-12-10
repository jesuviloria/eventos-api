package com.tiquetera.domains.ports.in;

import com.tiquetera.domains.models.Event;

public interface UpdateEventUseCase {
    Event execute(Long id, Event event);
}
