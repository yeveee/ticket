package com.app.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ticket.dto.CommentaireDTO;
import com.app.ticket.entity.Commentaire;
import com.app.ticket.entity.Ticket;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.repository.CommentaireRepository;
import com.app.ticket.repository.TicketRepository;
import com.app.ticket.repository.UtilisateurRepository;

@Service
public class CommentaireService {
    
    private final TicketRepository ticketRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CommentaireRepository commentaireRepository;

    public CommentaireService(TicketRepository ticketRepository, UtilisateurRepository utilisateurRepository,
            CommentaireRepository commentaireRepository) {
        this.ticketRepository = ticketRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.commentaireRepository = commentaireRepository;
    }

    public List<CommentaireDTO> findAll() {
        return commentaireRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
}

public CommentaireDTO findById(Long id) {
    Commentaire commentaire = commentaireRepository.findById(id).
    orElseThrow(() -> new RuntimeException("Commentaire introuvable"));
    return toDTO(commentaire);
}

private CommentaireDTO toDTO(Commentaire commentaire) {
    return new CommentaireDTO(commentaire.getId(), commentaire.getTexte(), commentaire.getAuteur().getId(), commentaire.getTicket().getId());
}

public CommentaireDTO create(CommentaireDTO dto) {
    Ticket ticket = ticketRepository.findById(dto.getTicketId()).
    orElseThrow(() -> new RuntimeException("Tickets introuvable"));
    Utilisateur auteur = utilisateurRepository.findById(dto.getAuteurId()).
    orElseThrow(() -> new RuntimeException("Auteurs introuvable"));
    Commentaire commentaire = new Commentaire();
    commentaire.setTicket(ticket);;
    commentaire.setTexte(dto.getTexte());
    commentaire.setAuteur(auteur);
    Commentaire saved = commentaireRepository.save(commentaire);
    return toDTO(saved);
}

public void delete(Long id) {
    commentaireRepository.deleteById(id);
}
}
