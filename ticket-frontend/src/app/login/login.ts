import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]),
    motDePasse: new FormControl('', [Validators.required])
  });

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.login(this.loginForm.value as any).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        console.log('Connecté !', response.token);
      },
      error: (err) => {
        console.error('Echec de connexion', err)
      }
    })
  }
}
