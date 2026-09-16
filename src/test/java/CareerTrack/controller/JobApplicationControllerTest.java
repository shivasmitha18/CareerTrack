package CareerTrack.controller;

import CareerTrack.dto.ApplicationStatsResponse;
import CareerTrack.dto.JobApplicationResponse;
import CareerTrack.entity.ApplicationStatus;
import CareerTrack.entity.JobApplication;
import CareerTrack.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JobApplicationControllerTest {


    @Mock
    private JobApplicationService jobApplicationService;


    @Test
    void createApplication_shouldReturn201Created() {

        JobApplication application =
                new JobApplication();

        JobApplicationResponse responseDto =
                new JobApplicationResponse();

        when(jobApplicationService.createApplication(
                anyLong(),
                any(JobApplication.class)
        )).thenReturn(application);

        when(jobApplicationService.convertToResponse(
                application
        )).thenReturn(responseDto);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<JobApplicationResponse> response =
                controller.createApplication(
                        1L,
                        application
                );

        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .createApplication(
                        1L,
                        application
                );

        verify(jobApplicationService)
                .convertToResponse(
                        application
                );
    }


    @Test
    void updateApplication_shouldReturn200Ok() {

        JobApplication application =
                new JobApplication();

        JobApplicationResponse responseDto =
                new JobApplicationResponse();

        when(jobApplicationService.updateApplication(
                anyLong(),
                any(JobApplication.class)
        )).thenReturn(application);

        when(jobApplicationService.convertToResponse(
                application
        )).thenReturn(responseDto);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<JobApplicationResponse> response =
                controller.updateApplication(
                        1L,
                        application
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .updateApplication(
                        1L,
                        application
                );

        verify(jobApplicationService)
                .convertToResponse(
                        application
                );
    }


    @Test
    void deleteApplication_shouldReturn204NoContent() {

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Void> response =
                controller.deleteApplication(1L);

        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );

        verify(jobApplicationService)
                .deleteApplication(1L);
    }


    @Test
    void getApplicationById_shouldReturn200Ok() {

        JobApplication application =
                new JobApplication();

        JobApplicationResponse responseDto =
                new JobApplicationResponse();

        when(jobApplicationService.getApplicationById(
                1L
        )).thenReturn(application);

        when(jobApplicationService.convertToResponse(
                application
        )).thenReturn(responseDto);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<JobApplicationResponse> response =
                controller.getApplicationById(1L);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationById(1L);

        verify(jobApplicationService)
                .convertToResponse(
                        application
                );
    }


    @Test
    void getAllApplications_shouldReturn200Ok() {

        when(jobApplicationService.getAllApplications())
                .thenReturn(
                        Collections.emptyList()
                );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.getAllApplications();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getAllApplications();
    }


    @Test
    void getApplicationsByUser_shouldReturn200Ok() {

        when(jobApplicationService.getApplicationsByUser(
                1L
        )).thenReturn(
                Collections.emptyList()
        );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.getApplicationsByUser(1L);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByUser(1L);
    }


    @Test
    void getApplicationsByStatus_shouldReturn200Ok() {

        when(jobApplicationService.getApplicationsByStatus(
                ApplicationStatus.INTERVIEW
        )).thenReturn(
                Collections.emptyList()
        );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.getApplicationsByStatus(
                        "INTERVIEW"
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByStatus(
                        ApplicationStatus.INTERVIEW
                );
    }


    @Test
    void getApplicationsByStatus_shouldRejectInvalidStatus() {

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        try {

            controller.getApplicationsByStatus(
                    "INVALID_STATUS"
            );

        } catch (IllegalArgumentException exception) {

            assertEquals(
                    "Invalid application status: INVALID_STATUS",
                    exception.getMessage()
            );

            return;
        }

        throw new AssertionError(
                "Expected IllegalArgumentException"
        );
    }


    @Test
    void searchByCompanyName_shouldReturn200Ok() {

        when(jobApplicationService.searchByCompanyName(
                "TCS"
        )).thenReturn(
                Collections.emptyList()
        );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.searchByCompanyName("TCS");

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .searchByCompanyName("TCS");
    }


    @Test
    void getApplicationStatistics_shouldReturn200Ok() {

        ApplicationStatsResponse statistics =
                new ApplicationStatsResponse();

        when(jobApplicationService.getApplicationStatistics())
                .thenReturn(statistics);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<ApplicationStatsResponse> response =
                controller.getApplicationStatistics();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationStatistics();
    }


    @Test
    void getUpcomingInterviews_shouldReturn200Ok() {

        when(jobApplicationService.getUpcomingInterviews(
                1L
        )).thenReturn(
                Collections.emptyList()
        );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.getUpcomingInterviews(1L);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getUpcomingInterviews(1L);
    }


    @Test
    void getApplicationsByStatusPaginated_shouldReturn200Ok() {

        Page<JobApplication> page =
                Page.empty();

        Pageable pageable =
                Pageable.ofSize(10);

        when(jobApplicationService.getApplicationsByStatusPaginated(
                eq(ApplicationStatus.INTERVIEW),
                eq(pageable)
        )).thenReturn(page);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Page<JobApplicationResponse>> response =
                controller.getApplicationsByStatusPaginated(
                        "INTERVIEW",
                        pageable
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByStatusPaginated(
                        eq(ApplicationStatus.INTERVIEW),
                        eq(pageable)
                );
    }


    @Test
    void getApplicationsPaginated_shouldReturn200Ok() {

        Page<JobApplication> page =
                Page.empty();

        Pageable pageable =
                Pageable.ofSize(10);

        when(jobApplicationService.getApplicationsPaginated(
                eq(pageable)
        )).thenReturn(page);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Page<JobApplicationResponse>> response =
                controller.getApplicationsPaginated(
                        pageable
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsPaginated(
                        eq(pageable)
                );
    }


    @Test
    void getApplicationsByUserPaginated_shouldReturn200Ok() {

        Page<JobApplication> page =
                Page.empty();

        Pageable pageable =
                Pageable.ofSize(10);

        when(jobApplicationService.getApplicationsByUserPaginated(
                eq(1L),
                eq(pageable)
        )).thenReturn(page);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Page<JobApplicationResponse>> response =
                controller.getApplicationsByUserPaginated(
                        1L,
                        pageable
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByUserPaginated(
                        eq(1L),
                        eq(pageable)
                );
    }


    @Test
    void searchByCompanyNamePaginated_shouldReturn200Ok() {

        Page<JobApplication> page =
                Page.empty();

        Pageable pageable =
                Pageable.ofSize(10);

        when(jobApplicationService.searchByCompanyNamePaginated(
                eq("TCS"),
                eq(pageable)
        )).thenReturn(page);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Page<JobApplicationResponse>> response =
                controller.searchByCompanyNamePaginated(
                        "TCS",
                        pageable
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .searchByCompanyNamePaginated(
                        eq("TCS"),
                        eq(pageable)
                );
    }


    @Test
    void getUserApplicationStatistics_shouldReturn200Ok() {

        ApplicationStatsResponse statistics =
                new ApplicationStatsResponse();

        when(jobApplicationService.getUserApplicationStatistics(
                1L
        )).thenReturn(statistics);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<ApplicationStatsResponse> response =
                controller.getUserApplicationStatistics(1L);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getUserApplicationStatistics(1L);
    }


    @Test
    void getApplicationsByDateRange_shouldReturn200Ok() {

        LocalDate startDate =
                LocalDate.of(2026, 9, 1);

        LocalDate endDate =
                LocalDate.of(2026, 9, 30);

        when(jobApplicationService.getApplicationsByDateRange(
                eq(1L),
                eq(startDate),
                eq(endDate)
        )).thenReturn(
                Collections.emptyList()
        );

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<List<JobApplicationResponse>> response =
                controller.getApplicationsByDateRange(
                        1L,
                        startDate,
                        endDate
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByDateRange(
                        eq(1L),
                        eq(startDate),
                        eq(endDate)
                );
    }


    @Test
    void getApplicationsByDateRangePaginated_shouldReturn200Ok() {

        Page<JobApplication> page =
                Page.empty();

        Pageable pageable =
                Pageable.ofSize(10);

        LocalDate startDate =
                LocalDate.of(2026, 9, 1);

        LocalDate endDate =
                LocalDate.of(2026, 9, 30);

        when(jobApplicationService.getApplicationsByDateRangePaginated(
                eq(1L),
                eq(startDate),
                eq(endDate),
                eq(pageable)
        )).thenReturn(page);

        JobApplicationController controller =
                new JobApplicationController(
                        jobApplicationService
                );

        ResponseEntity<Page<JobApplicationResponse>> response =
                controller.getApplicationsByDateRangePaginated(
                        1L,
                        startDate,
                        endDate,
                        pageable
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(
                response.getBody()
        );

        verify(jobApplicationService)
                .getApplicationsByDateRangePaginated(
                        eq(1L),
                        eq(startDate),
                        eq(endDate),
                        eq(pageable)
                );
    }

}