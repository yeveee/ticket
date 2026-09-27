package com.app.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ticket.domain.event.TicketCreatedEvent;
import com.app.ticket.dto.TicketDTO;
import com.app.ticket.entity.Projet;
import com.app.ticket.entity.Ticket;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.enums.Statut;
import com.app.ticket.port.in.TicketUseCase;
import com.app.ticket.port.out.TicketEventPublisherPort;
import com.app.ticket.port.out.TicketRepositoryPort;
import com.app.ticket.repository.ProjetRepository;
import com.app.ticket.repository.UtilisateurRepository;

@Service
public class TicketService implements TicketUseCase {

    private final TicketRepositoryPort ticketRepositoryPort;
    private final UtilisateurRepository utilisateurRepository;
    private final ProjetRepository projetRepository;
    private final TicketEventPublisherPort ticketEventPublisherPort;

    public TicketService(TicketRepositoryPort ticketRepositoryPort, UtilisateurRepository utilisateurRepository,
            ProjetRepository projetRepository, TicketEventPublisherPort ticketEventPublisherPort) {
        this.ticketRepositoryPort = ticketRepositoryPort;
        this.utilisateurRepository = utilisateurRepository;
        this.projetRepository = projetRepository;
        this.ticketEventPublisherPort = ticketEventPublisherPort;
    }

    @Override
    public List<TicketDTO> findAll() {
        return toDTOList(ticketRepositoryPort.findAllWithRelations());
    }

    @Override
    public TicketDTO findById(Long id) {
        Ticket ticket = ticketRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));
        return toDTO(ticket);
    }

    @Override
    public void delete(Long id) {
        ticketRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public TicketDTO create(TicketDTO dto) {
        Utilisateur auteur = utilisateurRepository.findById(dto.getAuteurId())
                .orElseThrow(() -> new RuntimeException("Auteur introuvable"));
        Utilisateur assignee = utilisateurRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee introuvable"));
        Projet projet = projetRepository.findById(dto.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        Ticket ticket = new Ticket();
        ticket.setTitre(dto.getTitre());
        ticket.setDescription(dto.getDescription());
        ticket.setPriorite(dto.getPriorite());
        ticket.setStatut(Statut.valueOf(dto.getStatut()));
        ticket.setProjet(projet);
        ticket.setAuteur(auteur);
        ticket.setAssignee(assignee);

        Ticket saved = ticketRepositoryPort.save(ticket);

        ticketEventPublisherPort.publishTicketCreated(
                new TicketCreatedEvent(saved.getId(), saved.getTitre(), saved.getAuteur().getId()));

        return toDTO(saved);
    }

    @Override
    @Transactional
    public TicketDTO update(Long id, TicketDTO dto) {
        Ticket ticket = ticketRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        Utilisateur auteur = utilisateurRepository.findById(dto.getAuteurId())
                .orElseThrow(() -> new RuntimeException("Auteur introuvable"));
        Utilisateur assignee = utilisateurRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee introuvable"));
        Projet projet = projetRepository.findById(dto.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        ticket.setTitre(dto.getTitre());
        ticket.setDescription(dto.getDescription());
        ticket.setPriorite(dto.getPriorite());
        ticket.setStatut(Statut.valueOf(dto.getStatut()));
        ticket.setProjet(projet);
        ticket.setAuteur(auteur);
        ticket.setAssignee(assignee);
        // pas de save() : ticket est managed, Hibernate flush l'UPDATE tout seul

        return toDTO(ticket);
    }

    @Override
    public List<TicketDTO> search(String keyword) {
        return toDTOList(ticketRepositoryPort.findByTitreContainingIgnoreCase(keyword));
    }

    private List<TicketDTO> toDTOList(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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
