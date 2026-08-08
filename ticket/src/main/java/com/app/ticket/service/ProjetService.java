package com.app.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ticket.dto.ProjetDTO;
import com.app.ticket.entity.Projet;
import com.app.ticket.repository.ProjetRepository;

@Service
public class ProjetService {
    
    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

public List<ProjetDTO> findAll() {
        return projetRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
}

public ProjetDTO findById(Long id) {
    Projet projet = projetRepository.findById(id).
    orElseThrow(() -> new RuntimeException("Projet introuvable"));
    return toDTO(projet);
}

private ProjetDTO toDTO(Projet projet) {
    return new ProjetDTO(projet.getId(), projet.getNom(), projet.getDescription());
}

public ProjetDTO create(ProjetDTO dto) {
    Projet projet = new Projet();
    projet.setNom(dto.getNom());
    projet.setDescription(dto.getDescription());
    Projet saved = projetRepository.save(projet);
    return toDTO(saved);
}

public void delete(Long id) {
    projetRepository.deleteById(id);
}
}
