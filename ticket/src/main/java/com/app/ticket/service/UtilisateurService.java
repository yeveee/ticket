package com.app.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ticket.dto.UtilisateurDTO;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.enums.Role;
import com.app.ticket.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
}

public UtilisateurDTO findById(Long id) {
    Utilisateur utilisateur = utilisateurRepository.findById(id).
    orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    return toDTO(utilisateur);
}

private UtilisateurDTO toDTO(Utilisateur utilisateur) {
    return new UtilisateurDTO(utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getRole().name());
}

public UtilisateurDTO create(UtilisateurDTO dto) {
    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setNom(dto.getNom());
    utilisateur.setEmail(dto.getEmail());
    utilisateur.setRole(Role.valueOf(dto.getRole()));
    Utilisateur saved = utilisateurRepository.save(utilisateur);
    return toDTO(saved);
}

public void delete(Long id) {
    utilisateurRepository.deleteById(id);
}
}
    

