package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Job;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;

    public ApplicationController(ApplicationService applicationService, JobService jobService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    @GetMapping("/apply/{jobId}")
    public String showApplyForm(@PathVariable Long jobId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Job> jobOpt = jobService.findJobById(jobId);
        if (jobOpt.isPresent()) {
            model.addAttribute("job", jobOpt.get());
            return "applications/apply";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Job not found!");
        return "redirect:/jobs";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId,
                             @RequestParam(required = false) String coverLetter,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        Optional<Job> jobOpt = jobService.findJobById(jobId);

        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            var user = userDetails.getUser();
            
            if (job.getApplicationDeadline() != null &&
            	    job.getApplicationDeadline().isBefore(LocalDateTime.now())) {

            	    redirectAttributes.addFlashAttribute("errorMessage", "Application deadline has passed!");
            	    return "redirect:/jobs/" + jobId;
            	}

            // ✅ CHECK RESUME
            if (user.getResumeFilePath() == null || user.getResumeFilePath().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please upload your resume before applying!");
                return "redirect:/profile"; // or redirect to same job page
            }

            // ✅ CHECK DUPLICATE
            if (applicationService.hasUserApplied(job, user)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You already applied!");
                return "redirect:/jobs/" + jobId;
            }

            // ✅ APPLY
            Application application = new Application();
            application.setJob(job);
            application.setApplicant(user);
            application.setCoverLetter(coverLetter);

            applicationService.applyForJob(application);

            redirectAttributes.addFlashAttribute("successMessage", "Successfully applied!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Job not found!");
        }

        return "redirect:/jobs/" + jobId;
    }
    @GetMapping
    public String listApplications(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if ("ROLE_EMPLOYER".equals(userDetails.getUser().getRole().name())) {
            // Should actually get all jobs posted by employer, then all applications for those jobs.
            // But for simplicity, we can fetch all applications if we just do list, or specifically by job.
            // Let's redirect to dashboard for now, or build a complex query.
            return "redirect:/dashboard";
        } else {
            model.addAttribute("applications", applicationService.findApplicationsByStudentId(userDetails.getUser().getId()));
            return "applications/list";
        }
    }
    
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        applicationService.updateApplicationStatus(id, status);
        Optional<Application> appOpt = applicationService.findApplicationById(id);
        if (appOpt.isPresent()) {
            return "redirect:/jobs/" + appOpt.get().getJob().getId();
        }
        return "redirect:/dashboard";
    }
}
