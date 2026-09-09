import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MenuService } from '../service/menu-service';
import { OrderService } from '../service/order-service';
import { Product } from '../model/product';
import { Order } from '../model/order';
import {Navbar} from '../navbar/navbar';
import { SectionCard } from '../section-card/section-card';

@Component({
  selector: 'app-admin',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule, Navbar, SectionCard],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  private fb = inject(FormBuilder);
  private menuService = inject(MenuService);
  private orderService = inject(OrderService);

  products = signal<Array<Product>>([]);
  orders = signal<Array<Order>>([]);
  newProductForm!: FormGroup;
  errorMessage: string = '';

  private nextStatus: { [key: string]: string } = {
    RECEIVED: 'PREPARING',
    PREPARING: 'READY',
    READY: 'PICKED_UP',
  };

  ngOnInit(): void {
    this.loadProducts();
    this.loadOrders();

    this.newProductForm = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(2)]),
      description: this.fb.control(''),
      price: this.fb.control(0, [Validators.required, Validators.min(0)]),
    });
  }

  get nameControl() {
    return this.newProductForm.get('name');
  }

  get priceControl() {
    return this.newProductForm.get('price');
  }

  loadProducts(): void {
    this.menuService.getProducts().subscribe({
      next: (data) => {
        this.products.set(data);
      },
    });
  }

  loadOrders(): void {
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders.set(data);
      },
    });
  }

  submit(): void {
    if (this.newProductForm.invalid) return;

    const newProduct: Partial<Product> = {
      name: this.newProductForm.value.name,
      description: this.newProductForm.value.description,
      price: this.newProductForm.value.price,
      available: true,
    };

    this.menuService.createProduct(newProduct).subscribe({
      next: () => {
        this.newProductForm.reset();
        this.loadProducts();
      },
      error: () => {
        this.errorMessage = 'Greška pri dodavanju proizvoda.';
      },
    });
  }

  deleteProduct(id: number): void {
    this.menuService.deleteProduct(id).subscribe({
      next: () => {
        this.loadProducts();
      },
      error: () => {
        // error interceptor already shows an alert for 409
      },
    });
  }

  toggleAvailability(product: Product): void {
    this.menuService.updateProduct(product.id, { ...product, available: !product.available }).subscribe({
      next: () => {
        this.loadProducts();
      },
    });
  }

  canAdvance(order: Order): boolean {
    return !!this.nextStatus[order.status];
  }

  advanceStatus(order: Order): void {
    const newStatus = this.nextStatus[order.status];
    if (!newStatus) return;

    this.orderService.updateStatus(order.id, newStatus).subscribe({
      next: () => {
        this.loadOrders();
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
