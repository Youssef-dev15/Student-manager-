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