export interface Absence {
  absenceId: string;
  absenceDate: string;          
  justified: boolean;
  recordedBy: string | null;
}

export interface AbsencePayload {
  studentId: string;
  absenceDate: string;
  justified: boolean;
}