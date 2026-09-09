import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { OrderService } from '../service/order-service';
import { UserService } from '../service/user-service';
import { AuthService } from '../service/auth-service';
import { Order } from '../model/order';
import { LoyaltyCard } from '../model/loyalty-card';
import {Navbar} from '../navbar/navbar';

@Component({
  selector: 'app-orders',
  imports: [CurrencyPipe, DatePipe, Navbar],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders implements OnInit {
  private orderService = inject(OrderService);
  private userService = inject(UserService);
  private authService = inject(AuthService);

  orders = signal<Array<Order>>([]);
  loyalty = signal<LoyaltyCard | null>(null);

  ngOnInit(): void {
    const email = this.authService.getEmail();
    if (!email) return;

    this.orderService.getOrdersByCustomerEmail(email).subscribe({
      next: (data) => {
        this.orders.set(data);
      },
      error: (err) => {
        console.log('Greška pri učitavanju narudžbina: ' + err);
      },
    });

    const userId = this.authService.getUserId();
    if (!userId) return;

    this.userService.getLoyalty(userId).subscribe({
      next: (data) => {
        this.loyalty.set(data);
      },
      error: (err) => {
        console.log('Greška pri učitavanju loyalty podataka: ' + err);
      },
    });
  }

  statusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      RECEIVED: 'Primljena',
      PREPARING: 'Priprema se',
      READY: 'Spremna',
      PICKED_UP: 'Preuzeta',
    };
    return labels[status] || status;
  }

  statusBadgeClass(status: string): string {
    const classes: { [key: string]: string } = {
      RECEIVED: 'bg-secondary',
      PREPARING: 'bg-warning',
      READY: 'bg-info',
      PICKED_UP: 'bg-success',
    };
    return classes[status] || 'bg-secondary';
  }
}
