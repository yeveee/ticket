package com.app.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String titre;
    private String description;
    private int priorite;
    private String statut;
    private Long projetId;
    private Long auteurId;
    private Long assigneeId;
    
}
