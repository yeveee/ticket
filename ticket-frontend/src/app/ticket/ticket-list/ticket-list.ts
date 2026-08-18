import { Component, OnInit } from '@angular/core';
import { TicketService, Ticket } from '../ticket';

@Component({
  selector: 'app-ticket-list',
  imports: [],
  templateUrl: './ticket-list.html',
  styleUrl: './ticket-list.css',
})
export class TicketList implements OnInit{
  tickets: Ticket[] = [];

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.ticketService.getAll().subscribe(data => {
      this.tickets = data;
    });
  }
}
