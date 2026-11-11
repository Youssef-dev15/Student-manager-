import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GradeReport, GradeReportPayload } from '../../shared/models/grade-report.models';

@Injectable({ providedIn: 'root' })
export class GradeReportService {
  private http = inject(HttpClient);

  listByStudent(studentId: string): Observable<GradeReport[]> {
    return this.http.get<GradeReport[]>(`${environment.apiUrl}/students/${studentId}/grade-reports`);
  }

  add(payload: GradeReportPayload): Observable<GradeReport> {
    return this.http.post<GradeReport>(`${environment.apiUrl}/grade-reports`, payload);
  }

  delete(reportId: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/grade-reports/${reportId}`);
  }
}