import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { Users, CreditCard, TrendingUp, BarChart3, Search } from 'lucide-react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
} from 'recharts';
import { Card } from '../../components/ui/Card';
import { Input } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { adminApi } from '../../api/admin';

export function AdminPage() {
  const [activeTab, setActiveTab] = useState<'stats' | 'users' | 'subscriptions' | 'revenue'>('stats');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(1);

  const { data: stats, isLoading: statsLoading } = useQuery({
    queryKey: ['admin-stats'],
    queryFn: adminApi.getStats,
  });

  const { data: usersData, isLoading: usersLoading } = useQuery({
    queryKey: ['admin-users', page, searchQuery],
    queryFn: () => adminApi.getUsers(page, 20, searchQuery || undefined),
    enabled: activeTab === 'users',
  });

  const { data: subscriptions } = useQuery({
    queryKey: ['admin-subscriptions'],
    queryFn: adminApi.getSubscriptions,
    enabled: activeTab === 'subscriptions',
  });

  const { data: revenue } = useQuery({
    queryKey: ['admin-revenue'],
    queryFn: adminApi.getRevenue,
    enabled: activeTab === 'revenue',
  });

  const tabs = [
    { id: 'stats', label: 'Statistics', icon: BarChart3 },
    { id: 'users', label: 'Users', icon: Users },
    { id: 'subscriptions', label: 'Subscriptions', icon: CreditCard },
    { id: 'revenue', label: 'Revenue', icon: TrendingUp },
  ] as const;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Admin Panel</h1>

      <div className="flex gap-2 overflow-x-auto pb-2">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors ${
              activeTab === tab.id
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-800 dark:text-gray-400'
            }`}
          >
            <tab.icon className="w-4 h-4" />
            {tab.label}
          </button>
        ))}
      </div>

      {activeTab === 'stats' && (
        statsLoading ? <CardSkeleton /> : (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              {[
                { label: 'Total Users', value: stats?.totalUsers, icon: Users, color: 'text-blue-600', bg: 'bg-blue-50 dark:bg-blue-900/20' },
                { label: 'Active Subscriptions', value: stats?.activeSubscriptions, icon: CreditCard, color: 'text-green-600', bg: 'bg-green-50 dark:bg-green-900/20' },
                { label: 'Total Revenue', value: stats?.totalRevenue ? `₹${((stats.totalRevenue as number) / 1000).toFixed(0)}K` : '₹0', icon: TrendingUp, color: 'text-purple-600', bg: 'bg-purple-50 dark:bg-purple-900/20' },
                { label: 'New Users (30d)', value: stats?.newUsersLast30Days, icon: BarChart3, color: 'text-amber-600', bg: 'bg-amber-50 dark:bg-amber-900/20' },
              ].map((stat) => (
                <Card key={stat.label} className="p-5">
                  <div className={`p-2.5 rounded-lg inline-block ${stat.bg}`}>
                    <stat.icon className={`w-5 h-5 ${stat.color}`} />
                  </div>
                  <div className="mt-3">
                    <div className="text-2xl font-bold text-gray-900 dark:text-white">{String(stat.value || 0)}</div>
                    <div className="text-sm text-gray-600 dark:text-gray-400">{stat.label}</div>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        )
      )}

      {activeTab === 'users' && (
        <Card className="p-6">
          <div className="flex items-center gap-4 mb-4">
            <div className="flex-1">
              <Input
                placeholder="Search users..."
                icon={<Search className="w-4 h-4" />}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
          </div>
          {usersLoading ? <CardSkeleton /> : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-gray-200 dark:border-gray-700">
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Name</th>
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Email</th>
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Plan</th>
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Role</th>
                    <th className="text-right py-3 px-2 font-medium text-gray-500">Joined</th>
                  </tr>
                </thead>
                <tbody>
                  {((usersData as Record<string, unknown>)?.users as Array<Record<string, unknown>> || []).map((user: Record<string, unknown>) => (
                    <tr key={user.id as number} className="border-b border-gray-100 dark:border-gray-800">
                      <td className="py-3 px-2 text-gray-900 dark:text-white font-medium">{user.name as string}</td>
                      <td className="py-3 px-2 text-gray-600 dark:text-gray-400">{user.email as string}</td>
                      <td className="py-3 px-2">
                        <Badge variant={(user.plan as string) === 'free' ? 'default' : 'success'}>{(user.plan as string)?.toUpperCase()}</Badge>
                      </td>
                      <td className="py-3 px-2">
                        <Badge variant={(user.role as string) === 'admin' ? 'info' : 'default'}>{user.role as string}</Badge>
                      </td>
                      <td className="py-3 px-2 text-right text-gray-500">
                        {new Date(user.createdAt as string).toLocaleDateString()}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="flex justify-center gap-2 mt-4">
                <button
                  onClick={() => setPage((p) => Math.max(1, p - 1))}
                  disabled={page === 1}
                  className="px-3 py-1 text-sm rounded border disabled:opacity-50"
                >
                  Previous
                </button>
                <span className="px-3 py-1 text-sm">Page {page}</span>
                <button
                  onClick={() => setPage((p) => p + 1)}
                  className="px-3 py-1 text-sm rounded border"
                >
                  Next
                </button>
              </div>
            </div>
          )}
        </Card>
      )}

      {activeTab === 'subscriptions' && subscriptions && (
        <Card className="p-6">
          <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Subscription Analytics</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={subscriptions as Record<string, unknown>[]}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="planName" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="count" fill="#3b82f6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </Card>
      )}

      {activeTab === 'revenue' && revenue && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Card className="p-5">
              <div className="text-sm text-gray-500">Total Revenue</div>
              <div className="text-2xl font-bold text-gray-900 dark:text-white mt-1">
                ₹{((revenue.totalRevenue as number) || 0).toLocaleString()}
              </div>
            </Card>
            <Card className="p-5">
              <div className="text-sm text-gray-500">This Month</div>
              <div className="text-2xl font-bold text-gray-900 dark:text-white mt-1">
                ₹{((revenue.monthlyRevenue as number) || 0).toLocaleString()}
              </div>
            </Card>
            <Card className="p-5">
              <div className="text-sm text-gray-500">Average Per User</div>
              <div className="text-2xl font-bold text-gray-900 dark:text-white mt-1">
                ₹{((revenue.avgRevenuePerUser as number) || 0).toLocaleString()}
              </div>
            </Card>
          </div>
        </div>
      )}
    </motion.div>
  );
}
