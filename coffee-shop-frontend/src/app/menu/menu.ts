import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MenuService } from '../service/menu-service';
import { CartService } from '../service/cart-service';
import { Product } from '../model/product';
import { Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { ProductItem } from '../product-item/product-item';

@Component({
  selector: 'app-menu',
  imports: [Navbar, ProductItem, FormsModule],
  templateUrl: './menu.html',
  styleUrl: './menu.css',
})
export class Menu implements OnInit {
  private menuService = inject(MenuService);
  private cartService = inject(CartService);
  private router = inject(Router);

  products = signal<Array<Product>>([]);

  filterName: string = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;

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

  filteredProducts(): Array<Product> {
    return this.products().filter((p) => {
      const matchesName = p.name.toLowerCase().includes(this.filterName.toLowerCase());
      const matchesMin = this.minPrice == null || p.price >= this.minPrice;
      const matchesMax = this.maxPrice == null || p.price <= this.maxPrice;
      return matchesName && matchesMin && matchesMax;
    });
  }

  resetFilters(): void {
    this.filterName = '';
    this.minPrice = null;
    this.maxPrice = null;
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
