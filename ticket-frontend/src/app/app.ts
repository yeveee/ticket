import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Salutation } from './salutation/salutation';
import { LoginComponent } from './login/login';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Salutation, LoginComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ticket-frontend');
}
