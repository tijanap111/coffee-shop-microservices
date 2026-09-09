import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoyaltyCard } from '../model/loyalty-card';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/USER-SERVICE/users';

  getLoyalty(userId: number): Observable<LoyaltyCard> {
    return this.http.get<LoyaltyCard>(this.apiUrl + '/' + userId + '/loyalty');
  }
}
