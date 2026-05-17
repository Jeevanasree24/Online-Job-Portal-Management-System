package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.Application;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobController {


private final JobService jobService;
private final ApplicationService applicationService;

public JobController(JobService jobService, ApplicationService applicationService) {
    this.jobService = jobService;
    this.applicationService = applicationService;
}

// 🔹 LIST JOBS
@GetMapping
public String listJobs(Model model, @RequestParam(required = false) String query) {
    if (query != null && !query.trim().isEmpty()) {
        model.addAttribute("jobs", jobService.searchJobs(query));
        model.addAttribute("searchQuery", query);
    } else {
        model.addAttribute("jobs", jobService.findAllJobs());
    }
    return "jobs/list";
}

// 🔹 VIEW JOB DETAILS
@GetMapping("/{id}")
public String viewJob(@PathVariable Long id,
                      Model model,
                      @AuthenticationPrincipal CustomUserDetails userDetails) {

    jobService.findJobById(id).ifPresent(job -> {
        model.addAttribute("job", job);

        if (userDetails != null) {
            var user = userDetails.getUser();

            // ✅ CHECK IF STUDENT HAS APPLIED
            boolean applied = applicationService.hasUserApplied(job, user);
            model.addAttribute("applied", applied);

            // ✅ If employer, show applications
            if (user.getId().equals(job.getEmployer().getId())) {
                model.addAttribute("applications",
                        applicationService.findApplicationsByJobId(id));
            }
        }
    });

    return "jobs/detail";
}

// 🔹 SHOW POST FORM
@GetMapping("/post")
public String showPostJobForm(Model model) {
    model.addAttribute("job", new Job());
    return "jobs/post";
}

// 🔹 CREATE JOB
@PostMapping("/post")
public String postJob(@Valid @ModelAttribute("job") Job job,
                      BindingResult result,
                      @AuthenticationPrincipal CustomUserDetails userDetails,
                      RedirectAttributes redirectAttributes) {

    if (userDetails == null) {
        return "redirect:/login";
    }

    job.setEmployer(userDetails.getUser());

    if (result.hasErrors()) {
        return "jobs/post";
    }

    jobService.saveJob(job);

    redirectAttributes.addFlashAttribute("success", true);
    return "redirect:/jobs";
}

// 🔹 EDIT FORM
@GetMapping("/edit/{id}")
public String editJobForm(@PathVariable Long id,
                          Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

    if (userDetails == null) {
        return "redirect:/login";
    }

    Job job = jobService.findJobById(id).orElseThrow();

    if (!job.getEmployer().getId().equals(userDetails.getUser().getId())) {
        return "redirect:/jobs";
    }

    model.addAttribute("job", job);
    return "jobs/post";
}

// 🔹 UPDATE JOB
@PostMapping("/edit/{id}")
public String updateJob(@PathVariable Long id,
                        @Valid @ModelAttribute("job") Job job,
                        BindingResult result,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        RedirectAttributes redirectAttributes) {

    if (userDetails == null) {
        return "redirect:/login";
    }

    Job existingJob = jobService.findJobById(id).orElseThrow();

    if (!existingJob.getEmployer().getId().equals(userDetails.getUser().getId())) {
        return "redirect:/jobs";
    }

    if (result.hasErrors()) {
        return "jobs/post";
    }

    job.setId(id);
    job.setEmployer(userDetails.getUser());

    jobService.saveJob(job);

    redirectAttributes.addFlashAttribute("updated", true);
    return "redirect:/jobs";
}

// 🔹 DELETE JOB
@PostMapping("/delete/{id}")
public String deleteJob(@PathVariable Long id,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        RedirectAttributes redirectAttributes) {

    if (userDetails == null) {
        return "redirect:/login";
    }

    Job job = jobService.findJobById(id).orElseThrow();

    if (!job.getEmployer().getId().equals(userDetails.getUser().getId())) {
        return "redirect:/jobs";
    }

    jobService.deleteJob(id);

    redirectAttributes.addFlashAttribute("deleted", true);
    return "redirect:/jobs";
}


}
