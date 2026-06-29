import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Plus, FileText, Trash2, Download, Star } from 'lucide-react';
import toast from 'react-hot-toast';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { Modal } from '../../components/ui/Modal';
import { resumeApi } from '../../api/resume';
import type { Resume } from '../../types';
import { ResumeBuilder } from './ResumeBuilder';

export function ResumeListPage() {
  const [showBuilder, setShowBuilder] = useState(false);
  const queryClient = useQueryClient();

  const { data: resumes, isLoading } = useQuery({
    queryKey: ['resumes'],
    queryFn: resumeApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: resumeApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['resumes'] });
      toast.success('Resume deleted');
    },
    onError: () => toast.error('Failed to delete resume'),
  });

  const handleDownload = async (id: number, optimized = false) => {
    try {
      const blob = await resumeApi.download(id, optimized);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = optimized ? 'Optimized_resume.pdf' : 'resume.pdf';
      a.click();
      URL.revokeObjectURL(url);
    } catch {
      toast.error('Failed to download resume');
    }
  };

  if (isLoading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {Array.from({ length: 6 }).map((_, i) => <CardSkeleton key={i} />)}
      </div>
    );
  }

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Resumes</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Build and optimize your resumes with AI</p>
        </div>
        <Button onClick={() => setShowBuilder(true)}>
          <Plus className="w-4 h-4 mr-2" />
          New Resume
        </Button>
      </div>

      {resumes && resumes.length === 0 ? (
        <Card className="p-12 text-center">
          <FileText className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 dark:text-white">No resumes yet</h3>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Create your first resume to get started</p>
          <Button onClick={() => setShowBuilder(true)} className="mt-4">
            <Plus className="w-4 h-4 mr-2" />
            Create Resume
          </Button>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {resumes?.map((resume: Resume, i: number) => (
            <motion.div
              key={resume.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.05 }}
            >
              <Card hover className="p-5">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary-50 dark:bg-primary-900/20 rounded-lg">
                      <FileText className="w-5 h-5 text-primary-600" />
                    </div>
                    <div>
                      <Link
                        to={`/resume/${resume.id}`}
                        className="font-medium text-gray-900 dark:text-white hover:text-primary-600 transition-colors"
                      >
                        {resume.title}
                      </Link>
                      <p className="text-xs text-gray-500 mt-0.5">
                        {new Date(resume.updatedAt).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                  {resume.atsOptimized && (
                    <Badge variant="success">Optimized</Badge>
                  )}
                </div>

                {resume.latestAtsScore > 0 && (
                  <div className="mt-3 flex items-center gap-2">
                    <Star className="w-4 h-4 text-amber-500" />
                    <span className="text-sm text-gray-600 dark:text-gray-400">
                      ATS Score: <span className="font-medium text-gray-900 dark:text-white">{resume.latestAtsScore}%</span>
                    </span>
                  </div>
                )}

                {resume.targetRole && (
                  <p className="mt-2 text-xs text-gray-500">Target: {resume.targetRole}</p>
                )}

                <div className="mt-4 flex items-center gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleDownload(resume.id)}
                  >
                    <Download className="w-3 h-3 mr-1" />
                    PDF
                  </Button>
                  {resume.atsOptimized && (
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => handleDownload(resume.id, true)}
                    >
                      <Download className="w-3 h-3 mr-1" />
                      Optimized
                    </Button>
                  )}
                  <Button
                    size="sm"
                    variant="ghost"
                    className="ml-auto text-red-500 hover:text-red-700"
                    onClick={() => deleteMutation.mutate(resume.id)}
                  >
                    <Trash2 className="w-3 h-3" />
                  </Button>
                </div>
              </Card>
            </motion.div>
          ))}
        </div>
      )}

      <Modal isOpen={showBuilder} onClose={() => setShowBuilder(false)} title="Create Resume" size="xl">
        <ResumeBuilder onSuccess={() => { setShowBuilder(false); queryClient.invalidateQueries({ queryKey: ['resumes'] }); }} />
      </Modal>
    </motion.div>
  );
}
