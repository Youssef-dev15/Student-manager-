import { Component, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserAccount } from '../../shared/models/user.models';
import { AdminUserService } from './admin-user.service';

type StatusFilter = 'All' | 'Pending' | 'Approved' | 'Blocked';

@Component({
  selector: 'app-users',
  standalone: true,
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent {
  private userService = inject(AdminUserService);

  readonly filters: StatusFilter[] = ['All', 'Pending', 'Approved', 'Blocked'];

  users = signal<UserAccount[]>([]);
  filter = signal<StatusFilter>('All');
  loading = signal(false);
  busyId = signal<string | null>(null);   // account currently being updated
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  visibleUsers = computed(() => {
    const f = this.filter();
    return f === 'All' ? this.users() : this.users().filter(u => u.status === f);
  });

  counts = computed(() => {
    const list = this.users();
    return {
      All: list.length,
      Pending: list.filter(u => u.status === 'Pending').length,
      Approved: list.filter(u => u.status === 'Approved').length,
      Blocked: list.filter(u => u.status === 'Blocked').length,
    };
  });

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.userService.getAll().subscribe({
      next: users => {
        this.users.set(users);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  setFilter(f: StatusFilter): void {
    this.filter.set(f);
  }

  count(f: StatusFilter): number {
    return this.counts()[f];
  }

  approve(user: UserAccount): void {
    this.run(user, `Approve ${this.label(user)}?`, this.userService.validate(user.userId), 'Account approved.');
  }

  block(user: UserAccount): void {
    this.run(user, `Block ${this.label(user)}? They will lose access immediately.`, this.userService.block(user.userId), 'Account blocked.');
  }

  unblock(user: UserAccount): void {
    this.run(user, `Unblock ${this.label(user)}?`, this.userService.unblock(user.userId), 'Account unblocked.');
  }

  remove(user: UserAccount): void {
    if (!confirm(`Delete ${this.label(user)}? This cannot be undone.`)) return;

    this.busyId.set(user.userId);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    this.userService.delete(user.userId).subscribe({
      next: () => {
        this.users.update(list => list.filter(u => u.userId !== user.userId));
        this.busyId.set(null);
        this.successMessage.set('Account deleted.');
      },
      error: (err: HttpErrorResponse) => {
        this.busyId.set(null);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  isManager(user: UserAccount): boolean {
    return user.role === 'Manager';
  }

  private run(user: UserAccount, question: string, request: Observable<UserAccount>, success: string): void {
    if (!confirm(question)) return;

    this.busyId.set(user.userId);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    request.subscribe({
      next: updated => {
        this.users.update(list => list.map(u => (u.userId === updated.userId ? updated : u)));
        this.busyId.set(null);
        this.successMessage.set(success);
      },
      error: (err: HttpErrorResponse) => {
        this.busyId.set(null);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  private label(user: UserAccount): string {
    return user.fullName?.trim() || user.email;
  }

  private toMessage(err: HttpErrorResponse): string {
    switch (err.status) {
      case 0:
        return 'Server unreachable. Check that the backend is running.';
      case 403:
        return 'Access denied. Only administrators can manage accounts.';
      case 404:
        return 'Account not found. It may have been deleted.';
      case 409:
      case 500:
        return err.error?.message ?? 'This action is not allowed for the current status of the account.';
      default:
        return 'Something went wrong. Please try again.';
    }
  }
}