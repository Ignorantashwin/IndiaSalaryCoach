import apiClient from './client';
import type { User } from '../types';

export const userApi = {
  getMe: () =>
    apiClient.get<User>('/users/me').then((r) => r.data),

  updateMe: (updates: Record<string, unknown>) =>
    apiClient.patch<User>('/users/me', updates).then((r) => r.data),

  getDashboard: () =>
    apiClient.get<Record<string, unknown>>('/users/me/dashboard').then((r) => r.data),
};
