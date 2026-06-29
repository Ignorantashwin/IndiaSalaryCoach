import apiClient from './client';
import type { Subscription, Payment, CheckoutRequest } from '../types';

export const subscriptionApi = {
  getPlans: () =>
    apiClient.get<Record<string, unknown>[]>('/plans').then((r) => r.data),

  getCurrentSubscription: () =>
    apiClient.get<Subscription | null>('/subscriptions/current').then((r) => r.data),

  createCheckout: (data: CheckoutRequest) =>
    apiClient.post<Record<string, unknown>>('/subscriptions/checkout', data).then((r) => r.data),

  verifyPayment: (body: Record<string, string>) =>
    apiClient.post<Subscription>('/subscriptions/verify', body).then((r) => r.data),

  cancelSubscription: () =>
    apiClient.post<Subscription>('/subscriptions/cancel').then((r) => r.data),

  getPaymentHistory: () =>
    apiClient.get<Payment[]>('/subscriptions/payments').then((r) => r.data),
};
