package com.app.ticket.port.out;

import com.app.ticket.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryPort {
    List<Ticket> findAllWithRelations();
    Optional<Ticket> findById(Long id);
    List<Ticket> findByTitreContainingIgnoreCase(String keyword);
    Ticket save(Ticket ticket);
    void deleteById(Long id);
}
