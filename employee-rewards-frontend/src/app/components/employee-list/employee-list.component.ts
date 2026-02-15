import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/models';
import { AddEmployeeDialogComponent } from './add-employee.component';

import { EmployeeRewardsDialogComponent } from './employee-rewards-dialog.component'

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule,
    MatIconModule, MatDialogModule, MatSnackBarModule, MatChipsModule,
    MatTooltipModule, MatProgressSpinnerModule, MatFormFieldModule, MatInputModule],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  loading = true;

  constructor(
    private employeeService: EmployeeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.employeeService.getAllEmployees().subscribe({
      next: data => {
        this.employees = data;
        this.filteredEmployees = data;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Failed to load employees', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  onSearch(event: Event): void {
    const term = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredEmployees = this.employees.filter(e =>
      e.name.toLowerCase().includes(term) ||
      e.department.toLowerCase().includes(term) ||
      e.email.toLowerCase().includes(term)
    );
  }

  openAddDialog(): void {
    const ref = this.dialog.open(AddEmployeeDialogComponent, {
      data: {},
      width: '480px'
    });
    ref.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.createEmployee(result).subscribe({
          next: () => {
            this.loadEmployees();
            this.snackBar.open('Employee added successfully!', 'Close', { duration: 3000 });
          },
          error: (err) => {
            this.snackBar.open(err.error?.message || 'Failed to add employee', 'Close', { duration: 3000 });
          }
        });
      }
    });
  }

  openEditDialog(emp: Employee): void {
    const ref = this.dialog.open(AddEmployeeDialogComponent, {
      data: { employee: emp },
      width: '480px'
    });
    ref.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.updateEmployee(emp.id!, result).subscribe({
          next: () => {
            this.loadEmployees();
            this.snackBar.open('Employee updated!', 'Close', { duration: 3000 });
          },
          error: () => this.snackBar.open('Failed to update', 'Close', { duration: 3000 })
        });
      }
    });
  }

  deleteEmployee(emp: Employee): void {
    if (confirm(`Delete ${emp.name}? This will also remove all their rewards.`)) {
      this.employeeService.deleteEmployee(emp.id!).subscribe({
        next: () => {
          this.loadEmployees();
          this.snackBar.open('Employee deleted', 'Close', { duration: 3000 });
        },
        error: () => this.snackBar.open('Failed to delete', 'Close', { duration: 3000 })
      });
    }
  }

  openRewardsDialog(emp: Employee): void {
    this.dialog.open(EmployeeRewardsDialogComponent, {
      data: {
        employeeId:   emp.id,
        employeeName: emp.name,
        totalPoints:  emp.totalPoints || 0
      },
      width:     '560px',
      maxHeight: '80vh',       
      panelClass: 'rewards-dialog-panel'
    });
  }

  getGradient(name: string): string {
    const gradients = [
      'linear-gradient(135deg, #667eea, #764ba2)',
      'linear-gradient(135deg, #f093fb, #f5576c)',
      'linear-gradient(135deg, #4facfe, #00f2fe)',
      'linear-gradient(135deg, #43e97b, #38f9d7)',
      'linear-gradient(135deg, #fa709a, #fee140)',
      'linear-gradient(135deg, #a18cd1, #fbc2eb)'
    ];
    return gradients[name.charCodeAt(0) % gradients.length];
  }
}
