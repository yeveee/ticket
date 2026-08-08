package com.app.ticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ticket.dto.CommentaireDTO;
import com.app.ticket.service.CommentaireService;

@RestController
@RequestMapping("/api/commentaires")
public class CommentaireController {

    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @GetMapping
    public List<CommentaireDTO> findAll() {
        return commentaireService.findAll();
    }

    @GetMapping("/{id}")
    public CommentaireDTO findById(@PathVariable Long id) {
        return commentaireService.findById(id);
    }

    @PostMapping
    public CommentaireDTO create(@RequestBody CommentaireDTO dto) {
        return commentaireService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentaireService.delete(id);
    }

}
