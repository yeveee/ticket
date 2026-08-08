package com.app.ticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ticket.dto.UtilisateurDTO;
import com.app.ticket.service.UtilisateurService;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<UtilisateurDTO> findAll() {
        return utilisateurService.findAll();
    }

    @GetMapping("/{id}")
    public UtilisateurDTO findById(@PathVariable Long id) {
        return utilisateurService.findById(id);
    }

    @PostMapping
    public UtilisateurDTO create(@RequestBody UtilisateurDTO dto) {
        return utilisateurService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        utilisateurService.delete(id);
    }
    
}
  