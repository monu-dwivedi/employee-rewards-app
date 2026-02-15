import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { RewardService } from '../../services/reward.service';
import { Reward } from '../../models/models';

export interface RewardDialogData {
  employeeId: number;
  employeeName: string;
  totalPoints: number;
}

const PAGE_SIZE = 5; // rewards loaded per batch

@Component({
  selector: 'app-employee-rewards-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './employee-rewards-dialog.component.html',
  styleUrls: ['./employee-rewards-dialog.component.css']
})
export class EmployeeRewardsDialogComponent implements OnInit, OnDestroy {

  readonly PAGE_SIZE = PAGE_SIZE;

  allRewards: Reward[]     = []; 
  visibleRewards: Reward[] = []; 

  initialLoading = true;
  loadingMore    = false;
  allLoaded      = false;

  constructor(
    private rewardService: RewardService,
    private dialogRef: MatDialogRef<EmployeeRewardsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RewardDialogData
  ) {}

  ngOnInit(): void {
    // Fetch ALL rewards for this employee in one go
    this.rewardService.getRewardsByEmployee(this.data.employeeId).subscribe({
      next: rewards => {
        this.allRewards    = rewards;
        this.initialLoading = false;
        this.loadNextBatch(); 
      },
      error: () => {
        this.initialLoading = false;
      }
    });
  }

  ngOnDestroy(): void {}

  
  onScroll(event: Event): void {
    const el = event.target as HTMLElement;
    const nearBottom = el.scrollTop + el.clientHeight >= el.scrollHeight - 60;
    if (nearBottom && !this.loadingMore && !this.allLoaded) {
      this.loadNextBatch();
    }
  }

  private loadNextBatch(): void {
    if (this.allLoaded || this.loadingMore) return;

    const nextIndex = this.visibleRewards.length;
    if (nextIndex >= this.allRewards.length) {
      this.allLoaded = true;
      return;
    }

    this.loadingMore = true;

    setTimeout(() => {
      const nextBatch = this.allRewards.slice(nextIndex, nextIndex + PAGE_SIZE);
      this.visibleRewards = [...this.visibleRewards, ...nextBatch];
      this.loadingMore = false;
      if (this.visibleRewards.length >= this.allRewards.length) {
        this.allLoaded = true;
      }
    }, 400);
  }

  close(): void {
    this.dialogRef.close();
  }

  typeGradient(type?: string): string {
    const m: Record<string, string> = {
      'Performance': 'linear-gradient(135deg, #f093fb, #f5576c)',
      'Innovation':  'linear-gradient(135deg, #4facfe, #00f2fe)',
      'Teamwork':    'linear-gradient(135deg, #43e97b, #38f9d7)',
      'Excellence':  'linear-gradient(135deg, #fa709a, #fee140)',
    };
    return m[type || ''] || 'linear-gradient(135deg, #667eea, #764ba2)';
  }

  typeColor(type?: string): string {
    const m: Record<string, string> = {
      'Performance': '#f5576c',
      'Innovation':  '#4facfe',
      'Teamwork':    '#43e97b',
      'Excellence':  '#fa709a',
    };
    return m[type || ''] || '#667eea';
  }
}
