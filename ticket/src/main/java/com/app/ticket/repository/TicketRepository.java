package com.app.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ticket.entity.Ticket;

import org.springframework.data.jpa.repository.Query;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByProjetId(Long projetId);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.projet JOIN FETCH t.auteur JOIN FETCH t.assignee")
List<Ticket> findAllWithRelations();
}
