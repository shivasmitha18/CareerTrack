package CareerTrack.controller;

import CareerTrack.dto.ApplicationStatsResponse;
import CareerTrack.dto.JobApplicationResponse;
import CareerTrack.entity.ApplicationStatus;
import CareerTrack.entity.JobApplication;
import CareerTrack.service.JobApplicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@SecurityRequirement(name = "bearerAuth")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;
    }

    // Get all applications
    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>>
    getAllApplications() {

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .getAllApplications()
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get applications by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplicationResponse>>
    getApplicationsByUser(
            @PathVariable Long userId) {

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByUser(userId)
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get user applications directly as DTO responses
    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<JobApplicationResponse>>
    getUserApplications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                jobApplicationService
                        .getApplicationsByUserResponse(userId));
    }

    // Get applications by user with pagination
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<JobApplicationResponse>>
    getApplicationsByUserPaginated(
            @PathVariable Long userId,
            Pageable pageable) {

        Page<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByUserPaginated(
                                userId,
                                pageable)
                        .map(jobApplicationService::convertToResponse);

        return ResponseEntity.ok(responses);
    }

    // Search applications by company name
    @GetMapping("/search")
    public ResponseEntity<List<JobApplicationResponse>>
    searchByCompanyName(
            @RequestParam String companyName) {

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .searchByCompanyName(companyName)
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Search applications by company name with pagination
    @GetMapping("/search/page")
    public ResponseEntity<Page<JobApplicationResponse>>
    searchByCompanyNamePaginated(
            @RequestParam String companyName,
            Pageable pageable) {

        Page<JobApplicationResponse> responses =
                jobApplicationService
                        .searchByCompanyNamePaginated(
                                companyName,
                                pageable)
                        .map(jobApplicationService::convertToResponse);

        return ResponseEntity.ok(responses);
    }

    // Get applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationResponse>>
    getApplicationsByStatus(
            @PathVariable String status) {

        ApplicationStatus applicationStatus;

        try {

            applicationStatus =
                    ApplicationStatus.valueOf(
                            status.toUpperCase());

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid application status: " + status);
        }

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByStatus(
                                applicationStatus)
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get applications by status with pagination
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<JobApplicationResponse>>
    getApplicationsByStatusPaginated(
            @PathVariable String status,
            Pageable pageable) {

        ApplicationStatus applicationStatus;

        try {

            applicationStatus =
                    ApplicationStatus.valueOf(
                            status.toUpperCase());

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid application status: " + status);
        }

        Page<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByStatusPaginated(
                                applicationStatus,
                                pageable)
                        .map(jobApplicationService::convertToResponse);

        return ResponseEntity.ok(responses);
    }

    // Get all applications with pagination
    @GetMapping("/page")
    public ResponseEntity<Page<JobApplicationResponse>>
    getApplicationsPaginated(
            Pageable pageable) {

        Page<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsPaginated(pageable)
                        .map(jobApplicationService::convertToResponse);

        return ResponseEntity.ok(responses);
    }

    // Get overall application statistics
    @GetMapping("/stats")
    public ResponseEntity<ApplicationStatsResponse>
    getApplicationStatistics() {

        ApplicationStatsResponse statistics =
                jobApplicationService
                        .getApplicationStatistics();

        return ResponseEntity.ok(statistics);
    }

    // Get user-specific application statistics
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<ApplicationStatsResponse>
    getUserApplicationStatistics(
            @PathVariable Long userId) {

        ApplicationStatsResponse statistics =
                jobApplicationService
                        .getUserApplicationStatistics(userId);

        return ResponseEntity.ok(statistics);
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse>
    getApplicationById(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .getApplicationById(id);

        return ResponseEntity.ok(
                jobApplicationService
                        .convertToResponse(application));
    }

    // Create application
    @PostMapping("/user/{userId}")
    public ResponseEntity<JobApplicationResponse>
    createApplication(
            @PathVariable Long userId,
            @Valid @RequestBody JobApplication application) {

        JobApplication savedApplication =
                jobApplicationService
                        .createApplication(
                                userId,
                                application);

        JobApplicationResponse response =
                jobApplicationService
                        .convertToResponse(
                                savedApplication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Update application
    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse>
    updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplication applicationDetails) {

        JobApplication updatedApplication =
                jobApplicationService
                        .updateApplication(
                                id,
                                applicationDetails);

        JobApplicationResponse response =
                jobApplicationService
                        .convertToResponse(
                                updatedApplication);

        return ResponseEntity.ok(response);
    }

    // Delete application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteApplication(
            @PathVariable Long id) {

        jobApplicationService.deleteApplication(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // Get upcoming interviews
    @GetMapping("/user/{userId}/upcoming-interviews")
    public ResponseEntity<List<JobApplicationResponse>>
    getUpcomingInterviews(
            @PathVariable Long userId) {

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .getUpcomingInterviews(userId)
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get applications by date range
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<JobApplicationResponse>>
    getApplicationsByDateRange(
            @PathVariable Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByDateRange(
                                userId,
                                startDate,
                                endDate)
                        .stream()
                        .map(jobApplicationService::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Get applications by date range with pagination
    @GetMapping("/user/{userId}/date-range/page")
    public ResponseEntity<Page<JobApplicationResponse>>
    getApplicationsByDateRangePaginated(
            @PathVariable Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Pageable pageable) {

        Page<JobApplicationResponse> responses =
                jobApplicationService
                        .getApplicationsByDateRangePaginated(
                                userId,
                                startDate,
                                endDate,
                                pageable)
                        .map(jobApplicationService::convertToResponse);

        return ResponseEntity.ok(responses);
    }
}