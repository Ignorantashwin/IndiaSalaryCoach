import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { Download, Sparkles, Target, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { resumeApi } from '../../api/resume';

export function ResumeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const queryClient = useQueryClient();
  const [targetRole, setTargetRole] = useState('');
  const [jobDescription, setJobDescription] = useState('');

  const { data: resume, isLoading } = useQuery({
    queryKey: ['resume', id],
    queryFn: () => resumeApi.getById(Number(id)),
    enabled: !!id,
  });

  const scoreMutation = useMutation({
    mutationFn: () => resumeApi.score(Number(id), jobDescription, targetRole),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['resume', id] });
      toast.success(`ATS Score: ${(data as Record<string, unknown>).score || 'Calculated'}%`);
    },
    onError: () => toast.error('Failed to score resume'),
  });

  const optimizeMutation = useMutation({
    mutationFn: () => resumeApi.optimize(Number(id), targetRole),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['resume', id] });
      toast.success('Resume optimized!');
    },
    onError: () => toast.error('Failed to optimize resume'),
  });

  const aiOptimizeMutation = useMutation({
    mutationFn: () => resumeApi.aiOptimize(Number(id), targetRole),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['resume', id] });
      toast.success('AI optimization complete!');
    },
    onError: () => toast.error('AI optimization failed'),
  });

  const updateMutation = useMutation({
    mutationFn: (updates: Record<string, unknown>) => resumeApi.update(Number(id), updates),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['resume', id] });
      toast.success('Resume updated');
    },
    onError: () => toast.error('Failed to update'),
  });

  const handleDownload = async (optimized = false) => {
    try {
      const blob = await resumeApi.download(Number(id), optimized);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = optimized ? 'Optimized_resume.pdf' : 'resume.pdf';
      a.click();
      URL.revokeObjectURL(url);
    } catch {
      toast.error('Download failed');
    }
  };

  if (isLoading) {
    return <div className="space-y-4"><CardSkeleton /><CardSkeleton /></div>;
  }

  if (!resume) return null;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">{resume.title}</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">
            Template: {resume.templateId} | Updated: {new Date(resume.updatedAt).toLocaleDateString()}
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => handleDownload(false)}>
            <Download className="w-4 h-4 mr-2" />
            Download
          </Button>
          {resume.atsOptimized && (
            <Button variant="outline" onClick={() => handleDownload(true)}>
              <Download className="w-4 h-4 mr-2" />
              Optimized PDF
            </Button>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-4">
          <Card className="p-6 space-y-4">
            <h2 className="font-semibold text-gray-900 dark:text-white">Resume Content</h2>
            {resume.summary && (
              <div>
                <label className="text-sm font-medium text-gray-500">Summary</label>
                <p className="mt-1 text-sm text-gray-700 dark:text-gray-300">{resume.summary}</p>
              </div>
            )}
            {resume.experience && (
              <div>
                <label className="text-sm font-medium text-gray-500">Experience</label>
                <p className="mt-1 text-sm text-gray-700 dark:text-gray-300 whitespace-pre-wrap">{resume.experience}</p>
              </div>
            )}
            {resume.education && (
              <div>
                <label className="text-sm font-medium text-gray-500">Education</label>
                <p className="mt-1 text-sm text-gray-700 dark:text-gray-300">{resume.education}</p>
              </div>
            )}
            {resume.skills && (
              <div>
                <label className="text-sm font-medium text-gray-500">Skills</label>
                <p className="mt-1 text-sm text-gray-700 dark:text-gray-300">{resume.skills}</p>
              </div>
            )}
            <Button
              variant="outline"
              size="sm"
              onClick={() => updateMutation.mutate({ title: resume.title })}
              loading={updateMutation.isPending}
            >
              <Save className="w-3 h-3 mr-1" />
              Save
            </Button>
          </Card>

          {resume.optimizedsummary && (
            <Card className="p-6 border-green-200 dark:border-green-800">
              <div className="flex items-center gap-2 mb-3">
                <Sparkles className="w-5 h-5 text-green-600" />
                <h2 className="font-semibold text-green-700 dark:text-green-400">AI Optimized Content</h2>
              </div>
              <div className="space-y-3">
                <div>
                  <label className="text-sm font-medium text-gray-500">Optimized Summary</label>
                  <p className="mt-1 text-sm text-gray-700 dark:text-gray-300">{resume.optimizedsummary}</p>
                </div>
                {resume.optimizedskills && (
                  <div>
                    <label className="text-sm font-medium text-gray-500">Optimized Skills</label>
                    <p className="mt-1 text-sm text-gray-700 dark:text-gray-300">{resume.optimizedskills}</p>
                  </div>
                )}
              </div>
            </Card>
          )}
        </div>

        <div className="space-y-4">
          <Card className="p-6">
            <h3 className="font-semibold text-gray-900 dark:text-white mb-3">ATS Score</h3>
            {resume.latestAtsScore > 0 ? (
              <div className="text-center mb-4">
                <div className="text-4xl font-bold text-primary-600">{resume.latestAtsScore}%</div>
                <Badge variant={resume.latestAtsScore >= 70 ? 'success' : resume.latestAtsScore >= 50 ? 'warning' : 'error'}>
                  {resume.latestAtsScore >= 70 ? 'Excellent' : resume.latestAtsScore >= 50 ? 'Good' : 'Needs Work'}
                </Badge>
              </div>
            ) : (
              <p className="text-sm text-gray-500 mb-4">No score yet. Run ATS analysis below.</p>
            )}
            <div className="space-y-3">
              <Input
                placeholder="Target Role"
                value={targetRole}
                onChange={(e) => setTargetRole(e.target.value)}
              />
              <textarea
                className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px] text-sm"
                placeholder="Paste job description..."
                value={jobDescription}
                onChange={(e) => setJobDescription(e.target.value)}
              />
              <Button
                className="w-full"
                size="sm"
                onClick={() => scoreMutation.mutate()}
                loading={scoreMutation.isPending}
                disabled={!targetRole}
              >
                <Target className="w-3 h-3 mr-1" />
                Score Resume
              </Button>
            </div>
          </Card>

          <Card className="p-6">
            <h3 className="font-semibold text-gray-900 dark:text-white mb-3">AI Optimization</h3>
            <div className="space-y-3">
              <Input
                placeholder="Target Role"
                value={targetRole}
                onChange={(e) => setTargetRole(e.target.value)}
              />
              <Button
                className="w-full"
                size="sm"
                variant="secondary"
                onClick={() => optimizeMutation.mutate()}
                loading={optimizeMutation.isPending}
                disabled={!targetRole}
              >
                Optimize for ATS
              </Button>
              <Button
                className="w-full"
                size="sm"
                onClick={() => aiOptimizeMutation.mutate()}
                loading={aiOptimizeMutation.isPending}
                disabled={!targetRole}
              >
                <Sparkles className="w-3 h-3 mr-1" />
                AI Full Optimization
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </motion.div>
  );
}
