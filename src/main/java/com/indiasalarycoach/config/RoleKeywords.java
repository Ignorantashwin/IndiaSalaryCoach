package com.indiasalarycoach.config;

import java.util.HashMap;

import java.util.Map;

public class RoleKeywords {

    public static final Map<String, Map<String, Integer>> ROLE_KEYWORDS =

            new HashMap<>();

    static {

       
        ROLE_KEYWORDS.put(
    "Software Engineer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("OOP", 9),
        Map.entry("Data Structures", 9),
        Map.entry("Algorithms", 9),
        Map.entry("Git", 8),
        Map.entry("SQL", 8),
        Map.entry("System Design", 7),
        Map.entry("REST API", 7),
        Map.entry("Testing", 6)
    )
);

ROLE_KEYWORDS.put(
    "Software Development Engineer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("DSA", 10),
        Map.entry("Algorithms", 9),
        Map.entry("System Design", 8),
        Map.entry("OOP", 8),
        Map.entry("SQL", 7),
        Map.entry("Git", 6),
        Map.entry("REST API", 6)
    )
);

ROLE_KEYWORDS.put(
    "Machine Learning Engineer",
    Map.ofEntries(
        Map.entry("Python", 10),
        Map.entry("Machine Learning", 10),
        Map.entry("TensorFlow", 8),
        Map.entry("PyTorch", 8),
        Map.entry("Scikit-learn", 8),
        Map.entry("Deep Learning", 8),
        Map.entry("SQL", 6),
        Map.entry("MLOps", 6),
        Map.entry("AWS", 5)
    )
);

ROLE_KEYWORDS.put(
    "Cyber Security Engineer",
    Map.ofEntries(
        Map.entry("Network Security", 10),
        Map.entry("Penetration Testing", 9),
        Map.entry("OWASP", 9),
        Map.entry("SIEM", 8),
        Map.entry("Linux", 8),
        Map.entry("Firewalls", 8),
        Map.entry("Cryptography", 7),
        Map.entry("Incident Response", 7)
    )
);

ROLE_KEYWORDS.put(
    "Business Analyst",
    Map.ofEntries(
        Map.entry("SQL", 10),
        Map.entry("Excel", 10),
        Map.entry("Power BI", 8),
        Map.entry("Requirements Gathering", 8),
        Map.entry("Stakeholder Management", 8),
        Map.entry("Data Analysis", 8),
        Map.entry("JIRA", 6),
        Map.entry("Agile", 6)
    )
);

ROLE_KEYWORDS.put(
    "Database Administrator",
    Map.ofEntries(
        Map.entry("SQL", 10),
        Map.entry("PostgreSQL", 9),
        Map.entry("MySQL", 9),
        Map.entry("Database Design", 8),
        Map.entry("Performance Tuning", 8),
        Map.entry("Backup", 7),
        Map.entry("Replication", 7),
        Map.entry("Linux", 6)
    )
);

ROLE_KEYWORDS.put(
    "Site Reliability Engineer",
    Map.ofEntries(
        Map.entry("Linux", 10),
        Map.entry("Docker", 9),
        Map.entry("Kubernetes", 9),
        Map.entry("AWS", 8),
        Map.entry("Monitoring", 8),
        Map.entry("Prometheus", 8),
        Map.entry("Grafana", 8),
        Map.entry("CI/CD", 7),
        Map.entry("Terraform", 7)
    )
);

ROLE_KEYWORDS.put(
    "Technical Support Engineer",
    Map.ofEntries(
        Map.entry("Troubleshooting", 10),
        Map.entry("Linux", 8),
        Map.entry("SQL", 7),
        Map.entry("Networking", 7),
        Map.entry("Customer Support", 8),
        Map.entry("Ticketing Systems", 6),
        Map.entry("Windows", 6)
    )
);

ROLE_KEYWORDS.put(
    "MERN Stack Developer",
    Map.ofEntries(
        Map.entry("MongoDB", 10),
        Map.entry("Express", 10),
        Map.entry("React", 10),
        Map.entry("Node.js", 10),
        Map.entry("JavaScript", 9),
        Map.entry("REST API", 8),
        Map.entry("JWT", 7),
        Map.entry("Git", 6)
    )
);

ROLE_KEYWORDS.put(
    "PHP Developer",
    Map.ofEntries(
        Map.entry("PHP", 10),
        Map.entry("Laravel", 9),
        Map.entry("MySQL", 8),
        Map.entry("REST API", 7),
        Map.entry("JavaScript", 6),
        Map.entry("Git", 5)
    )
);
ROLE_KEYWORDS.put(
    "Java Developer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("Spring Boot", 10),
        Map.entry("REST API", 9),
        Map.entry("Hibernate", 8),
        Map.entry("JPA", 8),
        Map.entry("SQL", 8),
        Map.entry("PostgreSQL", 8),
        Map.entry("Microservices", 8),
        Map.entry("Spring Security", 7),
        Map.entry("JWT", 7),
        Map.entry("Docker", 6),
        Map.entry("AWS", 6)
    )
);

ROLE_KEYWORDS.put(
    "Backend Developer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("Spring Boot", 10),
        Map.entry("REST API", 9),
        Map.entry("Microservices", 8),
        Map.entry("SQL", 8),
        Map.entry("PostgreSQL", 8),
        Map.entry("Docker", 7),
        Map.entry("Redis", 7),
        Map.entry("Kafka", 6),
        Map.entry("AWS", 6)
    )
);

ROLE_KEYWORDS.put(
    "Frontend Developer",
    Map.ofEntries(
        Map.entry("React", 10),
        Map.entry("JavaScript", 10),
        Map.entry("TypeScript", 9),
        Map.entry("HTML", 9),
        Map.entry("CSS", 9),
        Map.entry("Redux", 8),
        Map.entry("Next.js", 8),
        Map.entry("Tailwind CSS", 7),
        Map.entry("REST API", 6),
        Map.entry("Git", 5)
    )
);

ROLE_KEYWORDS.put(
    "React Developer",
    Map.ofEntries(
        Map.entry("React", 10),
        Map.entry("JavaScript", 10),
        Map.entry("TypeScript", 9),
        Map.entry("Redux", 8),
        Map.entry("Next.js", 8),
        Map.entry("HTML", 8),
        Map.entry("CSS", 8),
        Map.entry("REST API", 7),
        Map.entry("Tailwind CSS", 6),
        Map.entry("Git", 5)
    )
);

ROLE_KEYWORDS.put(
    "Full Stack Developer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("Spring Boot", 9),
        Map.entry("React", 9),
        Map.entry("JavaScript", 8),
        Map.entry("TypeScript", 8),
        Map.entry("REST API", 8),
        Map.entry("SQL", 7),
        Map.entry("PostgreSQL", 7),
        Map.entry("Docker", 6),
        Map.entry("AWS", 5)
    )
);

ROLE_KEYWORDS.put(
    "Python Developer",
    Map.ofEntries(
        Map.entry("Python", 10),
        Map.entry("Django", 9),
        Map.entry("Flask", 8),
        Map.entry("FastAPI", 8),
        Map.entry("SQL", 8),
        Map.entry("PostgreSQL", 7),
        Map.entry("REST API", 7),
        Map.entry("Docker", 6),
        Map.entry("AWS", 5)
    )
);

ROLE_KEYWORDS.put(
    "Node.js Developer",
    Map.ofEntries(
        Map.entry("Node.js", 10),
        Map.entry("Express", 9),
        Map.entry("JavaScript", 9),
        Map.entry("MongoDB", 8),
        Map.entry("TypeScript", 8),
        Map.entry("REST API", 8),
        Map.entry("JWT", 7),
        Map.entry("Docker", 6)
    )
);

ROLE_KEYWORDS.put(
    "Android Developer",
    Map.ofEntries(
        Map.entry("Java", 10),
        Map.entry("Kotlin", 10),
        Map.entry("Android SDK", 9),
        Map.entry("Jetpack Compose", 8),
        Map.entry("Firebase", 7),
        Map.entry("SQLite", 6),
        Map.entry("REST API", 6)
    )
);

ROLE_KEYWORDS.put(
    "Flutter Developer",
    Map.ofEntries(
        Map.entry("Flutter", 10),
        Map.entry("Dart", 10),
        Map.entry("Firebase", 8),
        Map.entry("REST API", 7),
        Map.entry("State Management", 7),
        Map.entry("Git", 5)
    )
);

ROLE_KEYWORDS.put(
    "DevOps Engineer",
    Map.ofEntries(
        Map.entry("Docker", 10),
        Map.entry("Kubernetes", 10),
        Map.entry("AWS", 9),
        Map.entry("Terraform", 8),
        Map.entry("CI/CD", 8),
        Map.entry("Jenkins", 8),
        Map.entry("Linux", 8),
        Map.entry("Ansible", 7)
    )
);

ROLE_KEYWORDS.put(
    "Cloud Engineer",
    Map.ofEntries(
        Map.entry("AWS", 10),
        Map.entry("Azure", 10),
        Map.entry("GCP", 9),
        Map.entry("Terraform", 8),
        Map.entry("Docker", 7),
        Map.entry("Kubernetes", 7),
        Map.entry("Linux", 7)
    )
);

ROLE_KEYWORDS.put(
    "Data Analyst",
    Map.ofEntries(
        Map.entry("SQL", 10),
        Map.entry("Excel", 10),
        Map.entry("Power BI", 9),
        Map.entry("Tableau", 9),
        Map.entry("Python", 8),
        Map.entry("Statistics", 7),
        Map.entry("Data Visualization", 7)
    )
);

ROLE_KEYWORDS.put(
    "Data Scientist",
    Map.ofEntries(
        Map.entry("Python", 10),
        Map.entry("Machine Learning", 10),
        Map.entry("Pandas", 9),
        Map.entry("NumPy", 9),
        Map.entry("Statistics", 8),
        Map.entry("Scikit-learn", 8),
        Map.entry("TensorFlow", 7),
        Map.entry("PyTorch", 7)
    )
);

ROLE_KEYWORDS.put(
    "AI Engineer",
    Map.ofEntries(
        Map.entry("Python", 10),
        Map.entry("Machine Learning", 10),
        Map.entry("LLM", 9),
        Map.entry("LangChain", 8),
        Map.entry("RAG", 8),
        Map.entry("Vector Database", 8),
        Map.entry("OpenAI", 7),
        Map.entry("TensorFlow", 7),
        Map.entry("PyTorch", 7)
    )
);

ROLE_KEYWORDS.put(
    "QA Engineer",
    Map.ofEntries(
        Map.entry("Selenium", 10),
        Map.entry("JUnit", 8),
        Map.entry("TestNG", 8),
        Map.entry("Automation Testing", 9),
        Map.entry("API Testing", 8),
        Map.entry("Postman", 7),
        Map.entry("SQL", 6)
    )
);



        // Add remaining roles here...

    }

    private RoleKeywords() {

    }

}