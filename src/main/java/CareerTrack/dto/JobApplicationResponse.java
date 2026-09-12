package CareerTrack.dto;

import CareerTrack.entity.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobApplicationResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate interviewDate;
    private String jobLink;
    private String notes;
    private LocalDateTime createdAt;
    private UserResponse user;

    public JobApplicationResponse() {
    }

    public JobApplicationResponse(
            Long id,
            String companyName,
            String jobTitle,
            ApplicationStatus status,
            LocalDate appliedDate,
            LocalDate interviewDate,
            String jobLink,
            String notes,
            LocalDateTime createdAt,
            UserResponse user) {

        this.id = id;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.status = status;
        this.appliedDate = appliedDate;
        this.interviewDate = interviewDate;
        this.jobLink = jobLink;
        this.notes = notes;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserResponse getUser() {
        return user;
    }
}