import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { RewardService } from '../../services/reward.service';
import { EmployeeService } from '../../services/employee.service';
import { Reward, Employee } from '../../models/models';
import { forkJoin } from 'rxjs';

// --- Assign Reward Dialog ---------------------------------------------------
@Component({
  selector: 'app-assign-reward-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatSelectModule, MatIconModule,
    MatDatepickerModule, MatNativeDateModule],
  templateUrl: './assign-reward.component.html',
  styleUrls: ['./assign-reward.component.css']
})
export class AssignRewardDialogComponent implements OnInit {
  form: FormGroup;
  employees: Employee[] = [];
  rewardTypes: string[] = [];

  get today(): Date {
  return new Date();
}

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private rewardService: RewardService,
    private dialogRef: MatDialogRef<AssignRewardDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { reward?: Reward }
  ) {
    this.form = this.fb.group({
      employeeId: [data.reward?.employeeId || null, Validators.required],
      rewardName: [data.reward?.rewardName || '', Validators.required],
      rewardType: [data.reward?.rewardType || ''],
      points: [data.reward?.points || 100],
      dateAwarded: [data.reward?.dateAwarded ? new Date(data.reward.dateAwarded) : new Date()],
      description: [data.reward?.description || '']
    });
  }

  ngOnInit(): void {
    forkJoin({
      employees: this.employeeService.getAllEmployees(),
      types: this.rewardService.getRewardTypes()
    }).subscribe(({ employees, types }) => {
      this.employees = employees;
      this.rewardTypes = types;
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const val = this.form.value;
      const date = val.dateAwarded instanceof Date
        ? val.dateAwarded.toISOString().split('T')[0]
        : val.dateAwarded;
      this.dialogRef.close({ ...val, dateAwarded: date });
    }
  }
}
