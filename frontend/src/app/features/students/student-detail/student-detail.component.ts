import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Absence } from '../../../shared/models/absence.models';
import { GradeReport } from '../../../shared/models/grade-report.models';
import { StudentDetail } from '../../../shared/models/student.models';
import { AbsenceService } from '../absence.service';
import { GradeReportService } from '../grade-report.service';
import { StudentService } from '../student.service';

type Tab = 'info' | 'absences' | 'reports';

@Component({
  selector: 'app-student-detail',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-detail.component.html',
  styleUrl: './student-detail.component.scss',
})
export class StudentDetailComponent {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private studentService = inject(StudentService);
  private absenceService = inject(AbsenceService);
  private reportService = inject(GradeReportService);

  readonly studentId = this.route.snapshot.paramMap.get('id') ?? '';
  readonly today = new Date().toISOString().slice(0, 10);
  readonly semesters = [1, 2];

  student = signal<StudentDetail | null>(null);
  absences = signal<Absence[]>([]);
  reports = signal<GradeReport[]>([]);

  tab = signal<Tab>('info');
  loading = signal(true);
  saving = signal(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  absenceForm = this.fb.nonNullable.group({
    absenceDate: [this.today, [Validators.required]],
    justified: [false],
  });

  reportForm = this.fb.nonNullable.group({
    academicYear: ['', [Validators.required, Validators.maxLength(20)]],
    semesterNumber: [1, [Validators.required]],
    bulletinPdf: [''],
  });

  constructor() {
    this.loadStudent(true);
    this.loadAbsences();
    this.loadReports();
  }

  setTab(tab: Tab): void {
    this.tab.set(tab);
    this.errorMessage.set(null);
    this.successMessage.set(null);
  }

  // ----- loading -----

  private loadStudent(first = false): void {
    this.studentService.getById(this.studentId).subscribe({
      next: s => {
        this.student.set(s);
        if (first) this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  private loadAbsences(): void {
    this.absenceService.listByStudent(this.studentId).subscribe({
      next: list => this.absences.set(list),
      error: (err: HttpErrorResponse) => this.errorMessage.set(this.toMessage(err)),
    });
  }

  private loadReports(): void {
    this.reportService.listByStudent(this.studentId).subscribe({
      next: list => this.reports.set(list),
      error: (err: HttpErrorResponse) => this.errorMessage.set(this.toMessage(err)),
    });
  }

  // ----- absences -----

  addAbsence(): void {
    if (this.absenceForm.invalid) {
      this.absenceForm.markAllAsTouched();
      return;
    }

    this.begin();
    const v = this.absenceForm.getRawValue();

    this.absenceService.add({ studentId: this.studentId, absenceDate: v.absenceDate, justified: v.justified }).subscribe({
      next: () => {
        this.end('Absence added.');
        this.absenceForm.reset({ absenceDate: this.today, justified: false });
        this.loadAbsences();
        this.loadStudent();   // the absence counter changed
      },
      error: (err: HttpErrorResponse) => this.fail(err),
    });
  }

  deleteAbsence(absence: Absence): void {
    if (!confirm(`Delete the absence of ${absence.absenceDate}?`)) return;

    this.begin();
    this.absenceService.delete(absence.absenceId).subscribe({
      next: () => {
        this.end('Absence deleted.');
        this.loadAbsences();
        this.loadStudent();
      },
      error: (err: HttpErrorResponse) => this.fail(err),
    });
  }

  // ----- grade reports -----

  addReport(): void {
    if (this.reportForm.invalid) {
      this.reportForm.markAllAsTouched();
      return;
    }

    this.begin();
    const v = this.reportForm.getRawValue();

    this.reportService.add({
      studentId: this.studentId,
      academicYear: v.academicYear.trim(),
      semesterNumber: Number(v.semesterNumber),
      bulletinPdf: v.bulletinPdf.trim() || null,
    }).subscribe({
      next: () => {
        this.end('Grade report added.');
        this.reportForm.reset({ academicYear: '', semesterNumber: 1, bulletinPdf: '' });
        this.loadReports();
      },
      error: (err: HttpErrorResponse) => this.fail(err),
    });
  }

  deleteReport(report: GradeReport): void {
    if (!confirm(`Delete the report ${report.academicYear ?? ''} (semester ${report.semesterNumber ?? ''})?`)) return;

    this.begin();
    this.reportService.delete(report.reportId).subscribe({
      next: () => {
        this.end('Grade report deleted.');
        this.loadReports();
      },
      error: (err: HttpErrorResponse) => this.fail(err),
    });
  }

  // ----- student actions -----

  archive(): void {
    const s = this.student();
    if (!s || !confirm(`Archive ${s.firstName} ${s.lastName}?`)) return;

    this.begin();
    this.studentService.archive(this.studentId).subscribe({
      next: () => this.router.navigate(['/students']),
      error: (err: HttpErrorResponse) => this.fail(err),
    });
  }

  // ----- helpers -----

  private begin(): void {
    this.saving.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);
  }

  private end(message: string): void {
    this.saving.set(false);
    this.successMessage.set(message);
  }

  private fail(err: HttpErrorResponse): void {
    this.saving.set(false);
    this.errorMessage.set(this.toMessage(err));
  }

  private toMessage(err: HttpErrorResponse): string {
    switch (err.status) {
      case 0:
        return 'Server unreachable. Check that the backend is running.';
      case 400:
        return 'Invalid data. Check the fields and try again.';
      case 403:
        return 'Access denied.';
      case 404:
        return 'Not found. It may have been deleted.';
      default:
        return 'Something went wrong. Please try again.';
    }
  }
}