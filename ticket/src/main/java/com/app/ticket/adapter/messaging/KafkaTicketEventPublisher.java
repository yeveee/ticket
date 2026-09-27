package com.app.ticket.adapter.messaging;

import com.app.ticket.domain.event.TicketCreatedEvent;
import com.app.ticket.port.out.TicketEventPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTicketEventPublisher implements TicketEventPublisherPort {

    private static final String TOPIC = "ticket-created";

    private final KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;

    public KafkaTicketEventPublisher(KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishTicketCreated(TicketCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.ticketId().toString(), event);
    }
}
