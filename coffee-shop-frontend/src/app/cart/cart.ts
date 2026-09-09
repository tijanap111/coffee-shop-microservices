import { CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../service/cart-service';
import { Navbar } from '../navbar/navbar';

@Component({
  selector: 'app-cart',
  imports: [CurrencyPipe, Navbar],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  private cartService = inject(CartService);
  private router = inject(Router);

  getItems(): Array<CartItem> {
    return this.cartService.getItems();
  }

  increase(productId: number): void {
    const item = this.getItems().find((i) => i.product.id === productId);
    if (item) {
      this.cartService.addItem(item.product);
    }
  }

  decrease(productId: number): void {
    this.cartService.removeItem(productId);
  }

  getTotal(): number {
    return this.cartService.getTotalPrice();
  }

  goToCheckout(): void {
    this.router.navigate(['/checkout']);
  }

  goBackToMenu(): void {
    this.router.navigate(['/menu']);
  }
}
