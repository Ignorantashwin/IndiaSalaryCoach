import apiClient from './client';
import type { Resume, ResumeRequest } from '../types';

export const resumeApi = {
  getAll: () =>
    apiClient.get<Resume[]>('/resumes').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Resume>(`/resumes/${id}`).then((r) => r.data),

  create: (data: ResumeRequest) =>
    apiClient.post<Resume>('/resumes', data).then((r) => r.data),

  update: (id: number, updates: Record<string, unknown>) =>
    apiClient.patch<Resume>(`/resumes/${id}`, updates).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/resumes/${id}`),

  score: (id: number, jobDescription: string, targetRole: string) =>
    apiClient.post<Record<string, unknown>>(`/resumes/${id}/score`, {
      jobDescription,
      targetRole,
    }).then((r) => r.data),

  optimize: (id: number, targetRole: string) =>
    apiClient.post<Record<string, unknown>>(`/resumes/${id}/optimize`, {
      targetRole,
    }).then((r) => r.data),

  aiOptimize: (id: number, targetRole: string) =>
    apiClient.post<string>(`/resumes/${id}/ai-optimize`, {
      targetRole,
    }).then((r) => r.data),

  download: (id: number, optimized = false) =>
    apiClient.get(`/resumes/${id}/download`, {
      params: { optimized },
      responseType: 'blob',
    }).then((r) => r.data),
};
