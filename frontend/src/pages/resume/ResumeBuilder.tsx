import { useState } from 'react';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { resumeApi } from '../../api/resume';
import type { ResumeRequest } from '../../types';

interface Props {
  onSuccess: () => void;
}

export function ResumeBuilder({ onSuccess }: Props) {
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, formState: { errors } } = useForm<ResumeRequest>({
    defaultValues: { templateId: 'modern' },
  });

  const onSubmit = async (data: ResumeRequest) => {
    setLoading(true);
    try {
      await resumeApi.create(data);
      toast.success('Resume created!');
      onSuccess();
    } catch {
      toast.error('Failed to create resume');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input
          label="Resume Title"
          placeholder="e.g. Software Engineer Resume"
          error={errors.title?.message}
          {...register('title', { required: 'Title is required' })}
        />
        <div className="space-y-1.5">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Template</label>
          <select
            className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100"
            {...register('templateId')}
          >
            <option value="modern">Modern</option>
            <option value="classic">Classic</option>
            <option value="minimal">Minimal</option>
            <option value="creative">Creative</option>
          </select>
        </div>
      </div>

      <Input label="Target Role" placeholder="e.g. Senior Software Engineer" {...register('targetRole')} />

      <div className="space-y-1.5">
        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Personal Info (JSON)</label>
        <textarea
          className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
          placeholder='{"name": "John", "email": "john@example.com", "phone": "..."}'
          {...register('personalInfo')}
        />
      </div>

      <div className="space-y-1.5">
        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Summary</label>
        <textarea
          className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
          placeholder="Brief professional summary..."
          {...register('summary')}
        />
      </div>

      <div className="space-y-1.5">
        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Experience</label>
        <textarea
          className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[100px]"
          placeholder="Work experience details..."
          {...register('experience')}
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-1.5">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Education</label>
          <textarea
            className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
            placeholder="Education details..."
            {...register('education')}
          />
        </div>
        <div className="space-y-1.5">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Skills</label>
          <textarea
            className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
            placeholder="Skills (comma-separated)..."
            {...register('skills')}
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-1.5">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Certifications</label>
          <textarea
            className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
            placeholder="Certifications..."
            {...register('certifications')}
          />
        </div>
        <div className="space-y-1.5">
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Projects</label>
          <textarea
            className="w-full px-4 py-2.5 rounded-lg border border-gray-300 bg-white text-gray-900 dark:bg-gray-900 dark:border-gray-700 dark:text-gray-100 min-h-[80px]"
            placeholder="Notable projects..."
            {...register('projects')}
          />
        </div>
      </div>

      <div className="flex justify-end pt-4">
        <Button type="submit" loading={loading} size="lg">
          Create Resume
        </Button>
      </div>
    </form>
  );
}
