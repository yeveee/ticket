package com.app.ticket.adapter.messaging;

import com.app.ticket.domain.event.TicketCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TicketCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(TicketCreatedEventListener.class);

    @KafkaListener(topics = "ticket-created", groupId = "ticket-notifications")
    public void handle(TicketCreatedEvent event) {
        log.info("[NOTIFICATION] Nouveau ticket cree : '{}' (id={}, auteurId={})",
                event.titre(), event.ticketId(), event.auteurId());
    }
}
