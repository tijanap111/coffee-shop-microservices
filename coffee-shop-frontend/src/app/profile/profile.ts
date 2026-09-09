import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user-service';
import { AuthService } from '../service/auth-service';
import { Navbar } from '../navbar/navbar';
import { User } from '../model/user';

@Component({
  selector: 'app-profile',
  imports: [FormsModule, Navbar],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  private userService = inject(UserService);
  private authService = inject(AuthService);

  user: Partial<User> = { firstName: '', lastName: '', email: '' };
  successMessage: string = '';
  errorMessage: string = '';

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.user = data;
      },
    });
  }

  onSubmit(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.userService.updateUser(userId, this.user).subscribe({
      next: () => {
        this.successMessage = 'Podaci uspešno ažurirani.';
        this.errorMessage = '';
      },
      error: () => {
        this.errorMessage = 'Greška prilikom ažuriranja podataka.';
        this.successMessage = '';
      },
    });
  }
}
