export interface AuditLog {
  logId: string;
  actionType: string;
  adminEmail: string | null;
  description: string | null;
  timestamp: string;
}