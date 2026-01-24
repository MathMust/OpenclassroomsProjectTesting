import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('RegisterComponent Integration', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  // Mock Router pour éviter le warning NgZone
  const routerMock = {
    navigate: jest.fn()
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        BrowserAnimationsModule,
        HttpClientTestingModule
      ],
      providers: [
        AuthService,
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();
  }));

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should register successfully and navigate to /login', () => {
    jest.spyOn(routerMock, 'navigate');

    component.form.patchValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '12345'
    });

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    req.flush(null);

    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should initialize form with empty values', () => {
    const v = component.form.value;
    expect(v.email).toBe('');
    expect(v.firstName).toBe('');
    expect(v.lastName).toBe('');
    expect(v.password).toBe('');
    expect(component.form.valid).toBe(false);
  });

  it('should mark form invalid when fields are invalid', () => {
    component.form.patchValue({
      email: 'invalid',
      firstName: 'A',
      lastName: '',
      password: '12'
    });
    expect(component.form.invalid).toBe(true);

    component.form.patchValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '12345'
    });
    expect(component.form.valid).toBe(true);
  });

  it('should disable submit button when form is invalid', () => {
    component.form.reset();
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true);
  });

  it('should set onError on registration failure', () => {
    component.form.patchValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '12345'
    });

    component.submit();

    const req = httpMock.expectOne('api/auth/register');

    req.flush(
      { message: 'Failed' },
      { status: 400, statusText: 'Error' }
    );

    expect(component.onError).toBe(true);
  });

  it('should display error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();

    const el = fixture.nativeElement.querySelector('.error');
    expect(el).toBeTruthy();
    expect(el.textContent).toContain('An error occurred');
  });

  afterEach(() => {
    httpMock.verify();
  });
});
