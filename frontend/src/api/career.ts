import apiClient from './client';
import type { CareerReport, CareerReportRequest } from '../types';

export const careerApi = {
  getReports: () =>
    apiClient.get<CareerReport[]>('/career/reports').then((r) => r.data),

  generateReport: (data: CareerReportRequest) =>
    apiClient.post<CareerReport>('/career/reports', data).then((r) => r.data),

  getReport: (id: number) =>
    apiClient.get<CareerReport>(`/career/reports/${id}`).then((r) => r.data),

  chat: (message: string, context?: string) =>
    apiClient.post<Record<string, unknown>>('/career/chat', {
      message,
      context,
    }).then((r) => r.data),

  getInterviewPrep: (targetRole: string, industry: string, skills: string, experienceLevel: string) =>
    apiClient.post<Record<string, unknown>>('/career/interview-prep', {
      targetRole,
      industry,
      skills,
      experienceLevel,
    }).then((r) => r.data),

  generateInterviewQuestions: (role: string, level: string) =>
    apiClient.post<string>('/career/interview-questions', {
      role,
      level,
    }).then((r) => r.data),

  generateNegotiationLetter: (name: string, company: string, role: string, currentOffer: string, expectedSalary: string) =>
    apiClient.post<string>('/career/salary-negotiation', {
      name,
      company,
      role,
      currentOffer,
      expectedSalary,
    }).then((r) => r.data),
};
