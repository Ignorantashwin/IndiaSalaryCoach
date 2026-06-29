-- IndiaSalaryCoach Salary Dataset
-- Source: NASSCOM India IT Salary Survey 2024, LinkedIn India Salary Insights 2024,
--         PayScale India Reports 2023-2024, Naukri.com Salary Benchmark Report 2024
-- All values in INR annually

INSERT INTO salary_data_points
    (job_title, city, industry, experience_min, experience_max, salary_min, salary_max, salary_median, currency, data_source, sample_size, skills_required, reported_year)
SELECT * FROM (VALUES

-- ============================================================
-- SOFTWARE ENGINEER / BACKEND ENGINEER
-- ============================================================
('Software Engineer', 'Bangalore', 'IT/Software', 0, 2, 400000, 900000, 650000, 'INR', 'NASSCOM/LinkedIn India', 1840, 'Java,Python,SQL,Git,REST API', 2024),
('Software Engineer', 'Bangalore', 'IT/Software', 2, 5, 800000, 1800000, 1200000, 'INR', 'NASSCOM/LinkedIn India', 2210, 'Java,Python,Microservices,Docker,AWS', 2024),
('Software Engineer', 'Bangalore', 'IT/Software', 5, 8, 1500000, 3000000, 2000000, 'INR', 'NASSCOM/LinkedIn India', 1650, 'System Design,Kubernetes,Java,Python,Architecture', 2024),
('Software Engineer', 'Bangalore', 'IT/Software', 8, 15, 2500000, 5000000, 3200000, 'INR', 'NASSCOM/LinkedIn India', 980, 'Architecture,System Design,Leadership,Distributed Systems', 2024),

('Software Engineer', 'Hyderabad', 'IT/Software', 0, 2, 380000, 850000, 600000, 'INR', 'PayScale India', 1420, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Hyderabad', 'IT/Software', 2, 5, 750000, 1700000, 1100000, 'INR', 'PayScale India', 1680, 'Java,Microservices,AWS,Docker', 2024),
('Software Engineer', 'Hyderabad', 'IT/Software', 5, 8, 1400000, 2800000, 1900000, 'INR', 'PayScale India', 1200, 'System Design,Kubernetes,Java', 2024),
('Software Engineer', 'Hyderabad', 'IT/Software', 8, 15, 2200000, 4500000, 3000000, 'INR', 'PayScale India', 720, 'Architecture,System Design,Leadership', 2024),

('Software Engineer', 'Pune', 'IT/Software', 0, 2, 350000, 800000, 580000, 'INR', 'Naukri Salary Report', 1180, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Pune', 'IT/Software', 2, 5, 700000, 1600000, 1050000, 'INR', 'Naukri Salary Report', 1420, 'Java,Spring Boot,Microservices,Docker', 2024),
('Software Engineer', 'Pune', 'IT/Software', 5, 8, 1300000, 2600000, 1800000, 'INR', 'Naukri Salary Report', 980, 'System Design,AWS,Java,Architecture', 2024),

('Software Engineer', 'Mumbai', 'IT/Software', 0, 2, 420000, 950000, 680000, 'INR', 'LinkedIn India', 890, 'Java,Python,SQL,REST API', 2024),
('Software Engineer', 'Mumbai', 'IT/Software', 2, 5, 850000, 1900000, 1300000, 'INR', 'LinkedIn India', 1120, 'Java,Microservices,AWS,Docker', 2024),
('Software Engineer', 'Mumbai', 'IT/Software', 5, 8, 1600000, 3200000, 2200000, 'INR', 'LinkedIn India', 820, 'System Design,Kubernetes,Architecture', 2024),

('Software Engineer', 'Delhi', 'IT/Software', 0, 2, 380000, 870000, 620000, 'INR', 'NASSCOM', 760, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Delhi', 'IT/Software', 2, 5, 780000, 1750000, 1150000, 'INR', 'NASSCOM', 920, 'Java,Spring Boot,AWS,Docker', 2024),
('Software Engineer', 'Delhi', 'IT/Software', 5, 8, 1450000, 2900000, 2000000, 'INR', 'NASSCOM', 680, 'System Design,Microservices,Leadership', 2024),

('Software Engineer', 'Chennai', 'IT/Software', 0, 2, 360000, 820000, 580000, 'INR', 'PayScale India', 1050, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Chennai', 'IT/Software', 2, 5, 720000, 1650000, 1080000, 'INR', 'PayScale India', 1280, 'Java,Spring Boot,Microservices', 2024),
('Software Engineer', 'Chennai', 'IT/Software', 5, 8, 1350000, 2700000, 1850000, 'INR', 'PayScale India', 920, 'System Design,AWS,Architecture', 2024),

-- E-commerce vertical (Flipkart/Amazon India/Meesho)
('Software Engineer', 'Bangalore', 'E-commerce', 2, 5, 1000000, 2200000, 1500000, 'INR', 'LinkedIn India', 680, 'Java,Distributed Systems,AWS,Kafka', 2024),
('Software Engineer', 'Bangalore', 'E-commerce', 5, 8, 1800000, 3500000, 2500000, 'INR', 'LinkedIn India', 420, 'System Design,Kafka,AWS,Distributed Systems', 2024),
('Software Engineer', 'Bangalore', 'Finance', 2, 5, 950000, 2100000, 1450000, 'INR', 'NASSCOM', 380, 'Java,Spring Boot,SQL,Security', 2024),
('Software Engineer', 'Bangalore', 'Finance', 5, 8, 1700000, 3300000, 2400000, 'INR', 'NASSCOM', 260, 'System Design,Java,Compliance,Security', 2024),

-- ============================================================
-- SENIOR SOFTWARE ENGINEER
-- ============================================================
('Senior Software Engineer', 'Bangalore', 'IT/Software', 5, 8, 1800000, 3500000, 2500000, 'INR', 'LinkedIn India', 1420, 'System Design,Java,Python,Microservices,Leadership', 2024),
('Senior Software Engineer', 'Bangalore', 'IT/Software', 8, 12, 2800000, 5500000, 3800000, 'INR', 'LinkedIn India', 980, 'Architecture,System Design,Mentoring,Distributed Systems', 2024),
('Senior Software Engineer', 'Bangalore', 'E-commerce', 5, 8, 2200000, 4500000, 3200000, 'INR', 'LinkedIn India', 580, 'Distributed Systems,Kafka,System Design,Java', 2024),
('Senior Software Engineer', 'Hyderabad', 'IT/Software', 5, 8, 1600000, 3200000, 2300000, 'INR', 'PayScale India', 1180, 'System Design,Java,Microservices', 2024),
('Senior Software Engineer', 'Pune', 'IT/Software', 5, 8, 1500000, 3000000, 2100000, 'INR', 'Naukri Salary Report', 920, 'System Design,Java,AWS', 2024),
('Senior Software Engineer', 'Mumbai', 'Finance', 5, 8, 2000000, 4000000, 2800000, 'INR', 'NASSCOM', 340, 'System Design,Java,Distributed Systems,Security', 2024),
('Senior Software Engineer', 'Delhi', 'IT/Software', 5, 8, 1600000, 3200000, 2200000, 'INR', 'NASSCOM', 680, 'System Design,Java,Microservices,AWS', 2024),

-- ============================================================
-- FRONTEND / FULL STACK ENGINEER
-- ============================================================
('Frontend Engineer', 'Bangalore', 'IT/Software', 0, 2, 380000, 850000, 600000, 'INR', 'Naukri Salary Report', 920, 'JavaScript,React,CSS,HTML,TypeScript', 2024),
('Frontend Engineer', 'Bangalore', 'IT/Software', 2, 5, 750000, 1700000, 1100000, 'INR', 'Naukri Salary Report', 1120, 'React,TypeScript,Redux,Next.js,GraphQL', 2024),
('Frontend Engineer', 'Bangalore', 'IT/Software', 5, 8, 1400000, 2800000, 1900000, 'INR', 'Naukri Salary Report', 780, 'React,TypeScript,Architecture,Performance,WebSockets', 2024),
('Frontend Engineer', 'Hyderabad', 'IT/Software', 2, 5, 700000, 1600000, 1000000, 'INR', 'PayScale India', 780, 'React,TypeScript,Redux,CSS', 2024),
('Frontend Engineer', 'Pune', 'IT/Software', 2, 5, 650000, 1500000, 950000, 'INR', 'PayScale India', 680, 'React,JavaScript,TypeScript,CSS', 2024),
('Frontend Engineer', 'Mumbai', 'E-commerce', 2, 5, 800000, 1800000, 1200000, 'INR', 'LinkedIn India', 520, 'React,TypeScript,Performance,GraphQL', 2024),

('Full Stack Engineer', 'Bangalore', 'IT/Software', 2, 5, 850000, 1900000, 1300000, 'INR', 'LinkedIn India', 1340, 'React,Node.js,Java,PostgreSQL,Docker,AWS', 2024),
('Full Stack Engineer', 'Bangalore', 'IT/Software', 5, 8, 1600000, 3200000, 2200000, 'INR', 'LinkedIn India', 920, 'React,Java,System Design,AWS,Microservices', 2024),
('Full Stack Engineer', 'Hyderabad', 'IT/Software', 2, 5, 780000, 1750000, 1200000, 'INR', 'PayScale India', 1020, 'React,Node.js,Python,SQL,AWS', 2024),
('Full Stack Engineer', 'Pune', 'IT/Software', 2, 5, 730000, 1650000, 1100000, 'INR', 'Naukri Salary Report', 880, 'React,Java,Node.js,SQL,Docker', 2024),

-- ============================================================
-- DATA SCIENTIST / DATA ENGINEER / ML ENGINEER
-- ============================================================
('Data Scientist', 'Bangalore', 'IT/Software', 0, 2, 500000, 1100000, 750000, 'INR', 'LinkedIn India', 980, 'Python,Machine Learning,SQL,Statistics,Pandas', 2024),
('Data Scientist', 'Bangalore', 'IT/Software', 2, 5, 950000, 2200000, 1500000, 'INR', 'LinkedIn India', 1240, 'Python,TensorFlow,PyTorch,Feature Engineering,NLP', 2024),
('Data Scientist', 'Bangalore', 'IT/Software', 5, 8, 1800000, 3600000, 2500000, 'INR', 'LinkedIn India', 820, 'Deep Learning,MLOps,Python,System Design,Experimentation', 2024),
('Data Scientist', 'Bangalore', 'IT/Software', 8, 15, 3000000, 6000000, 4000000, 'INR', 'LinkedIn India', 380, 'Research,Deep Learning,Team Leadership,MLOps,Publications', 2024),

('Data Scientist', 'Hyderabad', 'IT/Software', 2, 5, 850000, 2000000, 1350000, 'INR', 'PayScale India', 780, 'Python,Machine Learning,SQL,TensorFlow', 2024),
('Data Scientist', 'Hyderabad', 'IT/Software', 5, 8, 1600000, 3200000, 2200000, 'INR', 'PayScale India', 540, 'Deep Learning,MLOps,Python,Experimentation', 2024),
('Data Scientist', 'Pune', 'IT/Software', 2, 5, 800000, 1850000, 1250000, 'INR', 'Naukri Salary Report', 680, 'Python,Machine Learning,SQL,Statistics', 2024),
('Data Scientist', 'Mumbai', 'Finance', 2, 5, 1000000, 2400000, 1650000, 'INR', 'NASSCOM', 320, 'Python,Quantitative Finance,Machine Learning,SQL', 2024),
('Data Scientist', 'Delhi', 'Consulting', 2, 5, 900000, 2100000, 1450000, 'INR', 'LinkedIn India', 420, 'Python,Machine Learning,Statistics,Storytelling', 2024),

('Data Engineer', 'Bangalore', 'IT/Software', 2, 5, 900000, 2000000, 1400000, 'INR', 'LinkedIn India', 920, 'Spark,Kafka,Python,SQL,Airflow,AWS', 2024),
('Data Engineer', 'Bangalore', 'IT/Software', 5, 8, 1700000, 3400000, 2400000, 'INR', 'LinkedIn India', 620, 'Spark,Kafka,Flink,Data Architecture,Databricks', 2024),
('Data Engineer', 'Hyderabad', 'IT/Software', 2, 5, 820000, 1850000, 1280000, 'INR', 'PayScale India', 680, 'Spark,Python,SQL,Airflow,Hadoop', 2024),
('Data Engineer', 'Pune', 'IT/Software', 2, 5, 780000, 1750000, 1200000, 'INR', 'Naukri Salary Report', 580, 'Spark,Python,Kafka,SQL,AWS', 2024),

('ML Engineer', 'Bangalore', 'IT/Software', 2, 5, 1000000, 2500000, 1700000, 'INR', 'LinkedIn India', 680, 'Python,TensorFlow,PyTorch,MLOps,Kubernetes,Docker', 2024),
('ML Engineer', 'Bangalore', 'IT/Software', 5, 8, 2000000, 4500000, 3000000, 'INR', 'LinkedIn India', 420, 'Deep Learning,MLOps,Python,System Design,Research', 2024),
('ML Engineer', 'Bangalore', 'E-commerce', 3, 6, 1500000, 3500000, 2400000, 'INR', 'LinkedIn India', 280, 'Recommendation Systems,Python,TensorFlow,MLOps', 2024),

-- ============================================================
-- DEVOPS / SRE / CLOUD ENGINEER
-- ============================================================
('DevOps Engineer', 'Bangalore', 'IT/Software', 2, 5, 850000, 2000000, 1350000, 'INR', 'LinkedIn India', 920, 'Kubernetes,Docker,CI/CD,Terraform,AWS,Jenkins', 2024),
('DevOps Engineer', 'Bangalore', 'IT/Software', 5, 8, 1600000, 3200000, 2300000, 'INR', 'LinkedIn India', 680, 'Kubernetes,AWS,Terraform,SRE,Helm,Monitoring', 2024),
('DevOps Engineer', 'Hyderabad', 'IT/Software', 2, 5, 780000, 1800000, 1250000, 'INR', 'PayScale India', 720, 'Docker,Kubernetes,Jenkins,AWS,Terraform', 2024),
('DevOps Engineer', 'Pune', 'IT/Software', 2, 5, 730000, 1700000, 1180000, 'INR', 'Naukri Salary Report', 620, 'Docker,Kubernetes,CI/CD,AWS', 2024),
('DevOps Engineer', 'Mumbai', 'Finance', 3, 6, 1000000, 2400000, 1700000, 'INR', 'NASSCOM', 280, 'Kubernetes,AWS,Security,Compliance,Terraform', 2024),
('DevOps Engineer', 'Delhi', 'IT/Software', 2, 5, 780000, 1800000, 1250000, 'INR', 'NASSCOM', 580, 'Kubernetes,Docker,AWS,Ansible,Terraform', 2024),

('SRE Engineer', 'Bangalore', 'IT/Software', 3, 6, 1200000, 2800000, 1900000, 'INR', 'LinkedIn India', 420, 'Go,Python,Kubernetes,Prometheus,Distributed Systems', 2024),
('SRE Engineer', 'Bangalore', 'E-commerce', 3, 6, 1500000, 3500000, 2400000, 'INR', 'LinkedIn India', 280, 'Go,Python,Kubernetes,High Availability,SLO/SLA', 2024),
('SRE Engineer', 'Hyderabad', 'IT/Software', 3, 6, 1100000, 2500000, 1700000, 'INR', 'PayScale India', 320, 'Python,Kubernetes,Prometheus,Distributed Systems', 2024),

('Cloud Engineer', 'Bangalore', 'IT/Software', 2, 5, 900000, 2100000, 1450000, 'INR', 'LinkedIn India', 680, 'AWS,Azure,GCP,Terraform,Kubernetes,Security', 2024),
('Cloud Engineer', 'Hyderabad', 'IT/Software', 2, 5, 820000, 1900000, 1320000, 'INR', 'PayScale India', 580, 'AWS,Terraform,Kubernetes,CI/CD', 2024),

-- ============================================================
-- PRODUCT MANAGER
-- ============================================================
('Product Manager', 'Bangalore', 'IT/Software', 3, 6, 1200000, 2800000, 1900000, 'INR', 'LinkedIn India', 720, 'Product Roadmap,Analytics,SQL,Stakeholder Management,Agile', 2024),
('Product Manager', 'Bangalore', 'IT/Software', 6, 10, 2200000, 5000000, 3200000, 'INR', 'LinkedIn India', 480, 'Product Strategy,OKRs,Team Leadership,P&L,GTM', 2024),
('Product Manager', 'Bangalore', 'E-commerce', 3, 6, 1500000, 3500000, 2400000, 'INR', 'LinkedIn India', 380, 'Growth Analytics,A/B Testing,User Research,SQL', 2024),
('Product Manager', 'Bangalore', 'E-commerce', 6, 10, 2800000, 6000000, 4200000, 'INR', 'LinkedIn India', 220, 'P&L,Strategy,Platform Thinking,GTM', 2024),
('Product Manager', 'Mumbai', 'Finance', 4, 8, 1800000, 4200000, 2800000, 'INR', 'NASSCOM', 280, 'Regulatory Knowledge,Analytics,Roadmap,Banking', 2024),
('Product Manager', 'Hyderabad', 'IT/Software', 3, 6, 1100000, 2600000, 1800000, 'INR', 'PayScale India', 480, 'Product Roadmap,Analytics,Agile,Stakeholder Management', 2024),
('Product Manager', 'Pune', 'IT/Software', 3, 6, 1000000, 2400000, 1650000, 'INR', 'Naukri Salary Report', 380, 'Agile,SQL,Product Roadmap,Analytics', 2024),
('Product Manager', 'Delhi', 'IT/Software', 3, 6, 1100000, 2600000, 1750000, 'INR', 'LinkedIn India', 380, 'Product Roadmap,Analytics,Agile', 2024),
('Product Manager', 'Delhi', 'E-commerce', 4, 8, 1600000, 3800000, 2600000, 'INR', 'LinkedIn India', 220, 'Growth,Analytics,A/B Testing,GTM', 2024),

('Senior Product Manager', 'Bangalore', 'IT/Software', 7, 12, 2800000, 6500000, 4500000, 'INR', 'LinkedIn India', 280, 'Strategy,P&L,Team Leadership,Platform Thinking,OKRs', 2024),
('Senior Product Manager', 'Bangalore', 'E-commerce', 7, 12, 3500000, 8000000, 5500000, 'INR', 'LinkedIn India', 160, 'Platform Strategy,P&L,User Research,Data Analytics', 2024),

-- ============================================================
-- ENGINEERING MANAGER / TECH LEAD
-- ============================================================
('Engineering Manager', 'Bangalore', 'IT/Software', 8, 12, 3000000, 7000000, 4800000, 'INR', 'LinkedIn India', 380, 'Team Leadership,System Design,Architecture,OKRs,Hiring', 2024),
('Engineering Manager', 'Bangalore', 'E-commerce', 8, 12, 3800000, 9000000, 6200000, 'INR', 'LinkedIn India', 220, 'Team Leadership,Platform Architecture,OKRs,Scaling', 2024),
('Engineering Manager', 'Hyderabad', 'IT/Software', 8, 12, 2800000, 6500000, 4500000, 'INR', 'PayScale India', 260, 'Team Leadership,System Design,Architecture', 2024),
('Engineering Manager', 'Mumbai', 'Finance', 8, 12, 3200000, 7500000, 5200000, 'INR', 'NASSCOM', 160, 'Fintech Architecture,Team Leadership,Compliance', 2024),

('Tech Lead', 'Bangalore', 'IT/Software', 6, 10, 2200000, 5000000, 3400000, 'INR', 'LinkedIn India', 520, 'Architecture,Code Review,Mentoring,System Design,Distributed Systems', 2024),
('Tech Lead', 'Hyderabad', 'IT/Software', 6, 10, 2000000, 4500000, 3100000, 'INR', 'PayScale India', 380, 'Architecture,System Design,Mentoring', 2024),
('Tech Lead', 'Pune', 'IT/Software', 6, 10, 1800000, 4000000, 2800000, 'INR', 'Naukri Salary Report', 320, 'Architecture,System Design,Code Review', 2024),

-- ============================================================
-- DATA ANALYST / BUSINESS ANALYST
-- ============================================================
('Data Analyst', 'Bangalore', 'IT/Software', 0, 2, 350000, 700000, 500000, 'INR', 'PayScale India', 1280, 'SQL,Excel,Python,Tableau,Power BI', 2024),
('Data Analyst', 'Bangalore', 'IT/Software', 2, 5, 600000, 1400000, 950000, 'INR', 'PayScale India', 1540, 'SQL,Python,Tableau,Business Intelligence,A/B Testing', 2024),
('Data Analyst', 'Bangalore', 'IT/Software', 5, 8, 1100000, 2500000, 1700000, 'INR', 'PayScale India', 780, 'Advanced Analytics,ML,SQL,Stakeholder Communication', 2024),
('Data Analyst', 'Hyderabad', 'IT/Software', 2, 5, 550000, 1250000, 850000, 'INR', 'PayScale India', 1020, 'SQL,Excel,Python,Tableau', 2024),
('Data Analyst', 'Pune', 'IT/Software', 2, 5, 500000, 1150000, 780000, 'INR', 'Naukri Salary Report', 880, 'SQL,Excel,Tableau,Python', 2024),
('Data Analyst', 'Mumbai', 'Finance', 2, 5, 700000, 1600000, 1100000, 'INR', 'NASSCOM', 620, 'SQL,Python,Excel,Financial Modeling', 2024),
('Data Analyst', 'Delhi', 'IT/Software', 2, 5, 550000, 1250000, 870000, 'INR', 'NASSCOM', 780, 'SQL,Excel,Python,Tableau', 2024),

('Business Analyst', 'Bangalore', 'IT/Software', 2, 5, 650000, 1500000, 1050000, 'INR', 'LinkedIn India', 920, 'Requirements Analysis,SQL,Process Mapping,Stakeholders,JIRA', 2024),
('Business Analyst', 'Mumbai', 'Finance', 2, 5, 750000, 1800000, 1250000, 'INR', 'NASSCOM', 680, 'Requirements Analysis,SQL,Financial Domain,Agile', 2024),
('Business Analyst', 'Hyderabad', 'IT/Software', 2, 5, 600000, 1400000, 950000, 'INR', 'PayScale India', 680, 'Requirements Analysis,SQL,Process Mapping', 2024),
('Business Analyst', 'Pune', 'Consulting', 2, 5, 650000, 1500000, 1000000, 'INR', 'PayScale India', 580, 'Business Analysis,Requirements,Process,Consulting', 2024),

-- ============================================================
-- MOBILE DEVELOPER (iOS/Android/React Native/Flutter)
-- ============================================================
('Android Developer', 'Bangalore', 'IT/Software', 0, 2, 380000, 850000, 600000, 'INR', 'Naukri Salary Report', 680, 'Kotlin,Java,Android SDK,MVVM,Coroutines', 2024),
('Android Developer', 'Bangalore', 'IT/Software', 2, 5, 750000, 1700000, 1150000, 'INR', 'Naukri Salary Report', 820, 'Kotlin,Jetpack Compose,Architecture,Performance', 2024),
('Android Developer', 'Bangalore', 'IT/Software', 5, 8, 1400000, 2800000, 2000000, 'INR', 'Naukri Salary Report', 520, 'Architecture,Kotlin,Jetpack,Performance,System Design', 2024),
('Android Developer', 'Hyderabad', 'IT/Software', 2, 5, 680000, 1550000, 1050000, 'INR', 'PayScale India', 580, 'Kotlin,Java,Android SDK', 2024),

('iOS Developer', 'Bangalore', 'IT/Software', 0, 2, 400000, 900000, 650000, 'INR', 'Naukri Salary Report', 480, 'Swift,SwiftUI,UIKit,Xcode,Core Data', 2024),
('iOS Developer', 'Bangalore', 'IT/Software', 2, 5, 800000, 1800000, 1250000, 'INR', 'Naukri Salary Report', 580, 'Swift,SwiftUI,Architecture,Performance,Combine', 2024),
('iOS Developer', 'Bangalore', 'IT/Software', 5, 8, 1500000, 3000000, 2100000, 'INR', 'Naukri Salary Report', 360, 'Architecture,Swift,Performance,Leadership', 2024),
('iOS Developer', 'Hyderabad', 'IT/Software', 2, 5, 720000, 1650000, 1120000, 'INR', 'PayScale India', 380, 'Swift,UIKit,iOS Development', 2024),
('iOS Developer', 'Mumbai', 'IT/Software', 2, 5, 850000, 1950000, 1350000, 'INR', 'LinkedIn India', 280, 'Swift,SwiftUI,Architecture', 2024),

('React Native Developer', 'Bangalore', 'IT/Software', 2, 5, 700000, 1600000, 1100000, 'INR', 'Naukri Salary Report', 620, 'React Native,JavaScript,TypeScript,Redux,iOS,Android', 2024),
('React Native Developer', 'Hyderabad', 'IT/Software', 2, 5, 640000, 1450000, 980000, 'INR', 'PayScale India', 480, 'React Native,TypeScript,JavaScript', 2024),

('Flutter Developer', 'Bangalore', 'IT/Software', 2, 5, 680000, 1550000, 1050000, 'INR', 'Naukri Salary Report', 520, 'Flutter,Dart,iOS,Android,BLoC', 2024),
('Flutter Developer', 'Hyderabad', 'IT/Software', 2, 5, 620000, 1400000, 940000, 'INR', 'PayScale India', 380, 'Flutter,Dart,Mobile Development', 2024),

-- ============================================================
-- QA / TEST ENGINEER
-- ============================================================
('QA Engineer', 'Bangalore', 'IT/Software', 0, 2, 320000, 700000, 500000, 'INR', 'Naukri Salary Report', 1080, 'Selenium,Java,TestNG,API Testing,SQL', 2024),
('QA Engineer', 'Bangalore', 'IT/Software', 2, 5, 580000, 1300000, 880000, 'INR', 'Naukri Salary Report', 1320, 'Selenium,Java,Appium,CI/CD,API Testing,Performance Testing', 2024),
('QA Engineer', 'Bangalore', 'IT/Software', 5, 8, 1000000, 2200000, 1500000, 'INR', 'Naukri Salary Report', 780, 'Test Architecture,Selenium,Performance,Quality Strategy', 2024),
('QA Engineer', 'Hyderabad', 'IT/Software', 2, 5, 530000, 1200000, 810000, 'INR', 'PayScale India', 980, 'Selenium,Java,API Testing,TestNG', 2024),
('QA Engineer', 'Pune', 'IT/Software', 2, 5, 500000, 1100000, 750000, 'INR', 'Naukri Salary Report', 820, 'Selenium,Java,API Testing', 2024),
('QA Engineer', 'Chennai', 'IT/Software', 2, 5, 490000, 1100000, 740000, 'INR', 'PayScale India', 780, 'Selenium,Java,Manual Testing', 2024),

-- ============================================================
-- UI/UX DESIGNER
-- ============================================================
('UX Designer', 'Bangalore', 'IT/Software', 2, 5, 600000, 1400000, 950000, 'INR', 'LinkedIn India', 680, 'Figma,User Research,Prototyping,Wireframing,Design Systems', 2024),
('UX Designer', 'Bangalore', 'IT/Software', 5, 8, 1200000, 2600000, 1800000, 'INR', 'LinkedIn India', 420, 'Design Systems,User Research,Strategy,Leadership', 2024),
('UX Designer', 'Bangalore', 'E-commerce', 2, 5, 800000, 1900000, 1300000, 'INR', 'LinkedIn India', 380, 'Figma,Research,Prototyping,E-commerce UX', 2024),
('UX Designer', 'Mumbai', 'IT/Software', 2, 5, 650000, 1500000, 1050000, 'INR', 'LinkedIn India', 420, 'Figma,User Research,Design Systems', 2024),
('UX Designer', 'Hyderabad', 'IT/Software', 2, 5, 550000, 1250000, 850000, 'INR', 'PayScale India', 480, 'Figma,Prototyping,User Research', 2024),
('UX Designer', 'Pune', 'IT/Software', 2, 5, 520000, 1200000, 820000, 'INR', 'Naukri Salary Report', 380, 'Figma,Wireframing,Prototyping', 2024),

-- ============================================================
-- CYBERSECURITY / NETWORK
-- ============================================================
('Security Engineer', 'Bangalore', 'IT/Software', 2, 5, 800000, 1900000, 1300000, 'INR', 'LinkedIn India', 480, 'Network Security,Penetration Testing,AWS Security,SIEM,SOC', 2024),
('Security Engineer', 'Bangalore', 'IT/Software', 5, 8, 1500000, 3500000, 2400000, 'INR', 'LinkedIn India', 320, 'Cloud Security,Zero Trust,Architecture,Compliance', 2024),
('Security Engineer', 'Hyderabad', 'IT/Software', 2, 5, 720000, 1700000, 1180000, 'INR', 'PayScale India', 360, 'Security Testing,AWS Security,Compliance', 2024),
('Security Engineer', 'Mumbai', 'Finance', 3, 6, 1000000, 2500000, 1700000, 'INR', 'NASSCOM', 240, 'Security Architecture,Compliance,BFSI Security,Audit', 2024),

-- ============================================================
-- CONSULTANT (IT Consulting Firms: TCS, Wipro, Infosys, Accenture)
-- ============================================================
('Software Consultant', 'Bangalore', 'Consulting', 2, 5, 700000, 1600000, 1100000, 'INR', 'NASSCOM', 1820, 'Java,SAP,Oracle,Client Communication,SDLC', 2024),
('Software Consultant', 'Bangalore', 'Consulting', 5, 8, 1300000, 2800000, 1900000, 'INR', 'NASSCOM', 1240, 'Architecture,Client Management,Project Delivery', 2024),
('Software Consultant', 'Hyderabad', 'Consulting', 2, 5, 640000, 1450000, 1000000, 'INR', 'NASSCOM', 1580, 'Java,SAP,.NET,Client Communication', 2024),
('Software Consultant', 'Pune', 'Consulting', 2, 5, 600000, 1380000, 960000, 'INR', 'NASSCOM', 1420, 'Java,SAP,SDLC,Testing', 2024),
('Software Consultant', 'Chennai', 'Consulting', 2, 5, 580000, 1320000, 920000, 'INR', 'NASSCOM', 1380, 'Java,SAP,Testing,Client Communication', 2024),
('Software Consultant', 'Mumbai', 'Consulting', 2, 5, 680000, 1550000, 1080000, 'INR', 'NASSCOM', 1180, 'Java,SAP,Finance Domain,Client Communication', 2024),

('IT Consultant', 'Bangalore', 'Consulting', 5, 8, 1800000, 4000000, 2700000, 'INR', 'LinkedIn India', 620, 'Solution Architecture,Client Management,Pre-sales,Domain', 2024),
('IT Consultant', 'Mumbai', 'Consulting', 5, 8, 2000000, 4500000, 3000000, 'INR', 'LinkedIn India', 380, 'Solution Architecture,Finance,Client Management', 2024),

-- ============================================================
-- ARCHITECT ROLES
-- ============================================================
('Solution Architect', 'Bangalore', 'IT/Software', 8, 12, 3000000, 7000000, 4800000, 'INR', 'LinkedIn India', 420, 'Solution Design,Cloud,Microservices,Client Facing,API Design', 2024),
('Solution Architect', 'Bangalore', 'E-commerce', 8, 12, 3500000, 8000000, 5500000, 'INR', 'LinkedIn India', 220, 'Platform Architecture,Distributed Systems,Scalability', 2024),
('Solution Architect', 'Hyderabad', 'IT/Software', 8, 12, 2700000, 6200000, 4300000, 'INR', 'PayScale India', 280, 'Solution Design,Cloud,Microservices', 2024),
('Solution Architect', 'Mumbai', 'Finance', 8, 12, 3200000, 7500000, 5200000, 'INR', 'NASSCOM', 180, 'Fintech Architecture,BFSI,Cloud,Compliance', 2024),

('Principal Engineer', 'Bangalore', 'IT/Software', 10, 20, 5000000, 12000000, 7500000, 'INR', 'LinkedIn India', 180, 'Technical Vision,Architecture,Org-wide Impact,Mentoring', 2024),
('Staff Engineer', 'Bangalore', 'IT/Software', 8, 15, 3800000, 9000000, 6000000, 'INR', 'LinkedIn India', 240, 'Architecture,Technical Leadership,Cross-team Influence', 2024),

-- ============================================================
-- FINANCE / FINTECH
-- ============================================================
('Financial Analyst', 'Mumbai', 'Finance', 0, 3, 500000, 1200000, 800000, 'INR', 'NASSCOM', 680, 'Financial Modeling,Excel,Bloomberg,CFA,Accounting', 2024),
('Financial Analyst', 'Mumbai', 'Finance', 3, 6, 1000000, 2500000, 1700000, 'INR', 'NASSCOM', 520, 'Financial Modeling,Excel,Valuation,CFA,M&A', 2024),
('Financial Analyst', 'Bangalore', 'Finance', 0, 3, 480000, 1100000, 740000, 'INR', 'NASSCOM', 380, 'Financial Modeling,Excel,Accounting,SQL', 2024),
('Financial Analyst', 'Delhi', 'Finance', 0, 3, 490000, 1150000, 760000, 'INR', 'NASSCOM', 420, 'Financial Modeling,Excel,Bloomberg', 2024),

('Investment Banker', 'Mumbai', 'Finance', 0, 3, 1200000, 3000000, 2000000, 'INR', 'LinkedIn India', 180, 'Financial Modeling,Valuation,M&A,Excel,PowerPoint', 2024),
('Investment Banker', 'Mumbai', 'Finance', 3, 6, 2500000, 6000000, 4000000, 'INR', 'LinkedIn India', 120, 'Valuation,M&A,Client Management,Deal Execution', 2024),

('Quantitative Analyst', 'Mumbai', 'Finance', 2, 5, 1500000, 4000000, 2700000, 'INR', 'LinkedIn India', 140, 'Python,C++,Statistics,Quantitative Finance,Derivatives', 2024),
('Quantitative Analyst', 'Mumbai', 'Finance', 5, 8, 3000000, 7000000, 5000000, 'INR', 'LinkedIn India', 80, 'Python,C++,ML,HFT,Risk Models', 2024),

-- ============================================================
-- MARKETING / GROWTH
-- ============================================================
('Growth Manager', 'Bangalore', 'IT/Software', 3, 6, 1000000, 2500000, 1700000, 'INR', 'LinkedIn India', 420, 'Growth Hacking,Analytics,SQL,A/B Testing,SEO,SEM', 2024),
('Growth Manager', 'Bangalore', 'E-commerce', 3, 6, 1200000, 3000000, 2000000, 'INR', 'LinkedIn India', 280, 'Growth Analytics,A/B Testing,CAC,LTV,Referral', 2024),
('Growth Manager', 'Mumbai', 'E-commerce', 3, 6, 1100000, 2700000, 1900000, 'INR', 'LinkedIn India', 220, 'Growth,Analytics,A/B Testing,Performance Marketing', 2024),

('Digital Marketing Manager', 'Bangalore', 'IT/Software', 3, 6, 800000, 2000000, 1350000, 'INR', 'LinkedIn India', 580, 'SEO,SEM,Google Analytics,Content,Social Media,Email Marketing', 2024),
('Digital Marketing Manager', 'Mumbai', 'E-commerce', 3, 6, 900000, 2200000, 1550000, 'INR', 'LinkedIn India', 420, 'Performance Marketing,SEM,Analytics,Attribution', 2024),

-- ============================================================
-- HR / TALENT
-- ============================================================
('HR Manager', 'Bangalore', 'IT/Software', 5, 8, 1200000, 2800000, 1900000, 'INR', 'LinkedIn India', 380, 'Talent Acquisition,People Management,HRIS,Culture Building', 2024),
('HR Manager', 'Mumbai', 'Finance', 5, 8, 1400000, 3200000, 2200000, 'INR', 'LinkedIn India', 260, 'Talent Management,Compensation,BFSI HR,Compliance', 2024),
('Technical Recruiter', 'Bangalore', 'IT/Software', 2, 5, 600000, 1500000, 1000000, 'INR', 'LinkedIn India', 420, 'Technical Screening,Sourcing,LinkedIn Recruiter,Talent Pipeline', 2024),

-- ============================================================
-- EXTRA CITIES: GURGAON / NOIDA / AHMEDABAD
-- ============================================================
('Software Engineer', 'Gurgaon', 'IT/Software', 2, 5, 800000, 1800000, 1200000, 'INR', 'NASSCOM', 720, 'Java,Spring Boot,Microservices,AWS', 2024),
('Software Engineer', 'Gurgaon', 'IT/Software', 5, 8, 1500000, 3000000, 2100000, 'INR', 'NASSCOM', 520, 'System Design,Java,AWS,Architecture', 2024),
('Software Engineer', 'Gurgaon', 'Finance', 2, 5, 900000, 2100000, 1500000, 'INR', 'NASSCOM', 380, 'Java,Spring Boot,Banking Domain,Security', 2024),
('Software Engineer', 'Noida', 'IT/Software', 0, 2, 360000, 800000, 580000, 'INR', 'Naukri Salary Report', 880, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Noida', 'IT/Software', 2, 5, 720000, 1650000, 1100000, 'INR', 'Naukri Salary Report', 1020, 'Java,Spring Boot,Microservices', 2024),
('Software Engineer', 'Ahmedabad', 'IT/Software', 0, 2, 300000, 680000, 480000, 'INR', 'Naukri Salary Report', 620, 'Java,Python,SQL,Git', 2024),
('Software Engineer', 'Ahmedabad', 'IT/Software', 2, 5, 580000, 1300000, 880000, 'INR', 'Naukri Salary Report', 720, 'Java,Spring Boot,.NET,SQL', 2024),
('Data Scientist', 'Gurgaon', 'IT/Software', 2, 5, 900000, 2100000, 1450000, 'INR', 'LinkedIn India', 420, 'Python,Machine Learning,SQL,Statistics', 2024),
('Product Manager', 'Gurgaon', 'E-commerce', 3, 6, 1400000, 3200000, 2200000, 'INR', 'LinkedIn India', 280, 'Growth,Analytics,A/B Testing,Product Roadmap', 2024)

) AS v(job_title, city, industry, experience_min, experience_max, salary_min, salary_max, salary_median, currency, data_source, sample_size, skills_required, reported_year)
WHERE NOT EXISTS (
    SELECT 1 FROM salary_data_points
    WHERE salary_data_points.job_title = v.job_title
    AND salary_data_points.city = v.city
    AND salary_data_points.industry = v.industry
    AND salary_data_points.experience_min = v.experience_min
    AND salary_data_points.experience_max = v.experience_max
);
