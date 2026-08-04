package com.app.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentaireDTO {

    private Long id;
    private String texte;
    private Long auteurId;
    private Long ticketId;

}
