package com.app.ticket.service;

import com.app.ticket.dto.AuthResponseDTO;
import com.app.ticket.dto.LoginRequestDTO;
import com.app.ticket.dto.RegisterRequestDTO;
import com.app.ticket.dto.UtilisateurDTO;
import com.app.ticket.entity.Utilisateur;
import com.app.ticket.enums.Role;
import com.app.ticket.exception.InvalidCredentialsException;
import com.app.ticket.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void should_throw_when_email_already_used() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Bob", "bob@test.com", "motdepasse123", "DEVELOPPEUR");
        when(utilisateurRepository.findByEmail("bob@test.com")).thenReturn(Optional.of(new Utilisateur()));

        assertThrows(RuntimeException.class, () -> authService.register(dto));
        verify(utilisateurRepository, never()).save(any());
    }

    @Test
    void should_hash_password_and_save_when_registering() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Bob", "bob@test.com", "motdepasse123", "DEVELOPPEUR");
        when(utilisateurRepository.findByEmail("bob@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("motdepasse123")).thenReturn("hash-bcrypt");

        Utilisateur saved = new Utilisateur();
        saved.setId(1L);
        saved.setNom("Bob");
        saved.setEmail("bob@test.com");
        saved.setRole(Role.DEVELOPPEUR);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(saved);

        UtilisateurDTO result = authService.register(dto);

        assertEquals("bob@test.com", result.getEmail());
        verify(passwordEncoder).encode("motdepasse123");
    }

    @Test
    void should_throw_when_email_not_found_on_login() {
        LoginRequestDTO dto = new LoginRequestDTO("inconnu@test.com", "motdepasse123");
        when(utilisateurRepository.findByEmail("inconnu@test.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));
    }

    @Test
    void should_throw_when_password_is_wrong() {
        LoginRequestDTO dto = new LoginRequestDTO("bob@test.com", "mauvais-mdp");
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("bob@test.com");
        utilisateur.setMotDePasse("hash-bcrypt");

        when(utilisateurRepository.findByEmail("bob@test.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais-mdp", "hash-bcrypt")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));
    }

    @Test
    void should_return_token_when_login_succeeds() {
        LoginRequestDTO dto = new LoginRequestDTO("bob@test.com", "motdepasse123");
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("bob@test.com");
        utilisateur.setMotDePasse("hash-bcrypt");

        when(utilisateurRepository.findByEmail("bob@test.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("motdepasse123", "hash-bcrypt")).thenReturn(true);
        when(jwtService.generateToken(utilisateur)).thenReturn("un-faux-jwt");

        AuthResponseDTO result = authService.login(dto);

        assertEquals("un-faux-jwt", result.getToken());
    }
}
