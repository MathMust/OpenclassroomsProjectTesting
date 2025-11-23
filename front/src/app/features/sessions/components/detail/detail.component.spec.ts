import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiMock: any;
  let teacherServiceMock: any;
  let snackBarMock: any;
  let routerMock: any;
  let sessionServiceMock: any;

  const mockSession = {
    id: 1,
    name: 'Yoga Class',
    description: 'A nice yoga session',
    users: [1, 2],
    teacher_id: 1,
    date: new Date('2023-01-01'),
    createdAt: new Date('2023-01-01'),
    updatedAt: new Date('2023-02-01')
  };

  const mockTeacher = {
    id: 1,
    firstName: 'Alice',
    lastName: 'Smith'
  };

  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  beforeEach(async () => {
    sessionApiMock = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(mockTeacher))
    };

    snackBarMock = { open: jest.fn() };
    routerMock = { navigate: jest.fn() };
    sessionServiceMock = { sessionInformation: { id: 1, admin: true } };

    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionApiService, useValue: sessionApiMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher on init', () => {
    expect(sessionApiMock.detail).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(mockSession);
    expect(teacherServiceMock.detail).toHaveBeenCalledWith(mockSession.teacher_id.toString());
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true);
    expect(component.isAdmin).toBe(true);
  });

  it('should call back() and window.history.back', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => { });
    component.back();
    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });

  it('should call delete() and navigate with snackbar', fakeAsync(() => {
    component.delete();
    tick();
    expect(sessionApiMock.delete).toHaveBeenCalledWith('1');
    expect(snackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should participate and refresh session', fakeAsync(() => {
    component.isAdmin = false;
    component.isParticipate = false;
    component.participate();
    tick();
    expect(sessionApiMock.participate).toHaveBeenCalledWith('1', '1');
    expect(sessionApiMock.detail).toHaveBeenCalledTimes(2);
  }));

  it('should unParticipate and refresh session', fakeAsync(() => {
    component.isAdmin = false;
    component.isParticipate = true;
    component.unParticipate();
    tick();
    expect(sessionApiMock.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(sessionApiMock.detail).toHaveBeenCalledTimes(2);
  }));
});
