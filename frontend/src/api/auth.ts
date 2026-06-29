import apiClient from './client';
import type { AuthResponse, LoginRequest, RegisterRequest } from '../types';

export const authApi = {
  login: (data: LoginRequest) =>
    apiClient.post<AuthResponse>('/auth/login', data).then((r) => r.data),

  register: (data: RegisterRequest) =>
    apiClient.post<AuthResponse>('/auth/register', data).then((r) => r.data),

  googleAuth: (idToken: string) =>
    apiClient.post<AuthResponse>('/auth/google', { idToken }).then((r) => r.data),

  refresh: (refreshToken: string) =>
    apiClient.post<AuthResponse>('/auth/refresh', { refreshToken }).then((r) => r.data),

  logout: () => apiClient.post('/auth/logout'),

  forgotPassword: (email: string) =>
    apiClient.post<string>('/auth/forgot-password', { email }).then((r) => r.data),

  resetPassword: (token: string, newPassword: string) =>
    apiClient.post<string>('/auth/reset-password', { token, newPassword }).then((r) => r.data),
};
