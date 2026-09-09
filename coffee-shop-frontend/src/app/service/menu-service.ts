import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../model/product';
import { Category } from '../model/category';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8765/MENU-SERVICE';

  getProducts(): Observable<Array<Product>> {
    return this.http.get<Array<Product>>(this.apiUrl + '/products');
  }

  getAvailableProducts(): Observable<Array<Product>> {
    return this.http.get<Array<Product>>(this.apiUrl + '/products/available');
  }

  getProductsByCategory(categoryId: number): Observable<Array<Product>> {
    return this.http.get<Array<Product>>(this.apiUrl + '/products/category/' + categoryId);
  }

  getCategories(): Observable<Array<Category>> {
    return this.http.get<Array<Category>>(this.apiUrl + '/categories');
  }

  createProduct(product: Partial<Product>): Observable<Product> {
    return this.http.post<Product>(this.apiUrl + '/products', product);
  }

  updateProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(this.apiUrl + '/products/' + id, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/products/' + id);
  }
}
