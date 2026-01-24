import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiMock: any;
  let teacherServiceMock: any;
  let snackBarMock: any;
  let routerMock: any;

  const mockSessionService = {
    sessionInformation: { admin: true }
  };

  const mockTeachers = of([
    { id: 1, firstName: 'Alice', lastName: 'Smith' },
    { id: 2, firstName: 'Bob', lastName: 'Johnson' }
  ]);

  beforeEach(async () => {
    sessionApiMock = {
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        name: 'Yoga Class',
        date: '2023-01-01',
        teacher_id: 1,
        description: 'Test description'
      })),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({}))
    };

    teacherServiceMock = {
      all: jest.fn().mockReturnValue(mockTeachers)
    };

    snackBarMock = { open: jest.fn() };
    routerMock = { url: '/sessions/create', navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: jest.fn() } } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate away if user is not admin', () => {
    component['sessionService']!.sessionInformation!.admin = false;
    component.ngOnInit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should initialize form with empty values when creating', () => {
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.controls['name'].value).toBe('');
    expect(component.sessionForm?.controls['description'].value).toBe('');
  });

  it('should mark form invalid when values is invalid', () => {
    component.sessionForm?.controls['name'].setValue('');
    component.sessionForm?.controls['date'].setValue('2026-01-01');
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('test');
    expect(component.sessionForm?.invalid).toBe(true);

    component.sessionForm?.controls['name'].setValue('Yoga');
    component.sessionForm?.controls['date'].setValue('');
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('test');
    expect(component.sessionForm?.invalid).toBe(true);

    component.sessionForm?.controls['name'].setValue('Yoga');
    component.sessionForm?.controls['date'].setValue('2026-01-01');
    component.sessionForm?.controls['teacher_id'].setValue(null);
    component.sessionForm?.controls['description'].setValue('test');
    expect(component.sessionForm?.invalid).toBe(true);

    component.sessionForm?.controls['name'].setValue('Yoga');
    component.sessionForm?.controls['date'].setValue('2026-01-01');
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('');
    expect(component.sessionForm?.invalid).toBe(true);

  });

  it('should initialize form with session data on update', fakeAsync(() => {
    component.onUpdate = true;
    component['id'] = '1';
    routerMock.url = '/sessions/update/1'; // important pour passer dans le bloc update
    sessionApiMock.detail.mockReturnValue(of({
      id: 1,
      name: 'Yoga',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Desc'
    }));

    component.ngOnInit();
    tick();

    expect(component.sessionForm?.value.name).toBe('Yoga');
    expect(component.sessionForm?.value.description).toBe('Desc');
  }));

  it('should call create() on submit when onUpdate is false', fakeAsync(() => {
    component.onUpdate = false;
    component.sessionForm?.patchValue({
      name: 'Test Session',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Description'
    });
    component.submit();
    tick();
    expect(sessionApiMock.create).toHaveBeenCalled();
    expect(snackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should call update() on submit when onUpdate is true', fakeAsync(() => {
    component.onUpdate = true;
    component['id'] = '1';
    routerMock.url = '/sessions/update/1';
    component.sessionForm?.patchValue({
      name: 'Test Session',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Description'
    });
    component.submit();
    tick();
    expect(sessionApiMock.update).toHaveBeenCalledWith('1', expect.any(Object));
    expect(snackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should handle submit when sessionForm is undefined', () => {
    component['sessionForm'] = undefined;
    expect(() => component.submit()).not.toThrow();
  });

});
