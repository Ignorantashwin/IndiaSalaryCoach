import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { Check, CreditCard, Clock, X } from 'lucide-react';
import toast from 'react-hot-toast';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { subscriptionApi } from '../../api/subscription';
import type { Payment } from '../../types';

export function SubscriptionPage() {
  const [billingCycle, setBillingCycle] = useState<'MONTHLY' | 'ANNUAL'>('MONTHLY');
  const queryClient = useQueryClient();

  const { data: plans, isLoading: plansLoading } = useQuery({
    queryKey: ['plans'],
    queryFn: subscriptionApi.getPlans,
  });

  const { data: currentSub, isLoading: subLoading } = useQuery({
    queryKey: ['subscription'],
    queryFn: subscriptionApi.getCurrentSubscription,
  });

  const { data: payments } = useQuery({
    queryKey: ['payments'],
    queryFn: subscriptionApi.getPaymentHistory,
  });

  const checkoutMutation = useMutation({
    mutationFn: subscriptionApi.createCheckout,
    onSuccess: (data) => {
      if (data.orderId) {
        toast.success('Redirecting to payment...');
      }
    },
    onError: () => toast.error('Checkout failed'),
  });

  const cancelMutation = useMutation({
    mutationFn: subscriptionApi.cancelSubscription,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['subscription'] });
      toast.success('Subscription cancelled');
    },
    onError: () => toast.error('Cancellation failed'),
  });

  if (plansLoading || subLoading) return <CardSkeleton />;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-8">
      <div className="text-center">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Subscription Plans</h1>
        <p className="text-gray-600 dark:text-gray-400 mt-2">Choose the perfect plan for your career growth</p>

        <div className="inline-flex items-center gap-1 bg-gray-100 dark:bg-gray-800 rounded-lg p-1 mt-4">
          <button
            onClick={() => setBillingCycle('MONTHLY')}
            className={`px-4 py-2 text-sm font-medium rounded-md transition-colors ${
              billingCycle === 'MONTHLY' ? 'bg-white dark:bg-gray-700 shadow-sm' : ''
            }`}
          >
            Monthly
          </button>
          <button
            onClick={() => setBillingCycle('ANNUAL')}
            className={`px-4 py-2 text-sm font-medium rounded-md transition-colors ${
              billingCycle === 'ANNUAL' ? 'bg-white dark:bg-gray-700 shadow-sm' : ''
            }`}
          >
            Annual <Badge variant="success" className="ml-1">Save 20%</Badge>
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl mx-auto">
        {(plans || []).map((plan: Record<string, unknown>, i: number) => {
          const isCurrentPlan = currentSub?.planId === plan.id;
          const isPro = (plan.name as string)?.toLowerCase().includes('pro');
          return (
            <motion.div
              key={plan.id as string}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.1 }}
            >
              <Card className={`p-6 relative ${isPro ? 'border-primary-500 ring-2 ring-primary-500/20' : ''}`}>
                {isPro && (
                  <div className="absolute -top-3 left-1/2 -translate-x-1/2">
                    <Badge variant="info">Most Popular</Badge>
                  </div>
                )}
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">{plan.name as string}</h3>
                <div className="mt-4">
                  <span className="text-3xl font-bold text-gray-900 dark:text-white">
                    ₹{billingCycle === 'ANNUAL' ? ((plan.annualPrice as number) || (plan.price as number)) : (plan.price as number)}
                  </span>
                  <span className="text-gray-500">/{billingCycle === 'ANNUAL' ? 'year' : 'month'}</span>
                </div>
                <ul className="mt-6 space-y-3">
                  {((plan.features as string[]) || []).map((feature: string) => (
                    <li key={feature} className="flex items-center gap-2 text-sm text-gray-600 dark:text-gray-400">
                      <Check className="w-4 h-4 text-green-500 shrink-0" />
                      {feature}
                    </li>
                  ))}
                </ul>
                <Button
                  className="w-full mt-6"
                  variant={isPro ? 'primary' : 'outline'}
                  disabled={isCurrentPlan}
                  onClick={() => checkoutMutation.mutate({ planId: plan.id as string, billingCycle })}
                  loading={checkoutMutation.isPending}
                >
                  {isCurrentPlan ? 'Current Plan' : 'Subscribe'}
                </Button>
              </Card>
            </motion.div>
          );
        })}
      </div>

      {currentSub && (
        <Card className="p-6 max-w-2xl mx-auto">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Current Subscription</h2>
          <div className="space-y-3">
            <div className="flex justify-between">
              <span className="text-gray-600 dark:text-gray-400">Plan</span>
              <span className="font-medium text-gray-900 dark:text-white">{currentSub.planName}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600 dark:text-gray-400">Status</span>
              <Badge variant={currentSub.status === 'ACTIVE' ? 'success' : 'warning'}>
                {currentSub.status}
              </Badge>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600 dark:text-gray-400">Billing Cycle</span>
              <span className="font-medium text-gray-900 dark:text-white">{currentSub.billingCycle}</span>
            </div>
            {currentSub.currentPeriodEnd && (
              <div className="flex justify-between">
                <span className="text-gray-600 dark:text-gray-400">Renews on</span>
                <span className="font-medium text-gray-900 dark:text-white">
                  {new Date(currentSub.currentPeriodEnd).toLocaleDateString()}
                </span>
              </div>
            )}
            {currentSub.status === 'ACTIVE' && !currentSub.cancelAtPeriodEnd && (
              <Button
                variant="destructive"
                size="sm"
                onClick={() => cancelMutation.mutate()}
                loading={cancelMutation.isPending}
                className="mt-4"
              >
                <X className="w-4 h-4 mr-2" />
                Cancel Subscription
              </Button>
            )}
          </div>
        </Card>
      )}

      {payments && payments.length > 0 && (
        <Card className="p-6 max-w-2xl mx-auto">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Payment History</h2>
          <div className="space-y-3">
            {payments.map((payment: Payment) => (
              <div key={payment.id} className="flex items-center justify-between py-3 border-b border-gray-100 dark:border-gray-800 last:border-0">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-gray-100 dark:bg-gray-800 rounded-lg">
                    {payment.status === 'SUCCESS' ? (
                      <CreditCard className="w-4 h-4 text-green-600" />
                    ) : (
                      <Clock className="w-4 h-4 text-yellow-600" />
                    )}
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-900 dark:text-white">{payment.planName}</p>
                    <p className="text-xs text-gray-500">{new Date(payment.createdAt).toLocaleDateString()}</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-sm font-medium text-gray-900 dark:text-white">
                    {payment.currency} {payment.amount}
                  </p>
                  <Badge variant={payment.status === 'SUCCESS' ? 'success' : payment.status === 'PENDING' ? 'warning' : 'error'}>
                    {payment.status}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </Card>
      )}
    </motion.div>
  );
}
