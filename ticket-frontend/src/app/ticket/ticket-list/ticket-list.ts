import { Component, OnInit, PLATFORM_ID, ChangeDetectorRef, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Nav } from '../../nav/nav';
import { TicketService, Ticket } from '../ticket';

@Component({
  selector: 'app-ticket-list',
  imports: [RouterLink, Nav],
  templateUrl: './ticket-list.html',
  styleUrl: './ticket-list.css',
})
export class TicketList implements OnInit {
  tickets: Ticket[] = [];
  errorMessage: string | null = null;
  private platformId = inject(PLATFORM_ID);

  constructor(
    private ticketService: TicketService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // pas d'appel API pendant le rendu SSR (pas de token disponible côté serveur) :
    // on ne charge qu'une fois vraiment dans le navigateur, après hydratation
    if (isPlatformBrowser(this.platformId)) {
      this.loadTickets();
    }
  }

  loadTickets(): void {
    this.ticketService.getAll().subscribe({
      next: (data) => {
        this.tickets = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tickets', err);
        this.errorMessage = 'Impossible de charger les tickets.';
      }
    });
  }

  deleteTicket(id: number): void {
    this.ticketService.delete(id).subscribe(() => {
      this.loadTickets();
    });
  }
}
