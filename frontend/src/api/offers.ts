import apiClient from './client';
import type { OfferComparisonRequest } from '../types';

export const offersApi = {
  getAll: () =>
    apiClient.get<Record<string, unknown>[]>('/offers').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Record<string, unknown>>(`/offers/${id}`).then((r) => r.data),

  create: (data: OfferComparisonRequest) =>
    apiClient.post<Record<string, unknown>>('/offers', data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/offers/${id}`),
};
