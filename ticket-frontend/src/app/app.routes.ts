import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { TicketList } from './ticket/ticket-list/ticket-list';
import { TicketForm } from './ticket/ticket-form/ticket-form';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'tickets', component: TicketList, canActivate: [authGuard] },
  { path: 'tickets/new', component: TicketForm, canActivate: [authGuard] },
  { path: 'tickets/:id/edit', component: TicketForm, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
