import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';

import { TicketService } from './ticket';

describe('TicketService', () => {
  let service: TicketService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(TicketService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should GET the list of tickets', () => {
    service.getAll().subscribe(tickets => {
      expect(tickets.length).toBe(1);
      expect(tickets[0].titre).toBe('Bug');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tickets');
    expect(req.request.method).toBe('GET');
    req.flush([
      { id: 1, titre: 'Bug', description: '', priorite: 1, statut: 'OUVERT', projetId: 1, auteurId: 1, assigneeId: 1 }
    ]);
  });

  it('should DELETE a ticket by id', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/tickets/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
