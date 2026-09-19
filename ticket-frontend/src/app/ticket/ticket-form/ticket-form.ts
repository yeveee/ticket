import { Component, OnInit, PLATFORM_ID, ChangeDetectorRef, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService } from '../ticket';
import { UtilisateurService, Utilisateur } from '../../utilisateur/utilisateur';
import { ProjetService, Projet } from '../../projet/projet';

@Component({
  selector: 'app-ticket-form',
  imports: [ReactiveFormsModule],
  templateUrl: './ticket-form.html',
  styleUrl: './ticket-form.css',
})
export class TicketForm implements OnInit {
  ticketId: number | null = null;
  errorMessage: string | null = null;
  utilisateurs: Utilisateur[] = [];
  projets: Projet[] = [];
  private platformId = inject(PLATFORM_ID);

  ticketForm = new FormGroup({
    titre: new FormControl('', [Validators.required]),
    description: new FormControl(''),
    priorite: new FormControl(1, [Validators.required]),
    statut: new FormControl('OUVERT', [Validators.required]),
    projetId: new FormControl(0, [Validators.required]),
    auteurId: new FormControl(0, [Validators.required]),
    assigneeId: new FormControl(0, [Validators.required]),
  });

  constructor(
    private ticketService: TicketService,
    private utilisateurService: UtilisateurService,
    private projetService: ProjetService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    this.utilisateurService.getAll().subscribe(data => {
      this.utilisateurs = data;
      this.cdr.detectChanges();
    });
    this.projetService.getAll().subscribe(data => {
      this.projets = data;
      this.cdr.detectChanges();
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.ticketId = Number(idParam);
      this.ticketService.getById(this.ticketId).subscribe(ticket => {
        this.ticketForm.setValue({
          titre: ticket.titre,
          description: ticket.description,
          priorite: ticket.priorite,
          statut: ticket.statut,
          projetId: ticket.projetId,
          auteurId: ticket.auteurId,
          assigneeId: ticket.assigneeId,
        });
        this.cdr.detectChanges();
      });
    }
  }

  onSubmit(): void {
    const value = this.ticketForm.value as any;
    this.errorMessage = null;

    const request = this.ticketId
      ? this.ticketService.update(this.ticketId, value)
      : this.ticketService.create(value);

    request.subscribe({
      next: () => {
        this.router.navigate(['/tickets']);
      },
      error: (err) => {
        console.error('Erreur lors de la sauvegarde du ticket', err);
        this.errorMessage = 'Échec de la sauvegarde — vérifie que les ids projet/auteur/assigné existent.';
      }
    });
  }
}
