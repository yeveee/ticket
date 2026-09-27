package com.app.ticket.domain.event;

public record TicketCreatedEvent(Long ticketId, String titre, Long auteurId) {
}
