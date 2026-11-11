import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StudentPayload, StudentStatus } from '../../../shared/models/student.models';
import { StudentService } from '../student.service';

@Component({
  selector: 'app-student-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-form.component.html',
  styleUrl: './student-form.component.scss',
})
export class StudentFormComponent {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private studentService = inject(StudentService);

  // Adapt these lists to the values used in your data
  readonly levels = ['L1', 'L2', 'L3', 'M1', 'M2'];
  readonly genders = ['Male', 'Female'];
  readonly statuses: StudentStatus[] = ['ACTIVE', 'SUSPENDED', 'GRADUATED', 'DROPPED'];

  studentId = this.route.snapshot.paramMap.get('id');
  isEdit = this.studentId !== null;

  form = this.fb.nonNullable.group({
    studentNumber: ['', [Validators.required, Validators.maxLength(50)]],
    firstName: ['', [Validators.required, Validators.maxLength(100)]],
    lastName: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    birthDate: [''],
    major: [''],
    gender: [''],
    enrollmentYear: this.fb.control<number | null>(null, [Validators.min(1900), Validators.max(2100)]),
    studyLevel: [''],
    medicalFilePdf: [''],
    studentStatus: this.fb.nonNullable.control<StudentStatus>('ACTIVE'),
  });

  // Managed by the system (see the Absences tab), never typed by the user
  absencesCount = signal(0);

  loading = signal(false);
  saving = signal(false);
  errorMessage = signal<string | null>(null);

  constructor() {
    if (this.isEdit && this.studentId) {
      // The student number is an identifier: it cannot be changed after creation
      this.form.controls.studentNumber.disable();
      this.load(this.studentId);
    }
  }

  private load(id: string): void {
    this.loading.set(true);
    this.studentService.getById(id).subscribe({
      next: s => {
        this.form.patchValue({
          studentNumber: s.studentNumber,
          firstName: s.firstName,
          lastName: s.lastName,
          email: s.email,
          birthDate: s.birthDate ?? '',
          major: s.major ?? '',
          gender: s.gender ?? '',
          enrollmentYear: s.enrollmentYear,
          studyLevel: s.studyLevel ?? '',
          medicalFilePdf: s.medicalFilePdf ?? '',
          studentStatus: s.studentStatus,
        });
        this.absencesCount.set(s.absencesCount ?? 0);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.errorMessage.set(this.toMessage(err));
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.errorMessage.set(null);

    const v = this.form.getRawValue();   // includes the disabled student number
    const payload: StudentPayload = {
      studentNumber: v.studentNumber.trim(),
      firstName: v.firstName.trim(),
      lastName: v.lastName.trim(),
      email: v.email.trim().toLowerCase(),
      birthDate: v.birthDate || null,
      major: v.major.trim() || null,
      gender: v.gender || null,
      enrollmentYear: v.enrollmentYear,
      studyLevel: v.studyLevel || null,
      medicalFilePdf: v.medicalFilePdf.trim() || null,
      studentStatus: v.studentStatus,
      absencesCount: this.absencesCount(),
    };

    const request = this.isEdit && this.studentId
      ? this.studentService.update(this.studentId, payload)
      : this.studentService.create(payload);

    request.subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/students']);
      },
      error: (err: HttpErrorResponse) => {
        this.saving.set(false);
        this.errorMessage.set(this.toMessage(err));
      },
    });
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
        return 'Student not found.';
      case 409:
        return 'This email or student number is already in use.';
      case 500:
        return 'The student could not be saved. The email or student number may already be in use.';
      default:
        return 'Something went wrong. Please try again.';
    }
  }
}