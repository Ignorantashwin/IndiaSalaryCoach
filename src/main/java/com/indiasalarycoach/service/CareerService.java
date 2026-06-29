package com.indiasalarycoach.service;

import com.indiasalarycoach.dto.request.CareerReportRequest;
import com.indiasalarycoach.entity.CareerReport;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.exception.PlanLimitException;
import com.indiasalarycoach.exception.ResourceNotFoundException;
import com.indiasalarycoach.repository.CareerReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareerService {
    
    private final GeminiService geminiService;
    private final GroqService groqService;
    private final CareerReportRepository careerReportRepository;
    private final UserService userService;

    @Value("${openai.api-key:}") private String openaiApiKey;

    // Career transition knowledge base (structured data, not hardcoded salaries)
    private static final Map<String, Map<String, Object>> ROLE_TRANSITIONS = new LinkedHashMap<>();
    static {
        ROLE_TRANSITIONS.put("Software Engineer→Senior Software Engineer", Map.of(
            "timeline", 24, "skills", List.of("System Design", "Leadership", "Code Review", "Architecture"),
            "resources", List.of("Designing Data-Intensive Applications", "System Design Interview", "Tech Lead courses")));
        ROLE_TRANSITIONS.put("Software Engineer→Engineering Manager", Map.of(
            "timeline", 36, "skills", List.of("People Management", "Project Planning", "Stakeholder Communication", "OKRs"),
            "resources", List.of("The Manager's Path", "Engineering Management courses", "Leadership workshops")));
        ROLE_TRANSITIONS.put("Data Analyst→Data Scientist", Map.of(
            "timeline", 18, "skills", List.of("Machine Learning", "Python", "Statistics", "Deep Learning"),
            "resources", List.of("Fast.ai", "Coursera ML specialization", "Kaggle competitions")));
        ROLE_TRANSITIONS.put("Product Manager→Senior Product Manager", Map.of(
            "timeline", 30, "skills", List.of("Product Strategy", "Executive Communication", "P&L Management", "Cross-functional Leadership"),
            "resources", List.of("Inspired by Marty Cagan", "PM courses", "Product strategy frameworks")));
    }

    @Transactional
    public CareerReport generateReport(CareerReportRequest req, String email) {
        User user = userService.getByEmail(email);
        if (user.getPlan() == User.Plan.FREE || user.getPlan() == User.Plan.PRO) {
            throw new PlanLimitException("Career roadmap requires Premium plan.");
        }

        String transitionKey = req.getCurrentRole() + "→" + req.getTargetRole();
        Map<String, Object> transitionData = ROLE_TRANSITIONS.getOrDefault(transitionKey, null);

        List<String> inputSkills = req.getSkills() != null ?
                Arrays.asList(req.getSkills().split(",\\s*")) : List.of();

        // Compute skill gaps dynamically
        List<String> requiredSkills;
        int timeline;
        List<String> resources;

        if (transitionData != null) {
            requiredSkills = (List<String>) transitionData.get("skills");
            timeline = (Integer) transitionData.get("timeline");
            resources = (List<String>) transitionData.get("resources");
        } else {
            requiredSkills = List.of("Leadership", "Communication", "Domain Expertise", "Strategic Thinking");
            timeline = 24;
            resources = List.of("LinkedIn Learning", "Coursera", "Industry blogs and publications");
        }

        List<String> skillGapList = requiredSkills.stream()
                .filter(s -> inputSkills.stream().noneMatch(us -> us.equalsIgnoreCase(s)))
                .toList();

        double salaryProjection = estimateSalaryProjection(req.getTargetRole(), req.getExperienceYears() + (timeline / 12));

        String roadmap = buildRoadmap(req, requiredSkills, skillGapList, timeline);
        String skillGapsStr = String.join(", ", skillGapList);
        String learningResources = String.join(" | ", resources);

        // Use OpenAI if configured
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            try {
                String aiRoadmap = generateAiRoadmap(req, skillGapList);
                if (aiRoadmap != null) roadmap = aiRoadmap;
            } catch (Exception e) {
                log.warn("AI generation failed, using structured fallback", e);
            }
        }

        CareerReport report = CareerReport.builder()
                .user(user).currentRole(req.getCurrentRole()).targetRole(req.getTargetRole())
                .roadmap(roadmap).skillGaps(skillGapsStr).timelineMonths(timeline)
                .learningResources(learningResources).salaryProjection(salaryProjection)
                .confidenceScore(calculateConfidence(inputSkills, requiredSkills))
                .inputSkills(req.getSkills()).experienceYears(req.getExperienceYears())
                .industry(req.getIndustry()).city(req.getCity()).build();
        return careerReportRepository.save(report);
    }

    public List<CareerReport> getReports(String email) {
        User user = userService.getByEmail(email);
        return careerReportRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public CareerReport getReport(Long id, String email) {
        User user = userService.getByEmail(email);
        return careerReportRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Career report not found"));
    }

    public Map<String, Object> careerChat(String message, String context, String email) {
        User user = userService.getByEmail(email);
        if (user.getPlan() == User.Plan.FREE || user.getPlan() == User.Plan.PRO) {
            throw new PlanLimitException("AI Career Coach requires Premium plan.");
        }

        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            // TODO: Call OpenAI API with conversation context
            log.info("OpenAI integration ready - configure OPENAI_API_KEY to enable");
        }

        // Intelligent structured response
        String response = generateCareerChatResponse(message, user);
        List<String> suggestions = List.of(
            "What skills should I focus on for " + (user.getCurrentTitle() != null ? user.getCurrentTitle() : "my role") + "?",
            "What's the salary expectation for my next role?",
            "How can I improve my resume for ATS systems?",
            "What interview questions should I prepare for?"
        );

        return Map.of("message", response, "suggestions", suggestions);
    }

    public Map<String, Object> getInterviewPrep(String targetRole, String industry, String skills, String level, String email) {
        List<Map<String, Object>> questions = buildInterviewQuestions(targetRole, industry, level);
        List<String> topics = buildTechnicalTopics(targetRole);
        List<String> tips = List.of(
            "Research the company's recent news, products, and engineering blog",
            "Prepare 3-5 STAR method stories from your experience",
            "Practice coding problems on LeetCode (for technical roles)",
            "Prepare thoughtful questions to ask the interviewer",
            "Review your resume thoroughly — every item is fair game"
        );

        return Map.of("targetRole", targetRole, "commonQuestions", questions,
                "technicalTopics", topics, "tips", tips,
                "resources", List.of("LeetCode", "Glassdoor Interview Questions", "System Design Primer", "Cracking the Coding Interview"));
    }

    private String buildRoadmap(CareerReportRequest req, List<String> skills, List<String> gaps, int timeline) {
        return String.format(
            "CAREER ROADMAP: %s → %s\n\n" +
            "Phase 1 (Months 1-%d): Foundation — Master core skills: %s\n" +
            "Phase 2 (Months %d-%d): Development — Close skill gaps in: %s\n" +
            "Phase 3 (Months %d-%d): Positioning — Build portfolio, get certifications, network actively\n\n" +
            "Key actions:\n" +
            "- Start with online courses and hands-on projects\n" +
            "- Seek mentorship from professionals in target role\n" +
            "- Contribute to open-source or high-visibility internal projects\n" +
            "- Update LinkedIn and start engaging with the target community",
            req.getCurrentRole(), req.getTargetRole(),
            timeline / 3, String.join(", ", skills.stream().limit(3).toList()),
            timeline / 3 + 1, (2 * timeline / 3), String.join(", ", gaps.stream().limit(3).toList()),
            (2 * timeline / 3) + 1, timeline
        );
    }

    private String generateCareerChatResponse(String message, User user) {
        String lm = message.toLowerCase();
        if (lm.contains("salary") || lm.contains("pay") || lm.contains("ctc")) {
            return "For " + (user.getCurrentTitle() != null ? user.getCurrentTitle() : "your role") +
                   " in India, salaries typically range based on experience, city, and industry. " +
                   "Use our Salary Intelligence engine to get precise market data for your specific profile. " +
                   "Focus on upskilling in high-demand areas like cloud, AI/ML, and system design to command premium salaries.";
        }
        if (lm.contains("skill") || lm.contains("learn")) {
            return "Based on current market trends in India, the highest-value skills are: Cloud (AWS/Azure), " +
                   "System Design, Machine Learning, and DevOps practices. " +
                   "I recommend starting with one area and building depth before breadth. " +
                   "Certifications from AWS, Google, or Microsoft carry significant market value.";
        }
        if (lm.contains("interview")) {
            return "To ace technical interviews at top Indian tech companies (FAANG India, Flipkart, Paytm, Zomato): " +
                   "1) Practice 50+ LeetCode problems (focus on medium difficulty), " +
                   "2) Study system design for senior roles, " +
                   "3) Prepare behavioral stories using the STAR framework. " +
                   "Most companies do 4-6 rounds including 1-2 technical screens, system design, and HR.";
        }
        return "I'm your AI career coach for the Indian tech market. I can help you with salary insights, " +
               "skill development paths, interview preparation, and career transition strategies. " +
               "What specific aspect of your career would you like to work on today?";
    }

    private double estimateSalaryProjection(String role, int totalExp) {
        double base = switch (role.toLowerCase()) {
            case "senior software engineer" -> totalExp <= 6 ? 2200000 : 3000000;
            case "engineering manager", "tech lead" -> 3500000;
            case "data scientist" -> totalExp <= 5 ? 1800000 : 2800000;
            case "principal engineer", "staff engineer" -> 4500000;
            default -> 1500000 + (totalExp * 100000);
        };
        return base;
    }

    private double calculateConfidence(List<String> currentSkills, List<String> required) {
        if (required.isEmpty()) return 0.7;
        long matches = required.stream().filter(r -> currentSkills.stream().anyMatch(s -> s.equalsIgnoreCase(r))).count();
        return Math.round((0.4 + (matches / (double) required.size()) * 0.6) * 100.0) / 100.0;
    }

    private String generateAiRoadmap(CareerReportRequest req, List<String> gaps) {
        // Placeholder for OpenAI integration
        return null;
    }

    private List<Map<String, Object>> buildInterviewQuestions(String role, String industry, String level) {
        List<Map<String, Object>> questions = new ArrayList<>();
        // Behavioral
        questions.add(Map.of("question", "Tell me about a time you led a complex technical project under tight deadlines.",
                "category", "Behavioral", "difficulty", "medium", "sampleAnswer", "Use STAR: Situation, Task, Action, Result"));
        questions.add(Map.of("question", "Describe a situation where you had to disagree with your manager. How did you handle it?",
                "category", "Behavioral", "difficulty", "medium", "sampleAnswer", "Focus on data-driven reasoning and respectful communication"));
        // Technical
        questions.add(Map.of("question", "Design a URL shortener service like bit.ly (system design).",
                "category", "Technical", "difficulty", "hard", "sampleAnswer", "Discuss hash function, database choice, caching, scalability"));
        questions.add(Map.of("question", "Explain the difference between horizontal and vertical scaling.",
                "category", "Technical", "difficulty", "easy", "sampleAnswer", "Vertical = bigger machine, Horizontal = more machines"));
        questions.add(Map.of("question", "What is database indexing and when would you use a composite index?",
                "category", "Technical", "difficulty", "medium", "sampleAnswer", "Cover B-tree indexes, cardinality, and query patterns"));
        return questions;
    }

    private List<String> buildTechnicalTopics(String role) {
        return switch (role.toLowerCase()) {
            case "software engineer", "backend engineer" -> List.of("Data Structures & Algorithms", "System Design", "Database Design", "REST API Design", "Concurrency");
            case "data scientist" -> List.of("Statistics & Probability", "Machine Learning Algorithms", "Feature Engineering", "Model Evaluation", "Python Proficiency");
            case "devops engineer" -> List.of("Kubernetes & Docker", "CI/CD Pipelines", "Cloud Architecture", "Monitoring & Alerting", "Infrastructure as Code");
            default -> List.of("Problem Solving", "Domain Knowledge", "Communication", "Technical Depth", "Leadership");
        };
    }


    public String generateInterviewQuestions(  String role, String level) {

    String prompt = """
Generate interview questions.

Role: %s
Level: %s

Return ONLY valid JSON.

{
  "technicalQuestions": [],
  "hrQuestions": [],
  "systemDesignQuestions": []
}

Requirements:
- 15 technical questions
- 10 HR questions
- 5 system design questions
- No answers
"""
.formatted(role, level);

    try {
        return geminiService.askGemini(prompt);
    } catch (Exception e) {
        return groqService.analyze(prompt);
    }
}

public String generateNegotiationLetter(
        String name,
        String company,
        String role,
        String currentOffer,
        String expectedSalary) {

    String prompt = """
Generate a salary negotiation email.

Name: %s
Company: %s
Role: %s
Current Offer: %s
Expected Salary: %s

Return ONLY valid JSON.

{
  "subject": "",
  "letter": ""
}

Professional tone.
"""
.formatted(
        name,
        company,
        role,
        currentOffer,
        expectedSalary
);

    try {
        return geminiService.askGemini(prompt);
    } catch (Exception e) {
        return groqService.analyze(prompt);
    }
}

}
