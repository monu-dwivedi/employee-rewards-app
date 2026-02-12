import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Reward, ApiResponse } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RewardService {
  private apiUrl = `${environment.apiUrl}/rewards`;

  constructor(private http: HttpClient) {}

  getAllRewards(): Observable<Reward[]> {
    return this.http.get<ApiResponse<Reward[]>>(this.apiUrl).pipe(
      map(res => res.data)
    );
  }

  getRewardById(id: number): Observable<Reward> {
    return this.http.get<ApiResponse<Reward>>(`${this.apiUrl}/${id}`).pipe(
      map(res => res.data)
    );
  }

  getRewardsByEmployee(employeeId: number): Observable<Reward[]> {
    return this.http.get<ApiResponse<Reward[]>>(`${this.apiUrl}/employee/${employeeId}`).pipe(
      map(res => res.data)
    );
  }

  assignReward(reward: Reward): Observable<Reward> {
    return this.http.post<ApiResponse<Reward>>(this.apiUrl, reward).pipe(
      map(res => res.data)
    );
  }

  updateReward(id: number, reward: Reward): Observable<Reward> {
    return this.http.put<ApiResponse<Reward>>(`${this.apiUrl}/${id}`, reward).pipe(
      map(res => res.data)
    );
  }

  deleteReward(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`).pipe(
      map(() => undefined)
    );
  }

  getRewardTypes(): Observable<string[]> {
    return this.http.get<ApiResponse<string[]>>(`${this.apiUrl}/types`).pipe(
      map(res => res.data)
    );
  }
}
