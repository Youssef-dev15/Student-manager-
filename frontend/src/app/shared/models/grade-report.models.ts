export interface GradeReport {
  reportId: string;
  academicYear: string | null;
  semesterNumber: number | null;
  bulletinPdf: string | null;
}

export interface GradeReportPayload {
  studentId: string;
  academicYear: string;
  semesterNumber: number;
  bulletinPdf: string | null;
}