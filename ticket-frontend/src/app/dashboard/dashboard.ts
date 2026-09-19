import { Component, OnInit, PLATFORM_ID, ChangeDetectorRef, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Nav } from '../nav/nav';
import { TicketService, Ticket } from '../ticket/ticket';

@Component({
  selector: 'app-dashboard',
  imports: [Nav],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  total = 0;
  ouverts = 0;
  enCours = 0;
  resolus = 0;
  private platformId = inject(PLATFORM_ID);

  constructor(
    private ticketService: TicketService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    this.ticketService.getAll().subscribe((tickets: Ticket[]) => {
      this.total = tickets.length;
      this.ouverts = tickets.filter(t => t.statut === 'OUVERT').length;
      this.enCours = tickets.filter(t => t.statut === 'EN_COURS').length;
      this.resolus = tickets.filter(t => t.statut === 'RESOLU').length;
      this.cdr.detectChanges();
    });
  }
}
