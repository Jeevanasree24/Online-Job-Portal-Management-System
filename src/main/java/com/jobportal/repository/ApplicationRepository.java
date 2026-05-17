package com.jobportal.repository;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicant(User applicant);
    List<Application> findByApplicantId(Long applicantId);
    List<Application> findByJob(Job job);
    List<Application> findByJobId(Long jobId);
    boolean existsByJobAndApplicant(Job job, User applicant);
}
