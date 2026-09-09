import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { RegisterRequest } from '../model/register-request';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  registerForm!: FormGroup;
  errorMessage: string = '';

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstName: this.fb.control(null, [Validators.required, Validators.minLength(2)]),
      lastName: this.fb.control(null, [Validators.required, Validators.minLength(2)]),
      email: this.fb.control(null, [Validators.required, Validators.email]),
      password: this.fb.control(null, [Validators.required, Validators.minLength(6)]),
    });
  }

  get firstNameControl() {
    return this.registerForm.get('firstName');
  }

  get lastNameControl() {
    return this.registerForm.get('lastName');
  }

  get emailControl() {
    return this.registerForm.get('email');
  }

  get passwordControl() {
    return this.registerForm.get('password');
  }

  submit(): void {
    if (this.registerForm.invalid) return;

    const request: RegisterRequest = {
      firstName: this.firstNameControl?.value,
      lastName: this.lastNameControl?.value,
      email: this.emailControl?.value,
      password: this.passwordControl?.value,
    };

    this.authService.register(request).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = 'Registracija nije uspela. Proverite podatke.';
      },
    });
  }
}
