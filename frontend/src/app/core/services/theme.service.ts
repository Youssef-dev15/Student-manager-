import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  isDark = signal(this.readStored() === 'dark');

  constructor() {
    this.apply();
  }

  toggle(): void {
    this.isDark.update(v => !v);
    try { localStorage.setItem('theme', this.isDark() ? 'dark' : 'light'); } catch { /* stockage indisponible */ }
    this.apply();
  }

  private apply(): void {
    document.documentElement.setAttribute('data-theme', this.isDark() ? 'dark' : 'light');
  }

  private readStored(): string | null {
    try { return localStorage.getItem('theme'); } catch { return null; }
  }
}