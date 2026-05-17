package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // 🔹 APPLY FOR JOB
    public Application applyForJob(Application application) {
        application.setStatus(ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }

    // 🔹 STUDENT APPLICATIONS
    public List<Application> findApplicationsByStudentId(Long studentId) {
        return applicationRepository.findByApplicantId(studentId);
    }

    // 🔹 JOB APPLICATIONS
    public List<Application> findApplicationsByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    // 🔹 FIND BY ID
    public Optional<Application> findApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    // 🔹 UPDATE STATUS
    public void updateApplicationStatus(Long id, ApplicationStatus status) {
        applicationRepository.findById(id).ifPresent(app -> {
            app.setStatus(status);
            applicationRepository.save(app);
        });
    }

    // ✅ CHECK IF USER ALREADY APPLIED
    public boolean hasUserApplied(Job job, User user) {
        return applicationRepository.existsByJobAndApplicant(job, user);
    }
}