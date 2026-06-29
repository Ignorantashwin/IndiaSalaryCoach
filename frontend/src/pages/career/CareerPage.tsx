import { useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { motion } from 'framer-motion';
import { Briefcase, MessageSquare, GraduationCap, DollarSign, Plus, Send } from 'lucide-react';
import toast from 'react-hot-toast';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { Modal } from '../../components/ui/Modal';
import { careerApi } from '../../api/career';
import type { CareerReport, CareerReportRequest } from '../../types';

export function CareerPage() {
  const [activeTab, setActiveTab] = useState<'reports' | 'chat' | 'interview' | 'negotiation'>('reports');
  const [showReportForm, setShowReportForm] = useState(false);
  const queryClient = useQueryClient();

  const tabs = [
    { id: 'reports', label: 'Career Reports', icon: Briefcase },
    { id: 'chat', label: 'AI Chat', icon: MessageSquare },
    { id: 'interview', label: 'Interview Prep', icon: GraduationCap },
    { id: 'negotiation', label: 'Negotiation', icon: DollarSign },
  ] as const;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Career Coach</h1>
        {activeTab === 'reports' && (
          <Button onClick={() => setShowReportForm(true)}>
            <Plus className="w-4 h-4 mr-2" />
            New Report
          </Button>
        )}
      </div>

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

      {activeTab === 'reports' && <CareerReports />}
      {activeTab === 'chat' && <CareerChat />}
      {activeTab === 'interview' && <InterviewPrep />}
      {activeTab === 'negotiation' && <SalaryNegotiation />}

      <Modal isOpen={showReportForm} onClose={() => setShowReportForm(false)} title="Generate Career Report" size="lg">
        <CareerReportForm onSuccess={() => { setShowReportForm(false); queryClient.invalidateQueries({ queryKey: ['career-reports'] }); }} />
      </Modal>
    </motion.div>
  );
}

function CareerReports() {
  const { data: reports, isLoading } = useQuery({
    queryKey: ['career-reports'],
    queryFn: careerApi.getReports,
  });

  if (isLoading) return <CardSkeleton />;

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {(reports || []).map((report: CareerReport) => (
        <Card key={report.id} hover className="p-5">
          <div className="flex items-start justify-between">
            <div>
              <h3 className="font-medium text-gray-900 dark:text-white">
                {report.currentRole} → {report.targetRole}
              </h3>
              <p className="text-xs text-gray-500 mt-1">
                {new Date(report.createdAt).toLocaleDateString()}
              </p>
            </div>
            <Badge variant="info">{report.timelineMonths} months</Badge>
          </div>
          {report.confidenceScore && (
            <div className="mt-3">
              <div className="flex justify-between text-xs text-gray-500 mb-1">
                <span>Confidence</span>
                <span>{Math.round(report.confidenceScore * 100)}%</span>
              </div>
              <div className="w-full h-2 bg-gray-100 dark:bg-gray-800 rounded-full">
                <div
                  className="h-full bg-primary-500 rounded-full"
                  style={{ width: `${report.confidenceScore * 100}%` }}
                />
              </div>
            </div>
          )}
          {report.salaryProjection && (
            <p className="mt-2 text-sm text-gray-600 dark:text-gray-400">
              Projected: <span className="font-medium text-gray-900 dark:text-white">₹{(report.salaryProjection / 100000).toFixed(1)}L</span>
            </p>
          )}
          <div className="mt-3">
            <p className="text-xs text-gray-500 line-clamp-2">{report.roadmap}</p>
          </div>
        </Card>
      ))}
    </div>
  );
}

function CareerReportForm({ onSuccess }: { onSuccess: () => void }) {
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm<CareerReportRequest>();

  const onSubmit = async (data: CareerReportRequest) => {
    setLoading(true);
    try {
      await careerApi.generateReport(data);
      toast.success('Career report generated!');
      onSuccess();
    } catch {
      toast.error('Failed to generate report');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <Input label="Current Role" {...register('currentRole', { required: true })} />
        <Input label="Target Role" {...register('targetRole', { required: true })} />
      </div>
      <Input label="Skills (comma-separated)" {...register('skills', { required: true })} />
      <div className="grid grid-cols-3 gap-4">
        <Input label="Experience (years)" type="number" {...register('experienceYears', { required: true, valueAsNumber: true })} />
        <Input label="Industry" {...register('industry')} />
        <Input label="City" {...register('city')} />
      </div>
      <Button type="submit" loading={loading} className="w-full">Generate Report</Button>
    </form>
  );
}

function CareerChat() {
  const [messages, setMessages] = useState<Array<{ role: string; content: string }>>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;
    const userMsg = input.trim();
    setInput('');
    setMessages((prev) => [...prev, { role: 'user', content: userMsg }]);
    setLoading(true);
    try {
      const res = await careerApi.chat(userMsg);
      setMessages((prev) => [...prev, { role: 'assistant', content: (res.response as string) || (res.message as string) || JSON.stringify(res) }]);
    } catch {
      toast.error('Failed to get response');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="flex flex-col h-[600px]">
      <div className="p-4 border-b border-gray-200 dark:border-gray-800">
        <h2 className="font-semibold text-gray-900 dark:text-white">AI Career Coach</h2>
        <p className="text-xs text-gray-500">Ask anything about your career</p>
      </div>
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        {messages.length === 0 && (
          <div className="text-center py-12 text-gray-400">
            <MessageSquare className="w-12 h-12 mx-auto mb-3 opacity-50" />
            <p>Start a conversation about your career</p>
          </div>
        )}
        {messages.map((msg, i) => (
          <div key={i} className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}>
            <div className={`max-w-[80%] p-3 rounded-xl text-sm ${
              msg.role === 'user'
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 dark:bg-gray-800 text-gray-900 dark:text-white'
            }`}>
              <p className="whitespace-pre-wrap">{msg.content}</p>
            </div>
          </div>
        ))}
        {loading && (
          <div className="flex justify-start">
            <div className="bg-gray-100 dark:bg-gray-800 p-3 rounded-xl">
              <div className="flex gap-1">
                <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" />
                <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.1s' }} />
                <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }} />
              </div>
            </div>
          </div>
        )}
      </div>
      <div className="p-4 border-t border-gray-200 dark:border-gray-800">
        <div className="flex gap-2">
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && sendMessage()}
            placeholder="Ask about career growth, skills, transitions..."
            className="flex-1 px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-white text-sm"
          />
          <Button onClick={sendMessage} disabled={!input.trim() || loading}>
            <Send className="w-4 h-4" />
          </Button>
        </div>
      </div>
    </Card>
  );
}

function InterviewPrep() {
  const [result, setResult] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm<{ role: string; level: string }>();

  const onSubmit = async (data: { role: string; level: string }) => {
    setLoading(true);
    try {
      const res = await careerApi.generateInterviewQuestions(data.role, data.level);
      setResult(res);
    } catch {
      toast.error('Failed to generate questions');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <Card className="p-6">
        <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Interview Questions</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input label="Target Role" placeholder="e.g. Frontend Developer" {...register('role', { required: true })} />
          <div className="space-y-1.5">
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Level</label>
            <select className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100" {...register('level')}>
              <option value="junior">Junior</option>
              <option value="mid">Mid-Level</option>
              <option value="senior">Senior</option>
              <option value="lead">Lead</option>
            </select>
          </div>
          <Button type="submit" className="w-full" loading={loading}>
            <GraduationCap className="w-4 h-4 mr-2" />
            Generate Questions
          </Button>
        </form>
      </Card>
      {result && (
        <Card className="lg:col-span-2 p-6">
          <h3 className="font-semibold text-gray-900 dark:text-white mb-4">Generated Questions</h3>
          <div className="prose prose-sm dark:prose-invert max-w-none whitespace-pre-wrap">
            {result}
          </div>
        </Card>
      )}
    </div>
  );
}

function SalaryNegotiation() {
  const [result, setResult] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm<{
    name: string; company: string; role: string; currentOffer: string; expectedSalary: string;
  }>();

  const onSubmit = async (data: { name: string; company: string; role: string; currentOffer: string; expectedSalary: string }) => {
    setLoading(true);
    try {
      const res = await careerApi.generateNegotiationLetter(data.name, data.company, data.role, data.currentOffer, data.expectedSalary);
      setResult(res);
    } catch {
      toast.error('Failed to generate letter');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <Card className="p-6">
        <h2 className="font-semibold text-gray-900 dark:text-white mb-4">Negotiation Letter</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input label="Your Name" {...register('name', { required: true })} />
          <Input label="Company" {...register('company', { required: true })} />
          <Input label="Role" {...register('role', { required: true })} />
          <Input label="Current Offer (₹)" {...register('currentOffer', { required: true })} />
          <Input label="Expected Salary (₹)" {...register('expectedSalary', { required: true })} />
          <Button type="submit" className="w-full" loading={loading}>
            <DollarSign className="w-4 h-4 mr-2" />
            Generate Letter
          </Button>
        </form>
      </Card>
      {result && (
        <Card className="lg:col-span-2 p-6">
          <h3 className="font-semibold text-gray-900 dark:text-white mb-4">Negotiation Letter</h3>
          <div className="prose prose-sm dark:prose-invert max-w-none whitespace-pre-wrap bg-gray-50 dark:bg-gray-800/50 p-4 rounded-lg">
            {result}
          </div>
        </Card>
      )}
    </div>
  );
}
