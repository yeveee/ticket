import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { TicketList } from './ticket/ticket-list/ticket-list';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'tickets', component: TicketList, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
