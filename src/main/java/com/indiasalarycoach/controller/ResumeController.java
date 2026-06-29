package com.indiasalarycoach.controller;

import com.indiasalarycoach.dto.request.ResumeRequest;
import com.indiasalarycoach.entity.Resume;
import com.indiasalarycoach.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public ResponseEntity<List<Resume>> getResumes(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.getResumes(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Resume> createResume( @Valid @RequestBody ResumeRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED) .body(resumeService.createResume(req, userDetails.getUsername()));
            
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.getResume(id, userDetails.getUsername()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Resume> updateResume(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.updateResume(id, updates, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        resumeService.deleteResume(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/score")
    public ResponseEntity<Map<String, Object>> scoreResume(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.scoreResume(
                id, body.get("jobDescription"), body.get("targetRole"), userDetails.getUsername()));
    }

    @PostMapping("/{id}/optimize")
    public ResponseEntity<Map<String, Object>> optimizeResume(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.optimizeResume(
                id, body.get("targetRole"), userDetails.getUsername()));
    }

@PostMapping("/{id}/ai-optimize")
public ResponseEntity<String> aiOptimize(
        @PathVariable Long id,
        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal UserDetails userDetails) {

    return ResponseEntity.ok(
            resumeService.aiOptimize(
                    id,
                    body.get("targetRole"),
                    userDetails.getUsername()
            )
    );
}

@GetMapping("/{id}/download")
public ResponseEntity<byte[]> downloadResume(
        @PathVariable Long id,
        @RequestParam (defaultValue ="false") boolean optimized,
        @AuthenticationPrincipal UserDetails userDetails) {
 System.out.println("DOWNLOAD ENDPOINT HIT" + "optimized = " + optimized);
    byte[] pdf = resumeService.generateResumePdf( id,userDetails.getUsername(),optimized );

    return ResponseEntity.ok() .header( HttpHeaders.CONTENT_DISPOSITION, optimized ? "attachment; filename=Optimized_resume.pdf" : "attachment; filename=resume.pdf" )
      .contentType(MediaType.APPLICATION_PDF).body(pdf);

}

}
