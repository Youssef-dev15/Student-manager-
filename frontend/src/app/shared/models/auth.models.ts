export type Role = 'Admin' | 'Manager';
export type AccountStatus = 'Pending' | 'Approved' | 'Blocked';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  email: string;
  fullName: string;
  role: Role;
}