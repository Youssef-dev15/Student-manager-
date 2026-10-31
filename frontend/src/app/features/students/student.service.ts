import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../../shared/models/page.model';
import { StudentFilter, StudentListItem } from '../../shared/models/student.models';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/students`;

  search(filter: StudentFilter, page: number, size: number, sort: string): Observable<Page<StudentListItem>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    if (filter.keyword?.trim()) params = params.set('keyword', filter.keyword.trim());
    filter.studyLevels?.forEach(level => (params = params.append('studyLevels', level)));
    if (filter.enrollmentYearFrom != null) params = params.set('enrollmentYearFrom', filter.enrollmentYearFrom);
    if (filter.enrollmentYearTo != null) params = params.set('enrollmentYearTo', filter.enrollmentYearTo);
    if (filter.minAbsences != null) params = params.set('minAbsences', filter.minAbsences);
    if (filter.maxAbsences != null) params = params.set('maxAbsences', filter.maxAbsences);

    return this.http.get<Page<StudentListItem>>(`${this.url}/search`, { params });
  }

  archive(id: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${id}/archive`, null);
  }
}