import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuditLog } from '../../../shared/models/audit-log.models';
import { AuditLogService } from './audit-log.service';

@Component({
  selector: 'app-audit-logs',
  standalone: true,
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './audit-logs.component.html',
  styleUrl: './audit-logs.component.scss',
})
export class AuditLogsComponent {
  private service = inject(AuditLogService);

  readonly pageSizes = [10, 20, 50];

  filters = new FormGroup({
    actionType: new FormControl('', { nonNullable: true }),
    after: new FormControl('', { nonNullable: true }),
  });

  logs = signal<AuditLog[]>([]);
  totalElements = signal(0);
  totalPages = signal(0);
  page = signal(0);
  size = signal(20);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    const { actionType, after } = this.filters.getRawValue();
    // datetime-local gives "2026-11-10T12:30", the backend expects seconds too
    const afterIso = after ? (after.length === 16 ? `${after}:00` : after) : undefined;

    this.service.list(this.page(), this.size(), actionType.trim() || undefined, afterIso).subscribe({
      next: res => {
        this.logs.set(res.content);
        this.totalElements.set(res.totalElements);
        this.totalPages.set(res.totalPages);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  applyFilters(): void {
    this.page.set(0);
    this.load();
  }

  resetFilters(): void {
    this.filters.reset();
    this.page.set(0);
    this.load();
  }

  goTo(page: number): void {
    if (page < 0 || page >= this.totalPages()) return;
    this.page.set(page);
    this.load();
  }

  changeSize(event: Event): void {
    this.size.set(Number((event.target as HTMLSelectElement).value));
    this.page.set(0);
    this.load();
  }

  private toMessage(err: HttpErrorResponse): string {
    switch (err.status) {
      case 0:
        return 'Server unreachable. Check that the backend is running.';
      case 400:
        return 'Invalid filter. Check the values you entered.';
      case 403:
        return 'Access denied. Only administrators can view the audit log.';
      case 500:
        return 'Server error. Please try again, or contact an administrator if it persists.';
      default:
        return 'Something went wrong. Please try again.';
    }
  }
}