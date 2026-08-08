package com.app.ticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ticket.dto.ProjetDTO;
import com.app.ticket.service.ProjetService;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    @GetMapping
    public List<ProjetDTO> findAll() {
        return projetService.findAll();
    }

    @GetMapping("/{id}")
    public ProjetDTO findById(@PathVariable Long id) {
        return projetService.findById(id);
    }

    @PostMapping
    public ProjetDTO create(@RequestBody ProjetDTO dto) {
        return projetService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projetService.delete(id);
    }

}
