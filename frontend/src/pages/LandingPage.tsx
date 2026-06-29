import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  TrendingUp, FileText, Briefcase, Scale, Sparkles, ArrowRight,
  Check, Star, Shield, Zap, Globe, BarChart3,
} from 'lucide-react';
import { Button } from '../components/ui/Button';

export function LandingPage() {
  const features = [
    {
      icon: TrendingUp,
      title: 'Salary Intelligence',
      desc: 'Get real-time salary data across 100+ Indian cities with AI-driven market analysis.',
    },
    {
      icon: FileText,
      title: 'Resume Builder',
      desc: 'Build ATS-optimized resumes with AI scoring and professional templates.',
    },
    {
      icon: Briefcase,
      title: 'Career Coach',
      desc: 'AI-powered career guidance, interview prep, and personalized growth roadmaps.',
    },
    {
      icon: Scale,
      title: 'Offer Analysis',
      desc: 'Compare multiple job offers with AI to make the best career decision.',
    },
    {
      icon: Sparkles,
      title: 'AI Optimization',
      desc: 'Leverage Gemini AI to optimize your resume and salary negotiation strategy.',
    },
    {
      icon: BarChart3,
      title: 'Market Trends',
      desc: 'Stay updated with industry salary trends, skill demands, and growth insights.',
    },
  ];

  return (
    <div className="min-h-screen bg-white dark:bg-gray-950">
      {/* Navigation */}
      <nav className="fixed top-0 w-full bg-white/80 dark:bg-gray-950/80 backdrop-blur-md border-b border-gray-100 dark:border-gray-900 z-50">
        <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-700 rounded-lg flex items-center justify-center">
              <Sparkles className="w-4 h-4 text-white" />
            </div>
            <span className="font-bold text-gray-900 dark:text-white">IndiaSalaryCoach</span>
          </div>
          <div className="flex items-center gap-3">
            <Link to="/login">
              <Button variant="ghost" size="sm">Sign in</Button>
            </Link>
            <Link to="/register">
              <Button size="sm">Get Started</Button>
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero */}
      <section className="pt-32 pb-20 px-6">
        <div className="max-w-4xl mx-auto text-center">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            <div className="inline-flex items-center gap-2 px-4 py-1.5 bg-primary-50 dark:bg-primary-900/20 rounded-full text-sm text-primary-700 dark:text-primary-400 font-medium mb-6">
              <Zap className="w-3.5 h-3.5" />
              AI-Powered Career Intelligence
            </div>
            <h1 className="text-5xl md:text-6xl font-bold text-gray-900 dark:text-white leading-tight">
              Your Career Growth
              <br />
              <span className="bg-gradient-to-r from-primary-600 to-primary-400 bg-clip-text text-transparent">
                Starts Here
              </span>
            </h1>
            <p className="mt-6 text-xl text-gray-600 dark:text-gray-400 max-w-2xl mx-auto leading-relaxed">
              Get AI-driven salary insights, build optimized resumes, compare offers, and accelerate your career with personalized guidance tailored for the Indian job market.
            </p>
            <div className="mt-8 flex items-center justify-center gap-4">
              <Link to="/register">
                <Button size="lg">
                  Start Free <ArrowRight className="w-4 h-4 ml-2" />
                </Button>
              </Link>
              <Link to="/login">
                <Button variant="outline" size="lg">
                  Sign In
                </Button>
              </Link>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 40 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
            className="mt-16 grid grid-cols-2 md:grid-cols-4 gap-6"
          >
            {[
              { value: '50K+', label: 'Salary Data Points' },
              { value: '100+', label: 'Cities' },
              { value: '25+', label: 'Industries' },
              { value: '10K+', label: 'Users' },
            ].map((stat) => (
              <div key={stat.label} className="text-center">
                <div className="text-2xl md:text-3xl font-bold text-gray-900 dark:text-white">{stat.value}</div>
                <div className="text-sm text-gray-500 mt-1">{stat.label}</div>
              </div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Features */}
      <section className="py-20 px-6 bg-gray-50 dark:bg-gray-900/50">
        <div className="max-w-6xl mx-auto">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white">Everything you need for career growth</h2>
            <p className="mt-3 text-gray-600 dark:text-gray-400">Comprehensive tools powered by AI</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((feature, i) => (
              <motion.div
                key={feature.title}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ delay: i * 0.1 }}
                viewport={{ once: true }}
                className="bg-white dark:bg-gray-900 rounded-xl p-6 border border-gray-200 dark:border-gray-800 hover:shadow-lg transition-shadow"
              >
                <div className="w-10 h-10 bg-primary-50 dark:bg-primary-900/20 rounded-lg flex items-center justify-center mb-4">
                  <feature.icon className="w-5 h-5 text-primary-600" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">{feature.title}</h3>
                <p className="mt-2 text-sm text-gray-600 dark:text-gray-400 leading-relaxed">{feature.desc}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Social Proof */}
      <section className="py-20 px-6">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl font-bold text-gray-900 dark:text-white">Trusted by professionals</h2>
          <div className="mt-8 flex items-center justify-center gap-1">
            {Array.from({ length: 5 }).map((_, i) => (
              <Star key={i} className="w-5 h-5 text-yellow-400 fill-yellow-400" />
            ))}
          </div>
          <p className="mt-3 text-gray-600 dark:text-gray-400">
            Join thousands of professionals making smarter career decisions
          </p>
          <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-8">
            {[
              { icon: Globe, label: 'Pan-India Coverage', desc: 'Data from 100+ cities' },
              { icon: Shield, label: 'Data Privacy', desc: 'Your data stays secure' },
              { icon: Zap, label: 'Real-time Insights', desc: 'Updated market data' },
            ].map((item) => (
              <div key={item.label} className="flex flex-col items-center">
                <div className="w-12 h-12 bg-gray-100 dark:bg-gray-800 rounded-xl flex items-center justify-center">
                  <item.icon className="w-6 h-6 text-gray-600 dark:text-gray-400" />
                </div>
                <h4 className="mt-3 font-medium text-gray-900 dark:text-white">{item.label}</h4>
                <p className="text-sm text-gray-500">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-20 px-6">
        <div className="max-w-4xl mx-auto bg-gradient-to-br from-primary-600 to-primary-800 rounded-2xl p-12 text-center">
          <h2 className="text-3xl font-bold text-white">Ready to accelerate your career?</h2>
          <p className="mt-3 text-primary-100 text-lg">Start for free. No credit card required.</p>
          <Link to="/register">
            <Button size="lg" variant="secondary" className="mt-6">
              Get Started Free <ArrowRight className="w-4 h-4 ml-2" />
            </Button>
          </Link>
          <div className="mt-6 flex items-center justify-center gap-6 text-primary-200 text-sm">
            <span className="flex items-center gap-1"><Check className="w-4 h-4" /> Free tier available</span>
            <span className="flex items-center gap-1"><Check className="w-4 h-4" /> No credit card</span>
            <span className="flex items-center gap-1"><Check className="w-4 h-4" /> Cancel anytime</span>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="py-8 px-6 border-t border-gray-200 dark:border-gray-800">
        <div className="max-w-6xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-6 h-6 bg-gradient-to-br from-primary-500 to-primary-700 rounded-md flex items-center justify-center">
              <Sparkles className="w-3 h-3 text-white" />
            </div>
            <span className="text-sm font-medium text-gray-600 dark:text-gray-400">IndiaSalaryCoach</span>
          </div>
          <p className="text-sm text-gray-500">© 2024 IndiaSalaryCoach. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}
