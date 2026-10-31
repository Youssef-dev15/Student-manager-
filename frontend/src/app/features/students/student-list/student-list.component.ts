import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subscription, debounceTime } from 'rxjs';
import { StudentFilter, StudentListItem } from '../../../shared/models/student.models';
import { StudentService } from '../student.service';

type SortField = 'lastName' | 'enrollmentYear' | 'absencesCount';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.scss',
})
export class StudentListComponent {
  private studentService = inject(StudentService);
  private sub?: Subscription;

  // À adapter aux niveaux réellement utilisés dans ta base
  readonly levels = ['L1', 'L2', 'L3', 'M1', 'M2'];
  readonly pageSizes = [10, 20, 50];
  readonly absenceAlertThreshold = 3;

  form = new FormGroup({
    keyword: new FormControl('', { nonNullable: true }),
    studyLevels: new FormControl<string[]>([], { nonNullable: true }),
    enrollmentYearFrom: new FormControl<number | null>(null),
    enrollmentYearTo: new FormControl<number | null>(null),
    minAbsences: new FormControl<number | null>(null),
    maxAbsences: new FormControl<number | null>(null),
  });

  items = signal<StudentListItem[]>([]);
  totalElements = signal(0);
  totalPages = signal(0);
  page = signal(0);
  size = signal(20);
  sortField = signal<SortField>('lastName');
  sortDir = signal<'asc' | 'desc'>('asc');
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor() {
    this.form.valueChanges
      .pipe(debounceTime(300), takeUntilDestroyed())
      .subscribe(() => {
        this.page.set(0);
        this.load();
      });

    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.sub?.unsubscribe();   // ignore la réponse d'une recherche devenue obsolète

    this.sub = this.studentService
      .search(this.buildFilter(), this.page(), this.size(), `${this.sortField()},${this.sortDir()}`)
      .subscribe({
        next: res => {
          this.items.set(res.content);
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

  toggleLevel(level: string): void {
    const control = this.form.controls.studyLevels;
    const current = control.value;
    control.setValue(current.includes(level) ? current.filter(l => l !== level) : [...current, level]);
  }

  isLevelSelected(level: string): boolean {
    return this.form.controls.studyLevels.value.includes(level);
  }

  resetFilters(): void {
    this.form.reset();
  }

  toggleSort(field: SortField): void {
    if (this.sortField() === field) {
      this.sortDir.set(this.sortDir() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortField.set(field);
      this.sortDir.set('asc');
    }
    this.page.set(0);
    this.load();
  }

  sortIndicator(field: SortField): string {
    if (this.sortField() !== field) return '';
    return this.sortDir() === 'asc' ? '▲' : '▼';
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

  archive(student: StudentListItem): void {
    if (!confirm(`Are you sure you want to archive ${student.firstName} ${student.lastName}?`)) return;

    this.studentService.archive(student.studentId).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.errorMessage.set(this.toMessage(err)),
    });
  }

  isAbsenceHigh(student: StudentListItem): boolean {
    return (student.absencesCount ?? 0) >= this.absenceAlertThreshold;
  }

  private buildFilter(): StudentFilter {
    const v = this.form.getRawValue();
    return {
      keyword: v.keyword || undefined,
      studyLevels: v.studyLevels.length ? v.studyLevels : undefined,
      enrollmentYearFrom: v.enrollmentYearFrom ?? undefined,
      enrollmentYearTo: v.enrollmentYearTo ?? undefined,
      minAbsences: v.minAbsences ?? undefined,
      maxAbsences: v.maxAbsences ?? undefined,
    };
  }

  private toMessage(err: HttpErrorResponse): string {
    switch (err.status) {
      case 0:
        return 'Server unreachable. Please verify that the backend is running.';
      case 400:
        return 'Invalid filters. Please check your inputs.';
      case 403:
        return 'Access denied.';
      default:
        return 'An error occurred. Please try again.';
    }
  }
}