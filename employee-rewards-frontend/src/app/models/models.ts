export interface Employee {
  id?: number;
  name: string;
  department: string;
  email: string;
  jobTitle?: string;
  profileImageUrl?: string;
  rewardCount?: number;
  totalPoints?: number;
}

export interface Reward {
  id?: number;
  employeeId: number;
  employeeName?: string;
  employeeDepartment?: string;
  rewardName: string;
  dateAwarded?: string;
  rewardType?: string;
  points?: number;
  description?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface DashboardStats {
  totalEmployees: number;
  totalRewards: number;
  totalPoints: number;
  topEmployee?: string;
}
