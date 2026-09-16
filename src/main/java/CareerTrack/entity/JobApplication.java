package CareerTrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.AssertTrue;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Column(nullable = false)
    private String jobTitle;
    @NotNull(message = "Application status is required")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private ApplicationStatus status;

    private LocalDate appliedDate;

    private LocalDate interviewDate;
    @jakarta.validation.constraints.Pattern(
            regexp = "^(https?://).+",
            message = "Job link must start with http:// or https://"
    )
    private String jobLink;

    @Column(length = 2000)
    private String notes;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // Default constructor
    public JobApplication() {
    }


    // Constructor
    public JobApplication(
            String companyName,
            String jobTitle,
            ApplicationStatus status,
            LocalDate appliedDate,
            LocalDate interviewDate,
            String jobLink,
            String notes) {

        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.status = status;
        this.appliedDate = appliedDate;
        this.interviewDate = interviewDate;
        this.jobLink = jobLink;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }


    // Automatically set createdAt
    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }


    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }


    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }


    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @AssertTrue(message = "Interview date cannot be before applied date")
    public boolean isInterviewDateValid() {
        if (appliedDate == null || interviewDate == null) {
            return true;
        }

        return !interviewDate.isBefore(appliedDate);
    }
}