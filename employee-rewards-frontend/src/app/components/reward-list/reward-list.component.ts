import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { RewardService } from '../../services/reward.service';
import { Reward } from '../../models/models';
import { AssignRewardDialogComponent } from './assign-reward.component';

// ── Reward List Component ─────────────────────────────────────────────────────
@Component({
  selector: 'app-reward-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule,
    MatDialogModule, MatSnackBarModule, MatChipsModule, MatTooltipModule,
    MatProgressSpinnerModule, MatTableModule, MatFormFieldModule, MatInputModule],
  templateUrl: './reward-list.component.html',
  styleUrls: ['./reward-list.component.css']
})
export class RewardListComponent implements OnInit {
  rewards: Reward[] = [];
  filteredRewards: Reward[] = [];
  filterTypes = ['All', 'Performance', 'Innovation', 'Teamwork', 'Customer Service'];
  activeFilter = 'All';
  loading = true;

  constructor(
    private rewardService: RewardService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadRewards();
  }

  loadRewards(): void {
    this.loading = true;
    this.rewardService.getAllRewards().subscribe({
      next: data => {
        this.rewards = data;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Failed to load rewards', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  setFilter(type: string): void {
    this.activeFilter = type;
    this.applyFilter();
  }

  applyFilter(): void {
    this.filteredRewards = this.activeFilter === 'All'
      ? this.rewards
      : this.rewards.filter(r => r.rewardType === this.activeFilter);
  }

  openAssignDialog(): void {
    const ref = this.dialog.open(AssignRewardDialogComponent, {
      data: {},
      width: '520px'
    });
    ref.afterClosed().subscribe(result => {
      if (result) {
        this.rewardService.assignReward(result).subscribe({
          next: () => {
            this.loadRewards();
            this.snackBar.open('Reward assigned!', 'Close', { duration: 3000 });
          },
          error: () => this.snackBar.open('Failed to assign reward', 'Close', { duration: 3000 })
        });
      }
    });
  }

  openEditDialog(reward: Reward): void {
    const ref = this.dialog.open(AssignRewardDialogComponent, {
      data: { reward },
      width: '520px'
    });
    ref.afterClosed().subscribe(result => {
      if (result) {
        this.rewardService.updateReward(reward.id!, result).subscribe({
          next: () => {
            this.loadRewards();
            this.snackBar.open('Reward updated!', 'Close', { duration: 3000 });
          },
          error: () => this.snackBar.open('Failed to update', 'Close', { duration: 3000 })
        });
      }
    });
  }

  deleteReward(reward: Reward): void {
    if (confirm(`Delete the "${reward.rewardName}" reward?`)) {
      this.rewardService.deleteReward(reward.id!).subscribe({
        next: () => {
          this.loadRewards();
          this.snackBar.open('Reward deleted', 'Close', { duration: 3000 });
        },
        error: () => this.snackBar.open('Failed to delete', 'Close', { duration: 3000 })
      });
    }
  }

  getTypeGradient(type?: string): string {
    const map: Record<string, string> = {
      'Performance': 'linear-gradient(135deg, #f093fb, #f5576c)',
      'Innovation': 'linear-gradient(135deg, #4facfe, #00f2fe)',
      'Teamwork': 'linear-gradient(135deg, #43e97b, #38f9d7)',
      'Customer Service': 'linear-gradient(135deg, #fa709a, #fee140)',
    };
    return map[type || ''] || 'linear-gradient(135deg, #667eea, #764ba2)';
  }

  getAvatarColor(name: string): string {
    const colors = ['#e94560', '#4facfe', '#43e97b', '#f093fb', '#f5a623', '#667eea'];
    return colors[name.charCodeAt(0) % colors.length];
  }
}
