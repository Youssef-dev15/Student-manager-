import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { adminGuard, authGuard } from './core/guards/auth.guard';
export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then(m => m.RegisterComponent),
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'students', pathMatch: 'full' },
      {
        path: 'students',
        loadComponent: () =>
          import('./features/students/student-list/student-list.component').then(m => m.StudentListComponent),
      },
      {
        path: 'students/new',
        loadComponent: () =>
          import('./features/students/student-form/student-form.component').then(m => m.StudentFormComponent),
      },
      {
        path: 'students/:id/edit',
        loadComponent: () =>
          import('./features/students/student-form/student-form.component').then(m => m.StudentFormComponent),
      },
            {
        path: 'admin/users',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/admin/users.component').then(m => m.UsersComponent),
      },
            {
        path: 'admin/audit-logs',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/admin/audit-logs/audit-logs.component').then(m => m.AuditLogsComponent),
      },
            {
        path: 'students/:id',
        loadComponent: () =>
          import('./features/students/student-detail/student-detail.component').then(m => m.StudentDetailComponent),
      },
            {
        path: 'students/:id',
        loadComponent: () =>
          import('./features/students/student-detail/student-detail.component').then(m => m.StudentDetailComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'students' },
];