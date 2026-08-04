package com.app.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ticket.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByProjetId(Long projetId);
}
