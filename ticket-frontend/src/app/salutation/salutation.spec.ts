import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Salutation } from './salutation';

describe('Salutation', () => {
  let component: Salutation;
  let fixture: ComponentFixture<Salutation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Salutation],
    }).compileComponents();

    fixture = TestBed.createComponent(Salutation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
