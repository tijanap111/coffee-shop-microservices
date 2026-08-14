import { Customer } from './customer';

export interface Order {
  id: number;
  customer: Customer;
  status: string;
  totalPrice: number;
  createdAt: string;
  note: string;
}
