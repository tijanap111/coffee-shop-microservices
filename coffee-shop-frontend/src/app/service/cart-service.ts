import { Injectable } from '@angular/core';
import { Product } from '../model/product';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private items: Array<CartItem> = [];

  getItems(): Array<CartItem> {
    return this.items;
  }

  addItem(product: Product): void {
    const existing = this.items.find((item) => item.product.id === product.id);
    if (existing) {
      existing.quantity += 1;
    } else {
      this.items.push({ product, quantity: 1 });
    }
  }

  removeItem(productId: number): void {
    const existing = this.items.find((item) => item.product.id === productId);
    if (!existing) return;

    existing.quantity -= 1;
    if (existing.quantity <= 0) {
      this.items = this.items.filter((item) => item.product.id !== productId);
    }
  }

  getTotalPrice(): number {
    return this.items.reduce((total, item) => total + item.product.price * item.quantity, 0);
  }

  clearCart(): void {
    this.items = [];
  }
}
