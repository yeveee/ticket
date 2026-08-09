package com.app.ticket.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.ticket.dto.AuthResponseDTO;
import com.app.ticket.dto.LoginRequestDTO;
import com.app.ticket.dto.RegisterRequestDTO;
import com.app.ticket.dto.UtilisateurDTO;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.enums.Role;
import com.app.ticket.exception.InvalidCredentialsException;
import com.app.ticket.repository.UtilisateurRepository;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UtilisateurDTO register(RegisterRequestDTO dto) {
        if (utilisateurRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est deja utilise");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setRole(Role.valueOf(dto.getRole()));


        Utilisateur saved = utilisateurRepository.save(utilisateur);
        return new UtilisateurDTO(saved.getId(), saved.getNom(), saved.getEmail(), saved.getRole().name());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(dto.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(utilisateur);
        return new AuthResponseDTO(token);
    }
    
}
