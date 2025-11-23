import { TestBed } from '@angular/core/testing';
import { take } from 'rxjs';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    id: 1,
    admin: true,
    token: 'fake-jwt-token',
    type: 'user',
    username: 'johndoe',
    firstName: 'John',
    lastName: 'Doe'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have initial state', (done) => {
    service.$isLogged().pipe(take(1)).subscribe(isLogged => {
      expect(isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
      done();
    });
  });

  it('logIn() should set user and isLogged to true', (done) => {
    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);

    service.$isLogged().pipe(take(1)).subscribe(isLogged => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('logOut() should clear user and set isLogged to false', (done) => {
    service.logIn(mockUser);

    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().pipe(take(1)).subscribe(isLogged => {
      expect(isLogged).toBe(false);
      done();
    });
  });
});
