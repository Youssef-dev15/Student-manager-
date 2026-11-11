export interface StudentListItem {
  studentId: string;
  firstName: string;
  lastName: string;
  enrollmentYear: number | null;
  studyLevel: string | null;
  absencesCount: number | null;
}

export interface StudentFilter {
  keyword?: string;
  studyLevels?: string[];
  enrollmentYearFrom?: number;
  enrollmentYearTo?: number;
  minAbsences?: number;
  maxAbsences?: number;
}

export type StudentStatus = 'ACTIVE' | 'SUSPENDED' | 'GRADUATED' | 'DROPPED';


export interface StudentDetail {
  studentId: string;
  studentNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  birthDate: string | null;
  major: string | null;
  gender: string | null;
  enrollmentYear: number | null;
  studyLevel: string | null;
  medicalFilePdf: string | null;
  studentStatus: StudentStatus;
  absencesCount: number | null;
}

export type StudentPayload = Omit<StudentDetail, 'studentId'>;

