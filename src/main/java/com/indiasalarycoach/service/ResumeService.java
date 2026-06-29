package com.indiasalarycoach.service;

import com.indiasalarycoach.dto.request.ResumeRequest;
import com.indiasalarycoach.entity.Resume;
import com.indiasalarycoach.entity.ResumeScore;
import com.indiasalarycoach.entity.User;
import com.indiasalarycoach.exception.PlanLimitException;
import com.indiasalarycoach.exception.ResourceNotFoundException;
import com.indiasalarycoach.repository.ResumeRepository;
import com.indiasalarycoach.repository.ResumeScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.indiasalarycoach.config.RoleKeywords.ROLE_KEYWORDS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final GeminiService geminiService;
    private final GroqService groqService;
    private final ResumeRepository resumeRepository;
    private final ResumeScoreRepository resumeScoreRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    // Real ATS keyword database by role    -  replaced to config/RoleKeywords.java
   
   private String findSupportedRole(String role) {

    if (role == null || role.isBlank()) {
        return null;
    }

    role = role.toLowerCase();

    if (role.contains("java") && role.contains("backend")) {
        return "Java Developer";
    }
    if (role.contains("java")) {
        return "Java developer";
    }
    if (role.contains("backend")) {
        return "Backend Developer" ;
    }

    if(role.contains("spring")) return "Spring Boot Developer";
    if(role.contains("react")) return "React Developer";

    if(role.contains("machine learning")) return "Machine Learning Engineer";
    if(role.contains("data scientist")) return "Data Scientist";
    if(role.contains("data analyst")) return "Data Analyst";
    if(role.contains("ai")) return "AI Engineer";

    if(role.contains("devops")) return "DevOps Engineer";
    if(role.contains("cloud")) return "Cloud Engineer";

    if(role.contains("node")) return "Node.js Developer";
    if(role.contains("python")) return "Python Developer";
    if(role.contains("android")) return "Android Developer";
    if(role.contains("flutter")) return "Flutter Developer";
    if(role.contains("qa")) return "QA Engineer";

if(role.contains("full stack")) return "Full Stack Developer";
if(role.contains("frontend")) return "Frontend Developer";

    return null;
}

private static final Set<String> STOP_WORDS = Set.of(
    "the","and","for","with","from","that","this","have","will",
    "your","their","our","you","they","them","who","what",
    "looking","candidate","candidates","required","preferred",
    "experience","years","year","position","role","job",
    "responsibilities","responsibility","skills","skill",
    "knowledge","ability","abilities","work","working",
    "team","teams","company","organizations","organization",
    "good","strong","excellent","must","should","can",
    "using","used","use","build","building","develop",
    "developer","engineering","engineer"
);

    @Transactional
    public Resume createResume(ResumeRequest req, String email) {
        User user = userService.getByEmail(email);

        if (user.getPlan() == User.Plan.FREE) {
            throw new PlanLimitException("Resume builder requires Premium plan.");
        }
        Resume resume = Resume.builder()
                .user(user).title(req.getTitle()).templateId(req.getTemplateId())
                .personalInfo(req.getPersonalInfo()).summary(req.getSummary())
                .experience(req.getExperience()).education(req.getEducation())
                .skills(req.getSkills()).certifications(req.getCertifications())
                .projects(req.getProjects()).targetRole(req.getTargetRole())
                .atsOptimized(false).build();
        return resumeRepository.save(resume);
    }

    public List<Resume> getResumes(String email) {
        User user = userService.getByEmail(email);
        return resumeRepository.findByUserIdOrderByUpdatedAtDesc(user.getId());
    }

    public Resume getResume(Long id, String email) {
        User user = userService.getByEmail(email);
        return resumeRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    @Transactional
    public Resume updateResume(Long id, Map<String, Object> updates, String email) {
        Resume resume = getResume(id, email);
        if (updates.containsKey("title")) resume.setTitle((String) updates.get("title"));
        if (updates.containsKey("templateId")) resume.setTemplateId((String) updates.get("templateId"));
        if (updates.containsKey("personalInfo")) resume.setPersonalInfo((String) updates.get("personalInfo"));
        if (updates.containsKey("summary")) resume.setSummary((String) updates.get("summary"));
        if (updates.containsKey("experience")) resume.setExperience((String) updates.get("experience"));
        if (updates.containsKey("education")) resume.setEducation((String) updates.get("education"));
        if (updates.containsKey("skills")) resume.setSkills((String) updates.get("skills"));
        if (updates.containsKey("certifications")) resume.setCertifications((String) updates.get("certifications"));
        if (updates.containsKey("projects")) resume.setProjects((String) updates.get("projects"));
        if (updates.containsKey("targetRole")) resume.setTargetRole((String) updates.get("targetRole"));
        return resumeRepository.save(resume);
    }

    @Transactional
    public void deleteResume(Long id, String email) {
        Resume resume = getResume(id, email);
        resumeRepository.delete(resume);
    }

    @Transactional
    public Map<String, Object> scoreResume(Long id, String jobDescription, String targetRole, String email) {
        Resume resume = getResume(id, email);
        String resumeText = buildResumeText(resume);
        String jd = jobDescription != null ? jobDescription : "";

        // Real ATS scoring algorithm
        String role = targetRole != null
        ? targetRole
        : (resume.getTargetRole() != null ? resume.getTargetRole() : "");

String matchedRole = findSupportedRole(role);

Map<String, Integer> roleKeywordMap =
        ROLE_KEYWORDS.getOrDefault(matchedRole, Map.of());

List<String> roleKeywords =
        new ArrayList<>(roleKeywordMap.keySet());
       
        List<String> jdKeywords = extractKeywords(jd);
        List<String> allKeywords = new ArrayList<>(roleKeywords);
        allKeywords.addAll(jdKeywords);
        allKeywords = allKeywords.stream().distinct().toList();
       
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        for (String kw : allKeywords) {
            if (resumeText.toLowerCase().contains(kw.toLowerCase())) matched.add(kw);
            else missing.add(kw);
        }

        int totalWeight = roleKeywordMap.values()
        .stream()
        .mapToInt(Integer::intValue)
        .sum();

int matchedWeight = matched.stream()
        .mapToInt(k -> roleKeywordMap.getOrDefault(k, 1))
        .sum();

double keywordScore =
        totalWeight == 0
        ? 70
        : ((double) matchedWeight / totalWeight) * 100;
        double formatScore = scoreFormat(resume);
        double contentScore = scoreContent(resume);
        double atsScore = (keywordScore * 0.5 + formatScore * 0.3 + contentScore * 0.2);
        double overallScore = (atsScore * 0.6 + contentScore * 0.4);

        List<String> suggestions = generateSuggestions(resume, missing, formatScore, contentScore);

        ResumeScore score = ResumeScore.builder()
                .resume(resume).overallScore(Math.round(overallScore * 10.0) / 10.0)
                .atsScore(Math.round(atsScore * 10.0) / 10.0)
                .keywordScore(Math.round(keywordScore * 10.0) / 10.0)
                .formatScore(formatScore).contentScore(contentScore)
                .missingKeywords(String.join(",", missing))
                .matchedKeywords(String.join(",", matched))
                .suggestions(String.join("||", suggestions))
                .jobDescription(jobDescription).build();
        score = resumeScoreRepository.save(score);

        resume.setLatestAtsScore(overallScore);
        resume.setAtsOptimized(overallScore >= 75);
        resumeRepository.save(resume);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resumeId", id);
        result.put("overallScore", score.getOverallScore());
        result.put("atsScore", score.getAtsScore());
        result.put("keywordScore", score.getKeywordScore());
        result.put("formatScore", score.getFormatScore());
        result.put("contentScore", score.getContentScore());
        result.put("missingKeywords", missing);
        result.put("matchedKeywords", matched);
        result.put("suggestions", suggestions);
        result.put("scoredAt", score.getScoredAt());
        return result;
    }

    public Map<String, Object> optimizeResume(Long id, String targetRole, String email) {
        Resume resume = getResume(id, email);
       String matchedRole = findSupportedRole(targetRole);

Map<String, Integer> roleKeywordMap =
        ROLE_KEYWORDS.getOrDefault(matchedRole, Map.of());
List<String> roleKeywords =
        new ArrayList<>(roleKeywordMap.keySet());
      String resumeText = buildResumeText(resume).toLowerCase();

        List<String> toAdd = roleKeywords.stream()
        .filter(kw -> !resumeText.contains(kw.toLowerCase()))
        .toList();
        List<String> suggestions = List.of(
            "Quantify achievements with specific numbers and percentages",
            "Use active voice and strong action verbs (led, designed, delivered, optimized)",
            "Include relevant keywords from the job description in context",
            "Add measurable impact statements (e.g., 'Reduced load time by 40%')",
            "Tailor your professional summary to the target role"
        );

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("suggestions", suggestions);
        result.put("improvedSummary", "Results-driven " + (targetRole != null ? targetRole : "professional") +
                " with proven track record of delivering high-impact solutions. Expertise in " +
                String.join(", ", roleKeywords.stream().limit(3).toList()) + ".");
        result.put("skillsToAdd", toAdd);
        result.put("keywordsToInclude", roleKeywords.stream().limit(10).toList());
        result.put("sectionFeedback", List.of(
            Map.of("section", "Summary", "score", 70.0, "feedback", "Add quantified achievements and role-specific keywords"),
            Map.of("section", "Experience", "score", 65.0, "feedback", "Use STAR format: Situation, Task, Action, Result"),
            Map.of("section", "Skills", "score", 80.0, "feedback", "Group skills by category for better readability")
        ));
        return result;
    }

    private String buildResumeText(Resume r) {
        return String.join(" ", Arrays.asList(
            r.getSummary(), r.getExperience(), r.getSkills(), r.getCertifications(), r.getProjects()
        ).stream().filter(Objects::nonNull).toList());
    }

    private List<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) return List.of();
        String[] words = text.split("\\s+");
        Map<String, Long> freq = Arrays.stream(words)
        .map(w -> w.replaceAll("[^a-zA-Z+#.]", "").toLowerCase())
        .filter(w -> w.length() > 3)
        .filter(w -> !STOP_WORDS.contains(w))
        .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
        ));

return freq.entrySet().stream()
        .filter(e -> e.getValue() >= 2)
        .map(Map.Entry::getKey)
        .limit(30)
        .toList();
    }

    private double scoreFormat(Resume r) {
        int sections = 0;
        if (r.getSummary() != null && !r.getSummary().isBlank()) sections++;
        if (r.getExperience() != null && !r.getExperience().isBlank()) sections++;
        if (r.getEducation() != null && !r.getEducation().isBlank()) sections++;
        if (r.getSkills() != null && !r.getSkills().isBlank()) sections++;
        if (r.getPersonalInfo() != null && !r.getPersonalInfo().isBlank()) sections++;
        return Math.round((sections / 5.0) * 100.0 * 10.0) / 10.0;
    }

    private double scoreContent(Resume r) {
        int score = 50;
        if (r.getSummary() != null && r.getSummary().length() > 100) score += 15;
        if (r.getExperience() != null && r.getExperience().length() > 200) score += 20;
        if (r.getSkills() != null && r.getSkills().contains(",")) score += 10;
        if (r.getProjects() != null && !r.getProjects().isBlank()) score += 5;
        return Math.min(100, score);
    }

    private List<String> generateSuggestions(Resume r, List<String> missing, double format, double content) {
        List<String> s = new ArrayList<>();
        if (!missing.isEmpty()) s.add("Add these keywords to improve ATS score: " + String.join(", ", missing.stream().limit(5).toList()));
        if (format < 80) s.add("Complete all resume sections (summary, experience, education, skills)");
        if (content < 70) s.add("Expand your experience descriptions with quantifiable achievements");
        if (r.getSummary() == null || r.getSummary().length() < 100) s.add("Write a compelling professional summary (2-3 sentences)");
        s.add("Use a clean, ATS-friendly format without tables or graphics in the core sections");
        return s;
    }

    public String aiOptimize(Long id, String targetRole, String email) {
 String matchedRole = findSupportedRole(targetRole);

boolean supportedRole = matchedRole != null;


    Resume resume = getResume(id, email);

    String resumeText = buildResumeText(resume).toLowerCase();

double atsScore = 0;

if (supportedRole) {

    Map<String, Integer> roleKeywordMap =
            ROLE_KEYWORDS.getOrDefault(matchedRole, Map.of());

    int totalWeight = roleKeywordMap.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();

    int matchedWeight = roleKeywordMap.entrySet() .stream() .filter(e -> resumeText .contains(e.getKey().toLowerCase())) .mapToInt(Map.Entry::getValue) .sum();

    double keywordScore = totalWeight == 0 ? 70 : ((double) matchedWeight / totalWeight) * 100;

    double formatScore = scoreFormat(resume);
    double contentScore = scoreContent(resume);

    atsScore = Math.round( (keywordScore * 0.5 + formatScore * 0.3 + contentScore * 0.2) * 10.0 ) / 10.0; }

String prompt ;

if (supportedRole) {

    prompt = """
Analyze this resume specifically for the role: %s

Do NOT use markdown.

Do NOT wrap response in ```json.

Return detailed professional ATS analysis.
Return ONLY valid JSON.

Requirements:
- strengths: minimum 5 detailed points
- missingSkills: minimum 8 detailed points
- suggestions: minimum 5 actionable recommendations
- improvedSummary: 3-5 professional sentences
- Analyze deeply like a professional ATS system
- Do not return generic keywords
- Explain WHY each strength matters
- Explain WHY each missing skill matters

{
  "strengths": [],
  "missingSkills": [],
  "improvedSummary": "",
  "suggestions": []
}

Rules:
- Compare ONLY against the target role.
- Do not generate ATS score.
- Focus on strengths and improvements.

Resume:
%s
""".formatted(targetRole, resumeText);

} else {

    prompt = """
Analyze this resume for the role: %s

Return ONLY valid JSON.

{
  "atsScore": 0,
  "strengths": [],
  "missingSkills": [],
  "improvedSummary": "",
  "suggestions": []
}

Rules:
- ATS score must be realistic.
- 90-100 = Excellent match
- 75-89 = Strong match
- 50-74 = Average match
- Below 50 = Weak match

Resume:
%s
""".formatted(targetRole, resumeText);

}

String aiResponse;

try {
    aiResponse = geminiService.askGemini(prompt);
} catch (Exception e) {

   log.warn("Gemini failed. Falling back to Groq.");

    aiResponse = groqService.analyze(prompt);
}
    aiResponse = aiResponse
        .replace("```json", "")
        .replace("```", "")
        .trim();

    
try {

    JsonNode root = objectMapper.readTree(aiResponse);

    if (root instanceof ObjectNode objectNode) {

        objectNode.put("atsScore", atsScore);

        String improvedSummary = (String) objectNode.path("improvedSummary").asText(null);
        JsonNode missingSkillsNode = objectNode.path("missingSkills");

        StringBuilder optimizedSkills = new StringBuilder(resume.getSkills() );

        resume.setOptimizedskills(improvedSummary);
        resume.setOptimizedsummary(improvedSummary);
        resume.setLatestAtsScore(atsScore);

        resumeRepository.save(resume);

        return objectMapper.writeValueAsString(objectNode);
    }

    return aiResponse;

} catch (Exception e) {

    log.error("Failed to inject ATS score", e);

    return aiResponse;


}

}


public byte[] generateResumePdf(Long id, String email, boolean optimized) {

    Resume resume = getResume(id, email);
     
    String summary = resume.getSummary();
    String skills = resume.getSkills();

   if (optimized) {
        Map <String, Object> result = optimizeResume(id, resume.getTargetRole(), email);

        summary = (String) result.get("improvedSummary");

        @SuppressWarnings("unchecked")
       List<String> skillsToAdd = (List<String>) result.get("skillsToAdd");

    skills = resume.getSkills() + ", " + String.join( ", ", skillsToAdd);

   }
   
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    Document document = new Document();

    try {

        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont =
                new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);

        Font headingFont =
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

        // Title
       Paragraph title = new Paragraph(
        resume.getTitle() != null
                ? resume.getTitle()
                : "Resume",
        titleFont
);

title.setAlignment(Element.ALIGN_CENTER);

document.add(title);

        // Personal Info
        if (resume.getPersonalInfo() != null) {
            document.add(
                    new Paragraph(resume.getPersonalInfo())
            );
        }

        document.add(
                new Paragraph(
                        "------------------------------------------------------------"
                )
        );

        document.add(new Paragraph(" "));

        // Summary
       if (summary != null) {

    document.add(
            new Paragraph(
                    "SUMMARY",
                    headingFont
            )
    );

    document.add(
            new Paragraph(
                    summary
            )
    );

    document.add(new Paragraph(" "));
}

        // Experience

        if (resume.getExperience() != null) {

    document.add(
            new Paragraph(
                    "EXPERIENCE",
                    headingFont
            )
    );

    for (String exp : resume.getExperience().split("\\.")) {

        if (!exp.isBlank()) {

            document.add(
                    new Paragraph(
                            "• " + exp.trim()
                    )
            );
        }
    }

    document.add(new Paragraph(" "));
}

        // Education
        if (resume.getEducation() != null) {

            document.add(
                    new Paragraph(
                            "EDUCATION",
                            headingFont
                    )
            );

            document.add(
                    new Paragraph(
                            resume.getEducation()
                    )
            );

            document.add(new Paragraph(" "));
        }

//    Certification

        if (resume.getCertifications() != null) {

    document.add(
            new Paragraph(
                    "CERTIFICATIONS",
                    headingFont
            )
    );

    for (String cert : resume.getCertifications().split(",")) {

        document.add(
                new Paragraph(
                        "• " + cert.trim()
                )
        );
    }

    document.add(new Paragraph(" "));
}

        // Skills
if (skills != null) {

    document.add(
            new Paragraph(
                    "SKILLS",
                    headingFont
            )
    );

    document.add(
            new Paragraph(
                    skills
            )
    );

    document.add(new Paragraph(" "));
}

        // Projects
       if (resume.getProjects() != null) {

    document.add(
            new Paragraph(
                    "PROJECTS",
                    headingFont
            )
    );

    for (String project : resume.getProjects().split("\\.")) {

        if (!project.isBlank()) {

            document.add(
                    new Paragraph(
                            "• " + project.trim()
                    )
            );
        }
    }

    document.add(new Paragraph(" "));
}

        document.close();

        return out.toByteArray();

    } catch (Exception e) {

        throw new RuntimeException(
                "Failed to generate PDF",
                e
        );
    }
}
}
