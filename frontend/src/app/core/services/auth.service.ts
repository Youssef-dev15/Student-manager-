import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest, Role } from '../../shared/models/auth.models';

interface Session {
  token: string;
  email: string;
  fullName: string;
  role: Role;
}

const STORAGE_KEY = 'sm_session';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private session = signal<Session | null>(this.loadSession());

  user = computed(() => this.session());
  isAdmin = computed(() => this.session()?.role === 'Admin');

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap(res => this.saveSession({
        token: res.accessToken,
        email: res.email,
        fullName: res.fullName,
        role: res.role,
      }))
    );
  }

  register(request: RegisterRequest): Observable<unknown> {
    return this.http.post(`${environment.apiUrl}/auth/register`, request);
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  token(): string | null {
    return this.session()?.token ?? null;
  }

  hasValidSession(): boolean {
    const s = this.session();
    if (!s) return false;
    if (this.isExpired(s.token)) {
      this.clearSession();
      return false;
    }
    return true;
  }

  // --- méthodes privées ---

  private saveSession(s: Session): void {
    this.session.set(s);
    try { localStorage.setItem(STORAGE_KEY, JSON.stringify(s)); } catch { /* stockage indisponible */ }
  }

  private clearSession(): void {
    this.session.set(null);
    try { localStorage.removeItem(STORAGE_KEY); } catch { /* stockage indisponible */ }
  }

  private loadSession(): Session | null {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return null;
      const s = JSON.parse(raw) as Session;
      return this.isExpired(s.token) ? null : s;
    } catch {
      return null;
    }
  }

  private isExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return !payload.exp || payload.exp * 1000 < Date.now();
    } catch {
      return true;
    }
  }
}