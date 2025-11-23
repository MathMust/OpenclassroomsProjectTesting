import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { expect } from '@jest/globals';
import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { AuthService } from './features/auth/services/auth.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    sessionServiceMock = {
      $isLogged: jest.fn(() => of(true)),
      logOut: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [MatToolbarModule],
      declarations: [AppComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: AuthService, useValue: {} } // mock vide pour AuthService
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // ignore <router-outlet> et routerLink
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('$isLogged should return observable from sessionService', (done) => {
    component.$isLogged().subscribe((value) => {
      expect(value).toBe(true);
      done();
    });
    expect(sessionServiceMock.$isLogged).toHaveBeenCalled();
  });

  it('logout should call sessionService.logOut and router.navigate', () => {
    component.logout();
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });
});
