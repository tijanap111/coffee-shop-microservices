import { Component, inject, OnInit, signal } from '@angular/core';
import { MenuService } from '../service/menu-service';
import { CartService } from '../service/cart-service';
import { Product } from '../model/product';
import { Router } from '@angular/router';
import {Navbar} from '../navbar/navbar';

@Component({
  selector: 'app-menu',
  imports: [ Navbar ],
  templateUrl: './menu.html',
  styleUrl: './menu.css',
})
export class Menu implements OnInit {
  private menuService = inject(MenuService);
  private cartService = inject(CartService);
  private router = inject(Router);

  products = signal<Array<Product>>([]);

  ngOnInit(): void {
    this.menuService.getAvailableProducts().subscribe({
      next: (data) => {
        this.products.set(data);
      },
      error: (err) => {
        console.log('Greška pri učitavanju proizvoda: ' + err);
      },
    });
  }

  addToCart(product: Product): void {
    this.cartService.addItem(product);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }

  cartItemCount(): number {
    return this.cartService.getItems().reduce((sum, item) => sum + item.quantity, 0);
  }
}
