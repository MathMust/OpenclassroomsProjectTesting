import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call all()', () => {
    const mockSessions: Session[] = [];
    service.all().subscribe(res => expect(res).toEqual(mockSessions));

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should call detail(id)', () => {
    const mockSession: Session = { id: 1, name: 'Yoga', date: new Date('2023-01-01'), teacher_id: 1, description: 'Desc', users: [] };
    service.detail('1').subscribe(res => expect(res).toEqual(mockSession));

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should call delete(id)', () => {
    service.delete('1').subscribe(res => expect(res).toEqual({}));

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should call create(session)', () => {
    const session: Session = { id: 1, name: 'Yoga', date: new Date('2023-01-01'), teacher_id: 1, description: 'Desc', users: [] };
    service.create(session).subscribe(res => expect(res).toEqual(session));

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session);
    req.flush(session);
  });

  it('should call update(id, session)', () => {
    const session: Session = { id: 1, name: 'Yoga', date: new Date('2023-01-01'), teacher_id: 1, description: 'Desc', users: [] };
    service.update('1', session).subscribe(res => expect(res).toEqual(session));

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(session);
    req.flush(session);
  });

  it('should call participate(id, userId)', () => {
    service.participate('1', '2').subscribe(res => expect(res).toBeUndefined());

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('should call unParticipate(id, userId)', () => {
    service.unParticipate('1', '2').subscribe(res => expect(res).toBeUndefined());

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
