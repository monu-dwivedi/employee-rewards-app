import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Employee, ApiResponse } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) {}

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<ApiResponse<Employee[]>>(this.apiUrl).pipe(
      map(res => res.data)
    );
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<ApiResponse<Employee>>(`${this.apiUrl}/${id}`).pipe(
      map(res => res.data)
    );
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<ApiResponse<Employee>>(this.apiUrl, employee).pipe(
      map(res => res.data)
    );
  }

  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.http.put<ApiResponse<Employee>>(`${this.apiUrl}/${id}`, employee).pipe(
      map(res => res.data)
    );
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`).pipe(
      map(() => undefined)
    );
  }

  getEmployeesByDepartment(department: string): Observable<Employee[]> {
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}/department/${department}`).pipe(
      map(res => res.data)
    );
  }

  searchEmployees(name: string): Observable<Employee[]> {
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}/search?name=${name}`).pipe(
      map(res => res.data)
    );
  }

  getAllDepartments(): Observable<string[]> {
    return this.http.get<ApiResponse<string[]>>(`${this.apiUrl}/departments`).pipe(
      map(res => res.data)
    );
  }
}
