import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../../../shared/models/page.model';
import { AuditLog } from '../../../shared/models/audit-log.models';

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/admin/audit-logs`;

  list(page: number, size: number, actionType?: string, after?: string): Observable<Page<AuditLog>> {
    let params = new HttpParams().set('page', page).set('size', size);

    if (actionType) {
      return this.http.get<Page<AuditLog>>(`${this.url}/action/${encodeURIComponent(actionType)}`, { params });
    }
    if (after) {
      params = params.set('date', after);
      return this.http.get<Page<AuditLog>>(`${this.url}/after`, { params });
    }
    return this.http.get<Page<AuditLog>>(this.url, { params });
  }
}