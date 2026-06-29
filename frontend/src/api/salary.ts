import apiClient from './client';
import type {
  SalarySearchRequest,
  SalaryResultResponse,
  SalarySearchHistory,
  CityInsight,
  SkillImpact,
  IndustryComparison,
} from '../types';

export const salaryApi = {
  search: (data: SalarySearchRequest) =>
    apiClient.post<SalaryResultResponse>('/salary/search', data).then((r) => r.data),

  getSearchHistory: () =>
    apiClient.get<SalarySearchHistory[]>('/salary/searches').then((r) => r.data),

  getTopCities: (jobTitle: string, industry?: string) =>
    apiClient.get<CityInsight[]>('/salary/top-cities', {
      params: { jobTitle, industry },
    }).then((r) => r.data),

  getSkillImpact: (jobTitle: string, skills: string) =>
    apiClient.get<SkillImpact[]>('/salary/skill-impact', {
      params: { jobTitle, skills },
    }).then((r) => r.data),

  getIndustryComparison: (jobTitle: string) =>
    apiClient.get<IndustryComparison[]>('/salary/industry-comparison', {
      params: { jobTitle },
    }).then((r) => r.data),

  getMarketTrends: (industry?: string, city?: string) =>
    apiClient.get<Record<string, unknown>[]>('/salary/market-trends', {
      params: { industry, city },
    }).then((r) => r.data),
};
