package CareerTrack.service;

import CareerTrack.dto.ApplicationStatsResponse;
import CareerTrack.dto.JobApplicationResponse;
import CareerTrack.dto.UserResponse;
import CareerTrack.entity.ApplicationStatus;
import CareerTrack.entity.JobApplication;
import CareerTrack.entity.User;
import CareerTrack.repository.JobApplicationRepository;
import CareerTrack.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            UserRepository userRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
    }

    // Get applications for the current user
    public List<JobApplication> getAllApplications() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository.findByUserId(
                currentUser.getId());
    }

    // Get applications for the current user with pagination
    public Page<JobApplication> getApplicationsPaginated(
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository.findByUserId(
                currentUser.getId(),
                pageable);
    }

    // Get applications by user
    public List<JobApplication> getApplicationsByUser(
            Long userId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these applications");
        }

        return jobApplicationRepository.findByUserId(userId);
    }

    // Get applications by user with pagination
    public Page<JobApplication> getApplicationsByUserPaginated(
            Long userId,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these applications");
        }

        return jobApplicationRepository.findByUserId(
                userId,
                pageable);
    }

    // Get applications by user as response DTOs
    public List<JobApplicationResponse> getApplicationsByUserResponse(
            Long userId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these applications");
        }

        return jobApplicationRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get applications by status
    public List<JobApplication> getApplicationsByStatus(
            ApplicationStatus status) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository
                .findByUserIdAndStatus(
                        currentUser.getId(),
                        status);
    }

    // Get application by ID with ownership check
    public JobApplication getApplicationById(Long id) {

        return getApplicationForCurrentUser(id);
    }

    // Create application
    public JobApplication createApplication(
            Long userId,
            JobApplication application) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to create an application for this user");
        }

        application.setUser(currentUser);

        return jobApplicationRepository.save(application);
    }

    // Get application only if it belongs to current user
    private JobApplication getApplicationForCurrentUser(
            Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        JobApplication application =
                jobApplicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job application not found"));

        if (!application.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to access this application");
        }

        return application;
    }

    // Update application
    public JobApplication updateApplication(
            Long id,
            JobApplication applicationDetails) {

        JobApplication application =
                getApplicationForCurrentUser(id);

        application.setCompanyName(
                applicationDetails.getCompanyName());

        application.setJobTitle(
                applicationDetails.getJobTitle());

        application.setStatus(
                applicationDetails.getStatus());

        application.setAppliedDate(
                applicationDetails.getAppliedDate());

        application.setInterviewDate(
                applicationDetails.getInterviewDate());

        application.setJobLink(
                applicationDetails.getJobLink());

        application.setNotes(
                applicationDetails.getNotes());

        return jobApplicationRepository.save(application);
    }

    // Delete application
    public void deleteApplication(Long id) {

        JobApplication application =
                getApplicationForCurrentUser(id);

        jobApplicationRepository.delete(application);
    }

    // Search current user's applications by company name
    public List<JobApplication> searchByCompanyName(
            String companyName) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository
                .findByUserIdAndCompanyNameContainingIgnoreCase(
                        currentUser.getId(),
                        companyName);
    }

    // Search current user's applications by company name with pagination
    public Page<JobApplication> searchByCompanyNamePaginated(
            String companyName,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository
                .findByUserIdAndCompanyNameContainingIgnoreCase(
                        currentUser.getId(),
                        companyName,
                        pageable);
    }

    // Get applications by status with pagination
    public Page<JobApplication> getApplicationsByStatusPaginated(
            ApplicationStatus status,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return jobApplicationRepository
                .findByUserIdAndStatus(
                        currentUser.getId(),
                        status,
                        pageable);
    }

    // Get application count by status
    public long getApplicationCountByStatus(
            ApplicationStatus status) {

        return jobApplicationRepository
                .countByStatus(status);
    }

    // Get user-specific application count by status
    public long getUserApplicationCountByStatus(
            Long userId,
            ApplicationStatus status) {

        return jobApplicationRepository
                .countByUserIdAndStatus(
                        userId,
                        status);
    }

    // Get application statistics
    public ApplicationStatsResponse getApplicationStatistics() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        long applied =
                getUserApplicationCountByStatus(
                        currentUser.getId(),
                        ApplicationStatus.APPLIED);

        long assessment =
                getUserApplicationCountByStatus(
                        currentUser.getId(),
                        ApplicationStatus.ASSESSMENT);

        long interview =
                getUserApplicationCountByStatus(
                        currentUser.getId(),
                        ApplicationStatus.INTERVIEW);

        long offer =
                getUserApplicationCountByStatus(
                        currentUser.getId(),
                        ApplicationStatus.OFFER);

        long rejected =
                getUserApplicationCountByStatus(
                        currentUser.getId(),
                        ApplicationStatus.REJECTED);

        long totalApplications =
                applied
                        + assessment
                        + interview
                        + offer
                        + rejected;

        return new ApplicationStatsResponse(
                totalApplications,
                applied,
                assessment,
                interview,
                offer,
                rejected);
    }

    // Get user-specific application statistics
    public ApplicationStatsResponse
    getUserApplicationStatistics(Long userId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these statistics");
        }

        long applied =
                getUserApplicationCountByStatus(
                        userId,
                        ApplicationStatus.APPLIED);

        long assessment =
                getUserApplicationCountByStatus(
                        userId,
                        ApplicationStatus.ASSESSMENT);

        long interview =
                getUserApplicationCountByStatus(
                        userId,
                        ApplicationStatus.INTERVIEW);

        long offer =
                getUserApplicationCountByStatus(
                        userId,
                        ApplicationStatus.OFFER);

        long rejected =
                getUserApplicationCountByStatus(
                        userId,
                        ApplicationStatus.REJECTED);

        long totalApplications =
                applied
                        + assessment
                        + interview
                        + offer
                        + rejected;

        return new ApplicationStatsResponse(
                totalApplications,
                applied,
                assessment,
                interview,
                offer,
                rejected);
    }

    // Convert entity to response DTO
    public JobApplicationResponse convertToResponse(
            JobApplication application) {

        User user = application.getUser();

        UserResponse userResponse =
                new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail());

        return new JobApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getJobTitle(),
                application.getStatus(),
                application.getAppliedDate(),
                application.getInterviewDate(),
                application.getJobLink(),
                application.getNotes(),
                application.getCreatedAt(),
                userResponse);
    }

    // Get upcoming interviews for a user
    public List<JobApplication> getUpcomingInterviews(
            Long userId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these interviews");
        }

        return jobApplicationRepository
                .findByUserIdAndStatusAndInterviewDateGreaterThanEqual(
                        userId,
                        ApplicationStatus.INTERVIEW,
                        LocalDate.now());
    }

    // Get applications by date range
    public List<JobApplication> getApplicationsByDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these applications");
        }

        return jobApplicationRepository
                .findByUserIdAndAppliedDateBetween(
                        userId,
                        startDate,
                        endDate);
    }

    // Get applications by date range with pagination
    public Page<JobApplication> getApplicationsByDateRangePaginated(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException(
                    "You are not allowed to access these applications");
        }

        return jobApplicationRepository
                .findByUserIdAndAppliedDateBetween(
                        userId,
                        startDate,
                        endDate,
                        pageable);
    }
}