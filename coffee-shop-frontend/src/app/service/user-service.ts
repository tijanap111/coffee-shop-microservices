import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoyaltyCard } from '../model/loyalty-card';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/user-service/users';

  getLoyalty(userId: number): Observable<LoyaltyCard> {
    return this.http.get<LoyaltyCard>(this.apiUrl + '/' + userId + '/loyalty');
  }

  updateUser(id: number, data: Partial<User>): Observable<User> {
    return this.http.put<User>(this.apiUrl + '/' + id, data);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(this.apiUrl + '/' + id);
  }
}
