import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartService } from '../service/cart-service';
import { OrderService } from '../service/order-service';
import { AuthService } from '../service/auth-service';
import { CreateOrderRequest } from '../model/create-order-request';
import {Navbar} from '../navbar/navbar';

@Component({
  selector: 'app-checkout',
  imports: [ReactiveFormsModule, CurrencyPipe, Navbar],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout implements OnInit {
  private fb = inject(FormBuilder);
  private cartService = inject(CartService);
  private orderService = inject(OrderService);
  private authService = inject(AuthService);
  private router = inject(Router);

  checkoutForm!: FormGroup;
  errorMessage: string = '';

  ngOnInit(): void {
    this.checkoutForm = this.fb.group({
      address: this.fb.control(null, [Validators.required, Validators.minLength(5)]),
      note: this.fb.control(''),
    });
  }

  get addressControl() {
    return this.checkoutForm.get('address');
  }

  get noteControl() {
    return this.checkoutForm.get('note');
  }

  getCartTotal(): number {
    return this.cartService.getTotalPrice();
  }

  submit(): void {
    if (this.checkoutForm.invalid) return;

    const userId = this.authService.getUserId();
    if (!userId) return;

    const productQuantities: { [id: number]: number } = {};
    this.cartService.getItems().forEach((item) => {
      productQuantities[item.product.id] = item.quantity;
    });

    const combinedNote = `Adresa: ${this.addressControl?.value}` +
      (this.noteControl?.value ? ` | Napomena: ${this.noteControl?.value}` : '');

    const request: CreateOrderRequest = {
      customerId: userId,
      productQuantities: productQuantities,
      note: combinedNote,
    };

    this.orderService.createOrder(request).subscribe({
      next: () => {
        this.cartService.clearCart();
        this.router.navigate(['/orders']);
      },
      error: (err) => {
        this.errorMessage = 'Greška prilikom kreiranja narudžbine.';
      },
    });
  }
}
