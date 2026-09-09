import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Menu } from './menu/menu';
import { Cart } from './cart/cart';
import { Checkout } from './checkout/checkout';
import { Orders } from './orders/orders';
import { Admin } from './admin/admin';
import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'menu', component: Menu, canActivate: [authGuard] },
  { path: 'cart', component: Cart, canActivate: [authGuard] },
  { path: 'checkout', component: Checkout, canActivate: [authGuard] },
  { path: 'orders', component: Orders, canActivate: [authGuard] },
  { path: 'admin', component: Admin, canActivate: [authGuard, roleGuard] },
];
