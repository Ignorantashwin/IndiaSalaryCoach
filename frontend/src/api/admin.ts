import apiClient from './client';

export const adminApi = {
  getStats: () =>
    apiClient.get<Record<string, unknown>>('/admin/stats').then((r) => r.data),

  getUsers: (page = 1, limit = 20, search?: string) =>
    apiClient.get<Record<string, unknown>>('/admin/users', {
      params: { page, limit, search },
    }).then((r) => r.data),

  getSubscriptions: () =>
    apiClient.get<Record<string, unknown>[]>('/admin/subscriptions').then((r) => r.data),

  getRevenue: () =>
    apiClient.get<Record<string, unknown>>('/admin/revenue').then((r) => r.data),
};
