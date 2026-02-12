import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
    path: 'dashboard',
    loadComponent: () => import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'employees',
    loadComponent: () => import('./components/employee-list/employee-list.component').then(m => m.EmployeeListComponent)
  },
  {
    path: 'rewards',
    loadComponent: () => import('./components/reward-list/reward-list.component').then(m => m.RewardListComponent)
  },
  { path: '**', redirectTo: '/dashboard' }
];
