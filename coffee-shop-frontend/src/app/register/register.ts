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

  registerForma!: FormGroup;
  errorMessage: string = '';

  ngOnInit(): void {
    this.registerForma = this.fb.group({
      firstName: this.fb.control(null, [Validators.required, Validators.minLength(2)]),
      lastName: this.fb.control(null, [Validators.required, Validators.minLength(2)]),
      email: this.fb.control(null, [Validators.required, Validators.email]),
      password: this.fb.control(null, [Validators.required, Validators.minLength(6)]),
    });
  }

  get getterZaIme() {
    return this.registerForma.get('firstName');
  }

  get getterZaPrezime() {
    return this.registerForma.get('lastName');
  }

  get getterZaEmail() {
    return this.registerForma.get('email');
  }

  get getterZaLozinku() {
    return this.registerForma.get('password');
  }

  submit(): void {
    if (this.registerForma.invalid) return;

    const request: RegisterRequest = {
      firstName: this.getterZaIme?.value,
      lastName: this.getterZaPrezime?.value,
      email: this.getterZaEmail?.value,
      password: this.getterZaLozinku?.value,
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
