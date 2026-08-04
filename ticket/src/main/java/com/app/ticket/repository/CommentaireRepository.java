package com.app.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ticket.entity.Commentaire;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByTicketId(Long ticketId);
}
