export interface User {
  id: number;
  email: string;
  name: string;
  role: string;
  plan: string;
  avatarUrl?: string;
  currentTitle?: string;
  currentCity?: string;
  yearsExperience?: number;
  industry?: string;
  skills?: string;
  createdAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}

export interface SalarySearchRequest {
  jobTitle: string;
  experienceYears: number;
  skills?: string;
  city: string;
  industry: string;
}

export interface SalaryResultResponse {
  jobTitle: string;
  city: string;
  industry: string;
  experienceYears: number;
  minSalary: number;
  maxSalary: number;
  medianSalary: number;
  marketAverage: number;
  currency: string;
  dataPoints: number;
  dataSource: string;
  lastUpdated: string;
  topCities: CityInsight[];
  skillImpacts: SkillImpact[];
  industryComparisons: IndustryComparison[];
  percentileRanks: PercentileRank[];
}

export interface CityInsight {
  city: string;
  averageSalary: number;
  demandScore: number;
  costOfLivingIndex: number;
  salaryToLivingRatio: number;
}

export interface SkillImpact {
  skill: string;
  percentageIncrease: number;
  absoluteIncrease: number;
  demandLevel: string;
}

export interface IndustryComparison {
  industry: string;
  averageSalary: number;
  growthRate: number;
  demandTrend: string;
}

export interface PercentileRank {
  percentile: number;
  salary: number;
}

export interface SalarySearchHistory {
  id: number;
  jobTitle: string;
  city: string;
  industry: string;
  experienceYears: number;
  skills: string;
  minSalary: number;
  maxSalary: number;
  medianSalary: number;
  createdAt: string;
}

export interface Resume {
  id: number;
  title: string;
  templateId: string;
  personalInfo?: string;
  summary?: string;
  experience?: string;
  education?: string;
  skills?: string;
  certifications?: string;
  projects?: string;
  targetRole?: string;
  atsOptimized: boolean;
  createdAt: string;
  updatedAt: string;
  optimizedsummary?: string;
  optimizedskills?: string;
  latestAtsScore: number;
}

export interface ResumeRequest {
  title: string;
  templateId: string;
  personalInfo?: string;
  summary?: string;
  experience?: string;
  education?: string;
  skills?: string;
  certifications?: string;
  projects?: string;
  targetRole?: string;
}

export interface CareerReport {
  id: number;
  currentRole: string;
  targetRole: string;
  roadmap: string;
  skillGaps: string;
  timelineMonths: number;
  learningResources?: string;
  salaryProjection?: number;
  confidenceScore?: number;
  inputSkills?: string;
  experienceYears?: number;
  industry?: string;
  city?: string;
  createdAt: string;
}

export interface CareerReportRequest {
  currentRole: string;
  targetRole: string;
  skills: string;
  experienceYears: number;
  industry?: string;
  city?: string;
}

export interface OfferComparisonRequest {
  offers: JobOffer[];
}

export interface JobOffer {
  companyName: string;
  jobTitle: string;
  baseSalary: number;
  annualBonus?: number;
  equityValue?: number;
  benefits?: string;
  city?: string;
  industry?: string;
  remote?: boolean;
  growthPotential?: string;
}

export interface OfferAnalysis {
  id: number;
  offersJson: string;
  recommendation: string;
  topPickIndex: number;
  createdAt: string;
}

export interface Subscription {
  id: number;
  planId: string;
  planName: string;
  status: string;
  billingCycle: string;
  currentPeriodStart: string;
  currentPeriodEnd: string;
  cancelAtPeriodEnd: boolean;
  createdAt: string;
}

export interface Payment {
  id: number;
  amount: number;
  currency: string;
  status: string;
  planName: string;
  planId?: string;
  billingCycle?: string;
  razorpayOrderId?: string;
  razorpayPaymentId?: string;
  createdAt: string;
}

export interface CheckoutRequest {
  planId: string;
  billingCycle: string;
}

export interface Plan {
  id: string;
  name: string;
  price: number;
  annualPrice?: number;
  features: string[];
  popular?: boolean;
}
