import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useQuery, useMutation } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { Search, TrendingUp, MapPin, Briefcase, BarChart3, History } from 'lucide-react';
import toast from 'react-hot-toast';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  LineChart, Line, Legend,
} from 'recharts';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { salaryApi } from '../../api/salary';
import type { SalarySearchRequest, SalaryResultResponse, SalarySearchHistory } from '../../types';

export function SalaryPage() {
  const [result, setResult] = useState<SalaryResultResponse | null>(null);
  const [activeTab, setActiveTab] = useState<'search' | 'history' | 'cities' | 'skills' | 'industry' | 'trends'>('search');

  const { register, handleSubmit, watch } = useForm<SalarySearchRequest>();
  const jobTitle = watch('jobTitle');

  const searchMutation = useMutation({
    mutationFn: salaryApi.search,
    onSuccess: (data) => {
      setResult(data);
      toast.success('Salary data found!');
    },
    onError: () => toast.error('Search failed'),
  });

  const { data: history, isLoading: historyLoading } = useQuery({
    queryKey: ['salary-history'],
    queryFn: salaryApi.getSearchHistory,
    enabled: activeTab === 'history',
  });

  const { data: topCities } = useQuery({
    queryKey: ['top-cities', jobTitle],
    queryFn: () => salaryApi.getTopCities(jobTitle),
    enabled: activeTab === 'cities' && !!jobTitle,
  });

  const { data: skillImpact } = useQuery({
    queryKey: ['skill-impact', jobTitle],
    queryFn: () => salaryApi.getSkillImpact(jobTitle, 'React,Node.js,Python'),
    enabled: activeTab === 'skills' && !!jobTitle,
  });

  const { data: industryComparison } = useQuery({
    queryKey: ['industry-comparison', jobTitle],
    queryFn: () => salaryApi.getIndustryComparison(jobTitle),
    enabled: activeTab === 'industry' && !!jobTitle,
  });

  const { data: marketTrends } = useQuery({
    queryKey: ['market-trends'],
    queryFn: () => salaryApi.getMarketTrends(),
    enabled: activeTab === 'trends',
  });

  const tabs = [
    { id: 'search', label: 'Search', icon: Search },
    { id: 'history', label: 'History', icon: History },
    { id: 'cities', label: 'Top Cities', icon: MapPin },
    { id: 'skills', label: 'Skill Impact', icon: TrendingUp },
    { id: 'industry', label: 'Industry', icon: Briefcase },
    { id: 'trends', label: 'Trends', icon: BarChart3 },
  ] as const;

  const onSubmit = (data: SalarySearchRequest) => searchMutation.mutate(data);

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Salary Intelligence</h1>

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

      {activeTab === 'search' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Card className="p-6">
            <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Search Salary Data</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <Input label="Job Title" placeholder="e.g. Software Engineer" {...register('jobTitle', { required: true })} />
              <Input label="City" placeholder="e.g. Bangalore" {...register('city', { required: true })} />
              <Input label="Industry" placeholder="e.g. IT/Software" {...register('industry', { required: true })} />
              <Input label="Experience (years)" type="number" {...register('experienceYears', { required: true, valueAsNumber: true })} />
              <Input label="Skills (optional)" placeholder="React, Node.js" {...register('skills')} />
              <Button type="submit" className="w-full" loading={searchMutation.isPending}>
                <Search className="w-4 h-4 mr-2" />
                Search
              </Button>
            </form>
          </Card>

          <div className="lg:col-span-2 space-y-4">
            {result && (
              <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-4">
                <Card className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="font-semibold text-gray-900 dark:text-white">{result.jobTitle}</h3>
                    <Badge variant="info">{result.city}</Badge>
                  </div>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div className="text-center p-3 bg-gray-50 dark:bg-gray-800 rounded-lg">
                      <div className="text-xs text-gray-500">Min</div>
                      <div className="text-lg font-bold text-gray-900 dark:text-white">
                        {result.currency} {(result.minSalary / 100000).toFixed(1)}L
                      </div>
                    </div>
                    <div className="text-center p-3 bg-primary-50 dark:bg-primary-900/20 rounded-lg">
                      <div className="text-xs text-gray-500">Median</div>
                      <div className="text-lg font-bold text-primary-600">
                        {result.currency} {(result.medianSalary / 100000).toFixed(1)}L
                      </div>
                    </div>
                    <div className="text-center p-3 bg-gray-50 dark:bg-gray-800 rounded-lg">
                      <div className="text-xs text-gray-500">Max</div>
                      <div className="text-lg font-bold text-gray-900 dark:text-white">
                        {result.currency} {(result.maxSalary / 100000).toFixed(1)}L
                      </div>
                    </div>
                    <div className="text-center p-3 bg-gray-50 dark:bg-gray-800 rounded-lg">
                      <div className="text-xs text-gray-500">Market Avg</div>
                      <div className="text-lg font-bold text-gray-900 dark:text-white">
                        {result.currency} {(result.marketAverage / 100000).toFixed(1)}L
                      </div>
                    </div>
                  </div>
                  <div className="mt-4 text-xs text-gray-500">
                    Based on {result.dataPoints} data points | Source: {result.dataSource}
                  </div>
                </Card>

                {result.percentileRanks && result.percentileRanks.length > 0 && (
                  <Card className="p-6">
                    <h3 className="font-semibold text-gray-900 dark:text-white mb-4">Salary Distribution</h3>
                    <ResponsiveContainer width="100%" height={200}>
                      <BarChart data={result.percentileRanks}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="percentile" tickFormatter={(v) => `${v}%`} />
                        <YAxis tickFormatter={(v) => `${(v / 100000).toFixed(0)}L`} />
                        <Tooltip formatter={(v) => `${(Number(v) / 100000).toFixed(1)}L`} />
                        <Bar dataKey="salary" fill="#3b82f6" radius={[4, 4, 0, 0]} />
                      </BarChart>
                    </ResponsiveContainer>
                  </Card>
                )}
              </motion.div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'history' && (
        historyLoading ? <CardSkeleton /> : (
          <Card className="p-6">
            <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Search History</h2>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-gray-200 dark:border-gray-700">
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Job Title</th>
                    <th className="text-left py-3 px-2 font-medium text-gray-500">City</th>
                    <th className="text-left py-3 px-2 font-medium text-gray-500">Industry</th>
                    <th className="text-right py-3 px-2 font-medium text-gray-500">Median</th>
                    <th className="text-right py-3 px-2 font-medium text-gray-500">Date</th>
                  </tr>
                </thead>
                <tbody>
                  {(history as SalarySearchHistory[] || []).map((item) => (
                    <tr key={item.id} className="border-b border-gray-100 dark:border-gray-800">
                      <td className="py-3 px-2 text-gray-900 dark:text-white">{item.jobTitle}</td>
                      <td className="py-3 px-2 text-gray-600 dark:text-gray-400">{item.city}</td>
                      <td className="py-3 px-2 text-gray-600 dark:text-gray-400">{item.industry}</td>
                      <td className="py-3 px-2 text-right font-medium text-gray-900 dark:text-white">
                        {item.medianSalary ? `${(item.medianSalary / 100000).toFixed(1)}L` : '-'}
                      </td>
                      <td className="py-3 px-2 text-right text-gray-500">
                        {new Date(item.createdAt).toLocaleDateString()}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </Card>
        )
      )}

      {activeTab === 'cities' && topCities && (
        <Card className="p-6">
          <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Top Cities for {jobTitle || 'your role'}</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={topCities}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="city" />
              <YAxis tickFormatter={(v) => `${(v / 100000).toFixed(0)}L`} />
              <Tooltip formatter={(v) => `${(Number(v) / 100000).toFixed(1)}L`} />
              <Bar dataKey="averageSalary" fill="#3b82f6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </Card>
      )}

      {activeTab === 'skills' && skillImpact && (
        <Card className="p-6">
          <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Skill Impact Analysis</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={skillImpact} layout="vertical">
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis type="number" tickFormatter={(v) => `${v}%`} />
              <YAxis type="category" dataKey="skill" width={100} />
              <Tooltip formatter={(v) => `${v}%`} />
              <Bar dataKey="percentageIncrease" fill="#10b981" radius={[0, 4, 4, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </Card>
      )}

      {activeTab === 'industry' && industryComparison && (
        <Card className="p-6">
          <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Industry Comparison</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={industryComparison}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="industry" />
              <YAxis tickFormatter={(v) => `${(v / 100000).toFixed(0)}L`} />
              <Tooltip formatter={(v) => `${(Number(v) / 100000).toFixed(1)}L`} />
              <Legend />
              <Bar dataKey="averageSalary" fill="#3b82f6" name="Avg Salary" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </Card>
      )}

      {activeTab === 'trends' && marketTrends && (
        <Card className="p-6">
          <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Market Trends</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={marketTrends}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="period" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="averageSalary" stroke="#3b82f6" name="Average Salary" />
              <Line type="monotone" dataKey="demand" stroke="#10b981" name="Demand" />
            </LineChart>
          </ResponsiveContainer>
        </Card>
      )}
    </motion.div>
  );
}
