import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TicketService } from '../ticket';

@Component({
  selector: 'app-ticket-form',
  imports: [ReactiveFormsModule],
  templateUrl: './ticket-form.html',
  styleUrl: './ticket-form.css',
})
export class TicketForm implements OnInit {
  ticketId: number | null = null;

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
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
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
      });
    }
  }

  onSubmit(): void {
    const value = this.ticketForm.value as any;

    if (this.ticketId) {
      this.ticketService.update(this.ticketId, value).subscribe(() => {
        this.router.navigate(['/tickets']);
      });
    } else {
      this.ticketService.create(value).subscribe(() => {
        this.router.navigate(['/tickets']);
      });
    }
  }
}
