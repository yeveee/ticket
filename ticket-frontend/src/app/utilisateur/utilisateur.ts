import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Utilisateur {
  id: number;
  nom: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class UtilisateurService {
  private apiUrl = '/api/utilisateurs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(this.apiUrl);
  }
}
