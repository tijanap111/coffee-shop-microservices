export interface CreateOrderRequest {
  customerId: number;
  productQuantities: { [productId: number]: number };
  note: string;
}
