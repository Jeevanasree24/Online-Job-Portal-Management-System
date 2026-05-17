package com.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Future;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotBlank
@Column(nullable = false)
private String title;

@NotBlank
@Column(columnDefinition = "TEXT", nullable = false)
private String description;

@NotBlank
private String skillsRequired;

@PositiveOrZero
private Double salary;

@NotBlank
private String category;

@NotBlank
private String location;

@NotBlank
private String experience;

// ✅ FIXED: removed @NotNull (important)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "employer_id", nullable = false)
private User employer;

@OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
private List<Application> applications;

private LocalDateTime postedAt;

// ✅ NEW: Application Deadline
@Future(message = "Deadline must be in the future")
@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
private LocalDateTime applicationDeadline;

@PrePersist
protected void onCreate() {
    postedAt = LocalDateTime.now();
}

// 🔹 Getters & Setters

public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }

public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }

public String getSkillsRequired() { return skillsRequired; }
public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }

public Double getSalary() { return salary; }
public void setSalary(Double salary) { this.salary = salary; }

public String getCategory() { return category; }
public void setCategory(String category) { this.category = category; }

public String getLocation() { return location; }
public void setLocation(String location) { this.location = location; }

public String getExperience() { return experience; }
public void setExperience(String experience) { this.experience = experience; }

public User getEmployer() { return employer; }
public void setEmployer(User employer) { this.employer = employer; }

public List<Application> getApplications() { return applications; }
public void setApplications(List<Application> applications) { this.applications = applications; }

public LocalDateTime getPostedAt() { return postedAt; }
public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

public LocalDateTime getApplicationDeadline() { return applicationDeadline; }
public void setApplicationDeadline(LocalDateTime applicationDeadline) { this.applicationDeadline = applicationDeadline; }


}
