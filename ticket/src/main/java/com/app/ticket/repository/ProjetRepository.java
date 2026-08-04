package com.app.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ticket.entity.Projet;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
    
}
