package CareerTrack.repository;

import CareerTrack.entity.ApplicationStatus;
import CareerTrack.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // Get applications by user
    List<JobApplication> findByUserId(Long userId);

    // Get applications by user with pagination
    Page<JobApplication> findByUserId(
            Long userId,
            Pageable pageable);

    // Get applications by status
    Page<JobApplication> findByStatus(
            ApplicationStatus status,
            Pageable pageable);

    // Get applications by user and status
    List<JobApplication> findByUserIdAndStatus(
            Long userId,
            ApplicationStatus status);

    // Get applications by user and status with pagination
    Page<JobApplication> findByUserIdAndStatus(
            Long userId,
            ApplicationStatus status,
            Pageable pageable);

    // Search applications by company name
    List<JobApplication> findByCompanyNameContainingIgnoreCase(
            String companyName);

    // Search applications by company name with pagination
    Page<JobApplication> findByCompanyNameContainingIgnoreCase(
            String companyName,
            Pageable pageable);

    // Search applications by user's company name
    List<JobApplication> findByUserIdAndCompanyNameContainingIgnoreCase(
            Long userId,
            String companyName);

    // Search applications by user's company name with pagination
    Page<JobApplication> findByUserIdAndCompanyNameContainingIgnoreCase(
            Long userId,
            String companyName,
            Pageable pageable);

    // Count applications by status
    long countByStatus(ApplicationStatus status);

    // Count applications by user and status
    long countByUserIdAndStatus(
            Long userId,
            ApplicationStatus status);

    // Get upcoming interviews for a user
    List<JobApplication>
    findByUserIdAndStatusAndInterviewDateGreaterThanEqual(
            Long userId,
            ApplicationStatus status,
            LocalDate date);

    // Get applications by date range
    List<JobApplication> findByUserIdAndAppliedDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate);

    // Get applications by date range with pagination
    Page<JobApplication> findByUserIdAndAppliedDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
}