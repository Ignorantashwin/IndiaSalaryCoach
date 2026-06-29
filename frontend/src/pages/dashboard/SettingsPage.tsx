import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import { Save } from 'lucide-react';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { useAuthStore } from '../../stores/authStore';
import { useThemeStore } from '../../stores/themeStore';
import { userApi } from '../../api/user';

interface ProfileForm {
  name: string;
  currentTitle: string;
  currentCity: string;
  yearsExperience: number;
  industry: string;
  skills: string;
}

export function SettingsPage() {
  const [loading, setLoading] = useState(false);
  const { user, setUser } = useAuthStore();
  const { theme, setTheme } = useThemeStore();

  const { register, handleSubmit } = useForm<ProfileForm>({
    defaultValues: {
      name: user?.name || '',
      currentTitle: user?.currentTitle || '',
      currentCity: user?.currentCity || '',
      yearsExperience: user?.yearsExperience || 0,
      industry: user?.industry || '',
      skills: user?.skills || '',
    },
  });

  const onSubmit = async (data: ProfileForm) => {
    setLoading(true);
    try {
      const updated = await userApi.updateMe(data as unknown as Record<string, unknown>);
      setUser(updated);
      toast.success('Profile updated');
    } catch {
      toast.error('Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="max-w-3xl space-y-6"
    >
      <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Settings</h1>

      <Card className="p-6">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Profile</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input label="Name" {...register('name')} />
            <Input label="Current Title" {...register('currentTitle')} />
            <Input label="City" {...register('currentCity')} />
            <Input label="Years of Experience" type="number" {...register('yearsExperience', { valueAsNumber: true })} />
            <Input label="Industry" {...register('industry')} />
            <Input label="Skills (comma-separated)" {...register('skills')} />
          </div>
          <Button type="submit" loading={loading}>
            <Save className="w-4 h-4 mr-2" />
            Save Changes
          </Button>
        </form>
      </Card>

      <Card className="p-6">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Appearance</h2>
        <div className="flex gap-3">
          <button
            onClick={() => setTheme('light')}
            className={`flex-1 p-4 rounded-lg border-2 transition-colors ${
              theme === 'light'
                ? 'border-primary-500 bg-primary-50 dark:bg-primary-900/20'
                : 'border-gray-200 dark:border-gray-700'
            }`}
          >
            <div className="w-full h-20 bg-white border border-gray-200 rounded-lg mb-2" />
            <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Light</span>
          </button>
          <button
            onClick={() => setTheme('dark')}
            className={`flex-1 p-4 rounded-lg border-2 transition-colors ${
              theme === 'dark'
                ? 'border-primary-500 bg-primary-50 dark:bg-primary-900/20'
                : 'border-gray-200 dark:border-gray-700'
            }`}
          >
            <div className="w-full h-20 bg-gray-900 border border-gray-700 rounded-lg mb-2" />
            <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Dark</span>
          </button>
        </div>
      </Card>

      <Card className="p-6">
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Account</h2>
        <div className="space-y-3">
          <div className="flex items-center justify-between py-2">
            <span className="text-sm text-gray-600 dark:text-gray-400">Email</span>
            <span className="text-sm font-medium text-gray-900 dark:text-white">{user?.email}</span>
          </div>
          <div className="flex items-center justify-between py-2">
            <span className="text-sm text-gray-600 dark:text-gray-400">Plan</span>
            <span className="text-sm font-medium text-gray-900 dark:text-white capitalize">{user?.plan}</span>
          </div>
          <div className="flex items-center justify-between py-2">
            <span className="text-sm text-gray-600 dark:text-gray-400">Member since</span>
            <span className="text-sm font-medium text-gray-900 dark:text-white">
              {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : '-'}
            </span>
          </div>
        </div>
      </Card>
    </motion.div>
  );
}
