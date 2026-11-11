import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Absence, AbsencePayload } from '../../shared/models/absence.models';

@Injectable({ providedIn: 'root' })
export class AbsenceService {
  private http = inject(HttpClient);

  listByStudent(studentId: string): Observable<Absence[]> {
    return this.http.get<Absence[]>(`${environment.apiUrl}/students/${studentId}/absences`);
  }

  add(payload: AbsencePayload): Observable<Absence> {
    return this.http.post<Absence>(`${environment.apiUrl}/absences`, payload);
  }

  delete(absenceId: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/absences/${absenceId}`);
  }
}