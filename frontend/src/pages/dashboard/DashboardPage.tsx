import { useQuery } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import {
  TrendingUp,
  FileText,
  Briefcase,
  Scale,
  CreditCard,
  ArrowUpRight,
} from 'lucide-react';
import { Link } from 'react-router-dom';
import { Card } from '../../components/ui/Card';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { userApi } from '../../api/user';
import { useAuthStore } from '../../stores/authStore';

export function DashboardPage() {
  const user = useAuthStore((s) => s.user);
  const { data: dashboard, isLoading } = useQuery({
    queryKey: ['dashboard'],
    queryFn: userApi.getDashboard,
  });

  const stats = [
    {
      label: 'Salary Searches',
      value: (dashboard?.salarySearches as number) || 0,
      icon: TrendingUp,
      color: 'text-blue-600',
      bg: 'bg-blue-50 dark:bg-blue-900/20',
      link: '/salary',
    },
    {
      label: 'Resumes',
      value: (dashboard?.resumes as number) || 0,
      icon: FileText,
      color: 'text-emerald-600',
      bg: 'bg-emerald-50 dark:bg-emerald-900/20',
      link: '/resume',
    },
    {
      label: 'Career Reports',
      value: (dashboard?.careerReports as number) || 0,
      icon: Briefcase,
      color: 'text-purple-600',
      bg: 'bg-purple-50 dark:bg-purple-900/20',
      link: '/career',
    },
    {
      label: 'Offer Analyses',
      value: (dashboard?.offerAnalyses as number) || 0,
      icon: Scale,
      color: 'text-amber-600',
      bg: 'bg-amber-50 dark:bg-amber-900/20',
      link: '/offers',
    },
  ];

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => <CardSkeleton key={i} />)}
        </div>
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-6"
    >
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Welcome back, {user?.name?.split(' ')[0]}
          </h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">
            Here's your career intelligence overview
          </p>
        </div>
        <Badge variant={user?.plan === 'free' ? 'default' : 'success'}>
          {user?.plan?.toUpperCase()} Plan
        </Badge>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat, i) => (
          <motion.div
            key={stat.label}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
          >
            <Link to={stat.link}>
              <Card hover className="p-5">
                <div className="flex items-center justify-between">
                  <div className={`p-2.5 rounded-lg ${stat.bg}`}>
                    <stat.icon className={`w-5 h-5 ${stat.color}`} />
                  </div>
                  <ArrowUpRight className="w-4 h-4 text-gray-400" />
                </div>
                <div className="mt-4">
                  <div className="text-2xl font-bold text-gray-900 dark:text-white">
                    {stat.value}
                  </div>
                  <div className="text-sm text-gray-600 dark:text-gray-400 mt-0.5">
                    {stat.label}
                  </div>
                </div>
              </Card>
            </Link>
          </motion.div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-6">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            Quick Actions
          </h2>
          <div className="grid grid-cols-2 gap-3">
            {[
              { label: 'Search Salary', link: '/salary', icon: TrendingUp },
              { label: 'Build Resume', link: '/resume', icon: FileText },
              { label: 'Career Chat', link: '/career', icon: Briefcase },
              { label: 'Compare Offers', link: '/offers', icon: Scale },
            ].map((action) => (
              <Link
                key={action.label}
                to={action.link}
                className="flex items-center gap-3 p-3 rounded-lg border border-gray-200 dark:border-gray-800 hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors"
              >
                <action.icon className="w-4 h-4 text-primary-600" />
                <span className="text-sm font-medium text-gray-700 dark:text-gray-300">
                  {action.label}
                </span>
              </Link>
            ))}
          </div>
        </Card>

        <Card className="p-6">
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">
            Subscription Status
          </h2>
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600 dark:text-gray-400">Current Plan</span>
              <Badge variant={user?.plan === 'free' ? 'default' : 'success'}>
                {user?.plan?.toUpperCase()}
              </Badge>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600 dark:text-gray-400">Status</span>
              <Badge variant="success">Active</Badge>
            </div>
            {user?.plan === 'free' && (
              <Link
                to="/subscription"
                className="flex items-center justify-center gap-2 mt-4 p-3 bg-gradient-to-r from-primary-600 to-primary-700 text-white rounded-lg text-sm font-medium hover:from-primary-700 hover:to-primary-800 transition-all"
              >
                <CreditCard className="w-4 h-4" />
                Upgrade to Pro
              </Link>
            )}
          </div>
        </Card>
      </div>
    </motion.div>
  );
}
