import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MenuService } from '../service/menu-service';
import { Product } from '../model/product';
import {Navbar} from '../navbar/navbar';

@Component({
  selector: 'app-admin',
  imports: [CurrencyPipe, ReactiveFormsModule, Navbar],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin implements OnInit {
  private fb = inject(FormBuilder);
  private menuService = inject(MenuService);

  products = signal<Array<Product>>([]);
  newProductForm!: FormGroup;
  errorMessage: string = '';

  ngOnInit(): void {
    this.loadProducts();

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
        this.errorMessage = 'Error adding product.';
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
}
