import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TicketService, Ticket } from '../ticket';

@Component({
  selector: 'app-ticket-list',
  imports: [RouterLink],
  templateUrl: './ticket-list.html',
  styleUrl: './ticket-list.css',
})
export class TicketList implements OnInit {
  tickets: Ticket[] = [];

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.loadTickets();
  }

  loadTickets(): void {
    this.ticketService.getAll().subscribe(data => {
      this.tickets = data;
    });
  }

  deleteTicket(id: number): void {
    this.ticketService.delete(id).subscribe(() => {
      this.loadTickets();
    });
  }
}
