import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { LoginRequest } from '../model/login-request';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForma!: FormGroup;
  errorMessage: string = '';

  ngOnInit(): void {
    this.loginForma = this.fb.group({
      email: this.fb.control(null, [Validators.required, Validators.email]),
      password: this.fb.control(null, [Validators.required]),
    });
  }

  get getterZaEmail() {
    return this.loginForma.get('email');
  }

  get getterZaLozinku() {
    return this.loginForma.get('password');
  }

  submit(): void {
    if (this.loginForma.invalid) return;

    const credentials: LoginRequest = {
      email: this.getterZaEmail?.value,
      password: this.getterZaLozinku?.value,
    };

    this.authService.login(credentials).subscribe({
      next: () => {
        this.router.navigate(['/menu']);
      },
      error: (err) => {
        this.errorMessage = 'Pogrešan email ili lozinka.';
      },
    });
  }
}
