import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

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
      // Étapes suivantes : students/new, students/:id/edit, students/:id,
      // admin/users (adminGuard), admin/audit-logs (adminGuard)
    ],
  },
  { path: '**', redirectTo: 'students' },
];