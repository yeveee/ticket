package com.app.ticket.service;

import com.app.ticket.dto.TicketDTO;
import com.app.ticket.entity.*;
import com.app.ticket.enums.Statut;
import com.app.ticket.repository.TicketRepository;
import com.app.ticket.repository.ProjetRepository;
import com.app.ticket.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ProjetRepository projetRepository;
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void should_return_ticket_when_found() {
        // Arrange
        Projet projet = new Projet();
        projet.setId(3L);
        Utilisateur auteur = new Utilisateur();
        auteur.setId(1L);
        Utilisateur assignee = new Utilisateur();
        assignee.setId(2L);

        Ticket ticket = new Ticket();
        ticket.setId(10L);
        ticket.setTitre("Bug login");
        ticket.setPriorite(3);
        ticket.setStatut(Statut.OUVERT);
        ticket.setProjet(projet);
        ticket.setAuteur(auteur);
        ticket.setAssignee(assignee);

        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));

        // Act
        TicketDTO result = ticketService.findById(10L);

        // Assert
        assertEquals("Bug login", result.getTitre());
        verify(ticketRepository).findById(10L);
    }

    @Test
    void should_throw_when_ticket_not_found() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.findById(999L));
    }
}
