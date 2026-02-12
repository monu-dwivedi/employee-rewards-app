import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { EmployeeService } from '../../services/employee.service';
import { RewardService } from '../../services/reward.service';
import { Employee, Reward } from '../../models/models';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, MatCardModule, MatIconModule, MatButtonModule, MatChipsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  totalEmployees = 0;
  totalRewards = 0;
  totalPoints = 0;
  totalDepartments = 0;
  topEmployees: Employee[] = [];
  recentRewards: Reward[] = [];

  constructor(
    private employeeService: EmployeeService,
    private rewardService: RewardService
  ) {}

  ngOnInit(): void {
    forkJoin({
      employees: this.employeeService.getAllEmployees(),
      rewards: this.rewardService.getAllRewards(),
      departments: this.employeeService.getAllDepartments()
    }).subscribe(({ employees, rewards, departments }) => {
      this.totalEmployees = employees.length;
      this.totalRewards = rewards.length;
      this.totalPoints = rewards.reduce((sum, r) => sum + (r.points || 0), 0);
      this.totalDepartments = departments.length;
      this.topEmployees = [...employees]
        .sort((a, b) => (b.totalPoints || 0) - (a.totalPoints || 0))
        .slice(0, 5);
      this.recentRewards = rewards.slice(0, 5);
    });
  }

  getAvatarColor(name: string): string {
    const colors = ['#e94560', '#4facfe', '#43e97b', '#f093fb', '#f5a623', '#667eea'];
    const idx = name.charCodeAt(0) % colors.length;
    return colors[idx];
  }
}
