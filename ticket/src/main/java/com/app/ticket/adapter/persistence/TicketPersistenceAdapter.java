package com.app.ticket.adapter.persistence;

import com.app.ticket.entity.Ticket;
import com.app.ticket.port.out.TicketRepositoryPort;
import com.app.ticket.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TicketPersistenceAdapter implements TicketRepositoryPort {

    private final TicketRepository ticketRepository;

    public TicketPersistenceAdapter(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findAllWithRelations() {
        return ticketRepository.findAllWithRelations();
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public List<Ticket> findByTitreContainingIgnoreCase(String keyword) {
        return ticketRepository.findByTitreContainingIgnoreCase(keyword);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }
}
