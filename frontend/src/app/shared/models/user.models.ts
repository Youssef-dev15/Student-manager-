import { AccountStatus, Role } from './auth.models';

export interface UserAccount {
  userId: string;
  email: string;
  fullName: string | null;
  role: Role;
  status: AccountStatus | null;
}