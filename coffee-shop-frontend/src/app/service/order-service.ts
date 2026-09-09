import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../model/order';
import { CreateOrderRequest } from '../model/create-order-request';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/ORDER-SERVICE/orders';

  createOrder(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, request);
  }

  getOrdersByCustomer(customerId: number): Observable<Array<Order>> {
    return this.http.get<Array<Order>>(this.apiUrl + '/customer/' + customerId);
  }

  getOrdersByCustomerEmail(email: string): Observable<Array<Order>> {
    return this.http.get<Array<Order>>(this.apiUrl + '/customer/email/' + email);
  }
}
