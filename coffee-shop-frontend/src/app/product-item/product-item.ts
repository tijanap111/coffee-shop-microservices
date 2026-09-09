import { CurrencyPipe, NgClass } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { Product } from '../model/product';

@Component({
  selector: 'app-product-item',
  imports: [CurrencyPipe, NgClass],
  templateUrl: './product-item.html',
  styleUrl: './product-item.css',
})
export class ProductItem {
  product = input.required<Product>();
  addToCartEvent = output<Product>();

  onAddToCart(): void {
    this.addToCartEvent.emit(this.product());
  }
}
