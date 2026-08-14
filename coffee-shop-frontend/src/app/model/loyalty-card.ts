import { User } from './user';

export interface LoyaltyCard {
  id: number;
  user: User;
  points: number;
  level: string;
}
