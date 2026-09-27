package com.app.ticket.port.out;

import com.app.ticket.domain.event.TicketCreatedEvent;

public interface TicketEventPublisherPort {
    void publishTicketCreated(TicketCreatedEvent event);
}
