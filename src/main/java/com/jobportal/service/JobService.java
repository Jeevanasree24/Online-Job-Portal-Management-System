package com.jobportal.service;

import com.jobportal.model.Job;
import com.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> findJobById(Long id) {
        return jobRepository.findById(id);
    }
    
    public List<Job> searchJobs(String query) {
        return jobRepository.searchJobs(query);
    }
    
    public List<Job> findJobsByEmployerId(Long employerId) {
        // We will need a method in JobRepository: List<Job> findByEmployerId(Long employerId);
        // Let's assume we will add it to JobRepository
        return jobRepository.findByEmployerId(employerId);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
