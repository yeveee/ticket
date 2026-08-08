package com.app.ticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ticket.dto.TicketDTO;
import com.app.ticket.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketDTO> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/{id}")
    public TicketDTO findById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @PostMapping
    public TicketDTO create(@RequestBody TicketDTO dto) {
        return ticketService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }
}
