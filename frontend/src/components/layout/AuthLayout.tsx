import { Outlet } from 'react-router-dom';
import { Sparkles } from 'lucide-react';

export function AuthLayout() {
  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 p-12 flex-col justify-between relative overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 left-20 w-72 h-72 bg-white rounded-full blur-3xl" />
          <div className="absolute bottom-20 right-20 w-96 h-96 bg-primary-300 rounded-full blur-3xl" />
        </div>

        <div className="relative">
          <div className="flex items-center gap-3 mb-12">
            <div className="w-10 h-10 bg-white/20 backdrop-blur-sm rounded-xl flex items-center justify-center">
              <Sparkles className="w-5 h-5 text-white" />
            </div>
            <span className="text-xl font-bold text-white">IndiaSalaryCoach</span>
          </div>

          <h1 className="text-4xl font-bold text-white leading-tight">
            AI-Powered Career Intelligence for India
          </h1>
          <p className="mt-4 text-lg text-primary-100">
            Get salary insights, optimize your resume, compare offers, and accelerate your career with AI-driven guidance.
          </p>
        </div>

        <div className="relative">
          <div className="grid grid-cols-2 gap-4">
            {[
              { label: 'Salary Data Points', value: '50K+' },
              { label: 'Cities Covered', value: '100+' },
              { label: 'Industries', value: '25+' },
              { label: 'Career Reports', value: '10K+' },
            ].map((stat) => (
              <div key={stat.label} className="bg-white/10 backdrop-blur-sm rounded-xl p-4">
                <div className="text-2xl font-bold text-white">{stat.value}</div>
                <div className="text-sm text-primary-200">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="flex-1 flex items-center justify-center p-8 bg-white dark:bg-gray-950">
        <div className="w-full max-w-md">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
