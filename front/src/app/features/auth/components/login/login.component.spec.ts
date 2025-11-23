import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('LoginComponent Integration', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let httpMock: HttpTestingController;

  // MOCK Router pour éviter le warning
  const routerMock = {
    navigate: jest.fn()
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
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
        SessionService,
        { provide: Router, useValue: routerMock } // <-- mock ici
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();
  }));

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    const formValue = component.form.value;
    expect(formValue.email).toBe('');
    expect(formValue.password).toBe('');
    expect(component.form.valid).toBe(false);
  });

  it('should toggle password visibility', () => {
    expect(component.hide).toBe(true);
    component.hide = !component.hide;
    expect(component.hide).toBe(false);
  });

  it('should mark form invalid when email or password is invalid', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    expect(component.form.invalid).toBe(true);

    component.form.controls['email'].setValue('invalid-email');
    component.form.controls['password'].setValue('12');
    expect(component.form.invalid).toBe(true);

    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('123');
    expect(component.form.valid).toBe(true);
  });

  it('should login successfully and navigate', () => {
    jest.spyOn(sessionService, 'logIn');
    jest.spyOn(routerMock, 'navigate');

    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('123');

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');

    req.flush({ token: 'abc123', id: 1 });

    expect(sessionService.logIn).toHaveBeenCalledWith({ token: 'abc123', id: 1 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login failure', () => {
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('123');

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    req.flush({ message: 'Invalid login' }, { status: 401, statusText: 'Unauthorized' });

    expect(component.onError).toBe(true);
  });

  it('should disable submit button when form is invalid', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true);
  });

  it('should display error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();

    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  afterEach(() => {
    httpMock.verify();
  });
});
