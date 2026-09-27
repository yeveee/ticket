package com.app.ticket.port.in;

import com.app.ticket.dto.TicketDTO;

import java.util.List;

public interface TicketUseCase {
    List<TicketDTO> findAll();
    TicketDTO findById(Long id);
    List<TicketDTO> search(String keyword);
    TicketDTO create(TicketDTO dto);
    TicketDTO update(Long id, TicketDTO dto);
    void delete(Long id);
}
