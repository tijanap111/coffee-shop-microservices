import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest } from '../model/login-request';
import { RegisterRequest } from '../model/register-request';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/USER-SERVICE/users';

  login(credentials: LoginRequest): Observable<string> {
    const params = `?email=${credentials.email}&password=${credentials.password}`;
    return this.http.post(this.apiUrl + '/login' + params, {}, { responseType: 'text' }).pipe(
      tap((token) => {
        localStorage.setItem('token', token);
      })
    );
  }

  register(request: RegisterRequest): Observable<User> {
    return this.http.post<User>(this.apiUrl + '/register', request);
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role;
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.userId;
  }
}
