import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserAccount } from '../../shared/models/user.models';

@Injectable({ providedIn: 'root' })
export class AdminUserService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/admin/users`;

  getAll(): Observable<UserAccount[]> {
    return this.http.get<UserAccount[]>(this.url);
  }

  validate(id: string): Observable<UserAccount> {
    return this.http.patch<UserAccount>(`${this.url}/${id}/validate`, null);
  }

  block(id: string): Observable<UserAccount> {
    return this.http.patch<UserAccount>(`${this.url}/${id}/block`, null);
  }

  unblock(id: string): Observable<UserAccount> {
    return this.http.patch<UserAccount>(`${this.url}/${id}/unblock`, null);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}