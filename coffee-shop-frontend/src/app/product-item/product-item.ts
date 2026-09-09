import { CurrencyPipe } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { Product } from '../model/product';

@Component({
  selector: 'app-product-item',
  imports: [CurrencyPipe],
  templateUrl: './product-item.html',
  styleUrl: './product-item.css',
})
export class ProductItem {
  product = input<Product>();
  addToCartEvent = output<Product>();

  onAddToCart(): void {
    const p = this.product();
    if (p) {
      this.addToCartEvent.emit(p);
    }
  }
}
