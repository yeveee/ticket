package com.app.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ticket.dto.TicketDTO;
import com.app.ticket.entity.Projet;
import com.app.ticket.entity.Ticket;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.enums.Statut;
import com.app.ticket.repository.ProjetRepository;
import com.app.ticket.repository.TicketRepository;
import com.app.ticket.repository.UtilisateurRepository;

@Service
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProjetRepository projetRepository;

    public TicketService(TicketRepository ticketRepository, UtilisateurRepository utilisateurRepository, ProjetRepository projetRepository) {
        this.ticketRepository = ticketRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.projetRepository = projetRepository;
    }

    public List<TicketDTO> findAll() {
        return ticketRepository.findAllWithRelations().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
}

public TicketDTO findById(Long id) {
    Ticket ticket = ticketRepository.findById(id).
    orElseThrow(() -> new RuntimeException("Ticket introuvable"));
    return toDTO(ticket);
}

public void delete(Long id) {
    ticketRepository.deleteById(id);
}
    @Transactional
    public TicketDTO create(TicketDTO dto) {
        Utilisateur auteur = utilisateurRepository.findById(dto.getAuteurId()).
        orElseThrow(() -> new RuntimeException("Auteur introuvable"));
        Utilisateur assignee = utilisateurRepository.findById(dto.getAssigneeId()).
        orElseThrow(() -> new RuntimeException("Assignee introuvable"));
        Projet projet = projetRepository.findById(dto.getProjetId()).orElseThrow(() -> new RuntimeException("Projet introuvable"));
        Ticket ticket = new Ticket();
        ticket.setTitre(dto.getTitre());
        ticket.setDescription(dto.getDescription());
        ticket.setPriorite(dto.getPriorite());
        ticket.setStatut(Statut.valueOf(dto.getStatut()));
        ticket.setProjet(projet);
        ticket.setAuteur(auteur);
        ticket.setAssignee(assignee);
        Ticket saved = ticketRepository.save(ticket);
        return toDTO(saved);


    }

    private TicketDTO toDTO(Ticket ticket) {
    return new TicketDTO(ticket.getId(), 
    ticket.getTitre(), 
    ticket.getDescription(), 
    ticket.getPriorite(), 
    ticket.getStatut().name(), 
    ticket.getProjet().getId(),
    ticket.getAuteur().getId(), 
    ticket.getAssignee().getId());
}

    
}
