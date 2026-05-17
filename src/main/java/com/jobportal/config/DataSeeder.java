package com.jobportal.config;

import com.jobportal.model.Job;
import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, JobRepository jobRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                // Create Admin
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setName("System Administrator");
                admin.setEmail("admin@elevate.com");
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);

                // Create Employer
                User employer = new User();
                employer.setUsername("employer");
                employer.setPassword(passwordEncoder.encode("employer"));
                employer.setName("Tech Corp Inc.");
                employer.setEmail("hr@techcorp.com");
                employer.setRole(Role.ROLE_EMPLOYER);
                userRepository.save(employer);

                // Create Student
                User student = new User();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("student"));
                student.setName("Alice Smith");
                student.setEmail("alice@student.com");
                student.setRole(Role.ROLE_STUDENT);
                userRepository.save(student);

                // Create a sample job
                Job job1 = new Job();
                job1.setTitle("Software Engineer Intern");
                job1.setCategory("Engineering");
                job1.setLocation("Remote");
                job1.setSalary(45.0);
                job1.setExperience("Intern");
                job1.setDescription("We are looking for a passionate Software Engineer Intern to join our dynamic team.");
                job1.setSkillsRequired("Knowledge of Java, Spring Boot, and basic web technologies. Currently enrolled in a CS degree program.");
                job1.setEmployer(employer);
                jobRepository.save(job1);

                Job job2 = new Job();
                job2.setTitle("Marketing Manager");
                job2.setCategory("Marketing");
                job2.setLocation("New York, NY");
                job2.setSalary(120000.0);
                job2.setExperience("5+ years");
                job2.setDescription("Lead our marketing efforts and drive customer acquisition.");
                job2.setSkillsRequired("Experience in B2B marketing. Strong analytical skills.");
                job2.setEmployer(employer);
                jobRepository.save(job2);
            }
        };
    }
}
