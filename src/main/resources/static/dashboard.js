// =========================
// CareerTrack Dashboard
// =========================


// =========================
// Authentication
// =========================

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/index.html";
}


// =========================
// API Configuration
// =========================

const API_BASE_URL = "/api";


// =========================
// DOM Elements
// =========================

const applicationsList =
    document.getElementById("applicationsList");

const applicationForm =
    document.getElementById("applicationForm");

const applicationFormContainer =
    document.getElementById("applicationFormContainer");

const addApplicationButton =
    document.getElementById("addApplicationButton");

const logoutButton =
    document.getElementById("logoutButton");

const searchInput =
    document.getElementById("searchInput");

const filterStatus =
    document.getElementById("filterStatus");

const sortApplications =
    document.getElementById("sortApplications");

const applicationMessage =
    document.getElementById("applicationMessage");

const companyNameInput =
    document.getElementById("companyName");

const jobTitleInput =
    document.getElementById("jobTitle");

const statusInput =
    document.getElementById("status");

const appliedDateInput =
    document.getElementById("appliedDate");

const interviewDateInput =
    document.getElementById("interviewDate");

const interviewDateGroup =
    document.getElementById("interviewDateGroup");

const jobLinkInput =
    document.getElementById("jobLink");

const notesInput =
    document.getElementById("notes");


// =========================
// Application State
// =========================

let allApplications = [];

let editingApplicationId = null;

let applicationStatusChart = null;


// =========================
// API Headers
// =========================

function getAuthHeaders() {

    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
}


// =========================
// Load Dashboard
// =========================

async function loadDashboard() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/applications`,
                {
                    method: "GET",
                    headers: getAuthHeaders()
                }
            );


        if (response.status === 401) {

            localStorage.removeItem("token");

            window.location.href =
                "/index.html";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load applications"
            );
        }


        const applications =
            await response.json();


        // Remove loading message
        const loadingState =
            document.querySelector(
                ".loading-state"
            );

        if (loadingState) {
            loadingState.remove();
        }


        allApplications =
            Array.isArray(applications)
                ? applications
                : [];


        updateStatistics(
            allApplications
        );


        updateCareerProgress(
            allApplications
        );


        updateSuccessRate(
            allApplications
        );


        updateTotalOffers(
            allApplications
        );


        updateResponseRate(
            allApplications
        );


        updateApplicationChart(
            allApplications
        );


        filterAndSortApplications();


        await loadProfile();


        await loadUpcomingInterviews();

    } catch (error) {

        console.error(
            "Dashboard loading error:",
            error
        );


        showDashboardError(
            "Unable to load dashboard data. Please refresh the page."
        );
    }
}


// =========================
// Dashboard Error
function showDashboardError(message) {

    if (!applicationsList) {
        return;
    }

    applicationsList.innerHTML = `
        <div class="error-state">
            <div class="error-icon">⚠️</div>
            <h3>Something went wrong</h3>
            <p>${message}</p>
            <button
                type="button"
                id="retryButton">
                Try Again
            </button>
        </div>
    `;

    const retryButton =
        document.getElementById("retryButton");

    if (retryButton) {
        retryButton.addEventListener(
            "click",
            () => {

                applicationsList.innerHTML = `
                    <div class="loading-state">
                        <div class="loading-spinner"></div>
                        <p>Loading applications...</p>
                    </div>
                `;

                loadDashboard();
            }
        );
    }
}

// =========================
// Load User Profile
// =========================

async function loadProfile() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/users/me`,
                {
                    headers: getAuthHeaders()
                }
            );


        if (response.status === 401) {

            localStorage.removeItem("token");

            window.location.href =
                "/index.html";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load profile"
            );
        }


        const user =
            await response.json();


        const profileName =
            document.getElementById(
                "profileName"
            );

        const profileEmail =
            document.getElementById(
                "profileEmail"
            );


        if (profileName) {

            profileName.textContent =
                user.name || "User";
        }


        if (profileEmail) {

            profileEmail.textContent =
                user.email || "-";
        }

    } catch (error) {

        console.error(
            "Profile loading error:",
            error
        );


        const profileName =
            document.getElementById(
                "profileName"
            );

        const profileEmail =
            document.getElementById(
                "profileEmail"
            );


        if (profileName) {
            profileName.textContent =
                "Unable to load";
        }


        if (profileEmail) {
            profileEmail.textContent =
                "Unable to load";
        }
    }
}


// =========================
// Statistics
// =========================

function updateStatistics(
    applications
) {

    const total =
        applications.length;


    const applied =
        applications.filter(
            application =>
                application.status === "APPLIED"
        ).length;


    const interviews =
        applications.filter(
            application =>
                application.status === "INTERVIEW"
        ).length;


    const offers =
        applications.filter(
            application =>
                application.status === "OFFER"
        ).length;


    const rejected =
        applications.filter(
            application =>
                application.status === "REJECTED"
        ).length;


    setText(
        "totalApplications",
        total
    );


    setText(
        "appliedApplications",
        applied
    );


    setText(
        "interviewApplications",
        interviews
    );


    setText(
        "offerApplications",
        offers
    );


    setText(
        "rejectedApplications",
        rejected
    );
}


// =========================
// Helper: Set Text
// =========================

function setText(
    elementId,
    value
) {

    const element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.textContent =
            value;
    }
}


// =========================
// Career Progress
// =========================

function updateCareerProgress(
    applications
) {

    const progressPercentage =
        document.getElementById(
            "progressPercentage"
        );

    const progressFill =
        document.getElementById(
            "progressFill"
        );


    const total =
        applications.length;


    let progress = 0;


    if (total > 0) {

        const applied =
            applications.filter(
                application =>
                    application.status === "APPLIED"
            ).length;

        const assessment =
            applications.filter(
                application =>
                    application.status === "ASSESSMENT"
            ).length;

        const interview =
            applications.filter(
                application =>
                    application.status === "INTERVIEW"
            ).length;

        const offer =
            applications.filter(
                application =>
                    application.status === "OFFER"
            ).length;

        const rejected =
            applications.filter(
                application =>
                    application.status === "REJECTED"
            ).length;


        /*
         * Career progress:
         *
         * Applied      = 20%
         * Assessment   = 40%
         * Interview    = 60%
         * Offer        = 100%
         * Rejected     = 0%
         */


        let totalProgress = 0;


        totalProgress +=
            applied * 20;


        totalProgress +=
            assessment * 40;


        totalProgress +=
            interview * 60;


        totalProgress +=
            offer * 100;


        totalProgress +=
            rejected * 0;


        progress =
            Math.round(
                totalProgress / total
            );


        if (progress > 100) {
            progress = 100;
        }
    }


    if (progressPercentage) {

        progressPercentage.textContent =
            `${progress}%`;
    }


    if (progressFill) {

        progressFill.style.width =
            `${progress}%`;
    }
}


// =========================
// Success Rate
// =========================

function updateSuccessRate(
    applications
) {

    const successRateElement =
        document.getElementById(
            "successRate"
        );


    if (!successRateElement) {
        return;
    }


    const total =
        applications.length;


    if (total === 0) {

        successRateElement.textContent =
            "0%";

        return;
    }


    const successfulApplications =
        applications.filter(
            application =>
                application.status === "INTERVIEW" ||
                application.status === "OFFER"
        ).length;


    const successRate =
        Math.round(
            (
                successfulApplications /
                total
            ) * 100
        );


    successRateElement.textContent =
        `${successRate}%`;
}


// =========================
// Total Offers
// =========================

function updateTotalOffers(
    applications
) {

    const totalOffersElement =
        document.getElementById(
            "totalOffers"
        );


    if (!totalOffersElement) {
        return;
    }


    const totalOffers =
        applications.filter(
            application =>
                application.status === "OFFER"
        ).length;


    totalOffersElement.textContent =
        totalOffers;
}


// =========================
// Response Rate
// =========================

function updateResponseRate(
    applications
) {

    const responseRateElement =
        document.getElementById(
            "responseRate"
        );


    if (!responseRateElement) {
        return;
    }


    const totalApplications =
        applications.length;


    if (totalApplications === 0) {

        responseRateElement.textContent =
            "0%";

        return;
    }


    /*
     * An application is considered
     * responded when its status moves
     * beyond APPLIED.
     */

    const respondedApplications =
        applications.filter(
            application =>
                application.status === "ASSESSMENT" ||
                application.status === "INTERVIEW" ||
                application.status === "OFFER" ||
                application.status === "REJECTED"
        ).length;


    const responseRate =
        Math.round(
            (
                respondedApplications /
                totalApplications
            ) * 100
        );


    responseRateElement.textContent =
        `${responseRate}%`;
}


// =========================
// Application Chart
// =========================

function updateApplicationChart(
    applications
) {

    const canvas =
        document.getElementById(
            "applicationStatusChart"
        );


    if (!canvas) {
        return;
    }


    const applied =
        applications.filter(
            application =>
                application.status === "APPLIED"
        ).length;


    const assessment =
        applications.filter(
            application =>
                application.status === "ASSESSMENT"
        ).length;


    const interview =
        applications.filter(
            application =>
                application.status === "INTERVIEW"
        ).length;


    const offer =
        applications.filter(
            application =>
                application.status === "OFFER"
        ).length;


    const rejected =
        applications.filter(
            application =>
                application.status === "REJECTED"
        ).length;


    if (
        typeof Chart === "undefined"
    ) {

        console.warn(
            "Chart.js is not loaded."
        );

        return;
    }


    if (applicationStatusChart) {

        applicationStatusChart.destroy();
    }


    applicationStatusChart =
        new Chart(
            canvas,
            {
                type: "doughnut",

                data: {

                    labels: [
                        "Applied",
                        "Assessment",
                        "Interview",
                        "Offer",
                        "Rejected"
                    ],

                    datasets: [
                        {
                            data: [
                                applied,
                                assessment,
                                interview,
                                offer,
                                rejected
                            ]
                        }
                    ]
                },

                options: {

                    responsive: true,

                    maintainAspectRatio: false,

                    plugins: {

                        legend: {
                            position: "bottom"
                        }
                    }
                }
            }
        );
}


// =========================
// Filter + Sort
// =========================

function filterAndSortApplications() {

    if (!applicationsList) {
        return;
    }


    let filteredApplications =
        [...allApplications];


    const searchTerm =
        searchInput
            ? searchInput.value
                .trim()
                .toLowerCase()
            : "";


    const selectedStatus =
        filterStatus
            ? filterStatus.value
            : "ALL";


    const selectedSort =
        sortApplications
            ? sortApplications.value
            : "NEWEST";


    // Search
    if (searchTerm) {

        filteredApplications =
            filteredApplications.filter(
                application => {

                    const company =
                        application.companyName
                            ? application.companyName
                                .toLowerCase()
                            : "";

                    const jobTitle =
                        application.jobTitle
                            ? application.jobTitle
                                .toLowerCase()
                            : "";


                    return (
                        company.includes(searchTerm) ||
                        jobTitle.includes(searchTerm)
                    );
                }
            );
    }


    // Filter
    if (
        selectedStatus &&
        selectedStatus !== "ALL"
    ) {

        filteredApplications =
            filteredApplications.filter(
                application =>
                    application.status ===
                    selectedStatus
            );
    }


    // Sort
    filteredApplications.sort(
        (a, b) => {

            if (
                selectedSort ===
                "COMPANY_ASC"
            ) {

                return (
                    (a.companyName || "")
                        .localeCompare(
                            b.companyName || ""
                        )
                );
            }


            if (
                selectedSort ===
                "COMPANY_DESC"
            ) {

                return (
                    (b.companyName || "")
                        .localeCompare(
                            a.companyName || ""
                        )
                );
            }


            const dateA =
                a.appliedDate
                    ? new Date(a.appliedDate)
                    : new Date(0);


            const dateB =
                b.appliedDate
                    ? new Date(b.appliedDate)
                    : new Date(0);


            if (
                selectedSort ===
                "OLDEST"
            ) {

                return dateA - dateB;
            }


            return dateB - dateA;
        }
    );


    renderApplications(
        filteredApplications
    );
}


// =========================
// Render Applications
// =========================

function renderApplications(
    applications
) {

    if (!applicationsList) {
        return;
    }


    if (applications.length === 0) {

        applicationsList.innerHTML = `
            <div class="empty-state">
                <h3>No applications found</h3>
                <p>
                    Add your first job application
                    to start tracking your progress.
                </p>
            </div>
        `;

        return;
    }


    applicationsList.innerHTML =
        applications
            .map(
                application =>
                    createApplicationCard(
                        application
                    )
            )
            .join("");


    attachApplicationCardEvents();
}


// =========================
// Application Card
// =========================

function createApplicationCard(
    application
) {

    const status =
        application.status || "APPLIED";


    const statusLabel =
        formatStatus(status);


    const appliedDate =
        formatDate(
            application.appliedDate
        );


    const interviewDate =
        formatDate(
            application.interviewDate
        );


    const jobLink =
        application.jobLink
            ? `
                <a
                    href="${escapeHtml(
                application.jobLink
            )}"
                    target="_blank"
                    rel="noopener noreferrer">
                    View Job
                </a>
            `
            : "";


    return `
        <div
            class="application-card"
            data-id="${application.id}">

            <div class="application-card-header">

                <div>
                    <h3>
                        ${escapeHtml(
        application.companyName ||
        "Unknown Company"
    )}
                    </h3>

                    <p>
                        ${escapeHtml(
        application.jobTitle ||
        "Unknown Role"
    )}
                    </p>
                </div>

                <span
                    class="status-badge status-${status.toLowerCase()}">
                    ${statusLabel}
                </span>

            </div>


            <div class="application-card-details">

                <p>
                    <strong>Applied:</strong>
                    ${appliedDate}
                </p>


                ${
        application.interviewDate
            ? `
                            <p>
                                <strong>Interview:</strong>
                                ${interviewDate}
                            </p>
                        `
            : ""
    }


                ${
        application.notes
            ? `
                            <p>
                                <strong>Notes:</strong>
                                ${escapeHtml(
                application.notes
            )}
                            </p>
                        `
            : ""
    }

            </div>


            <div class="application-card-actions">

                ${jobLink}

                <button
                    type="button"
                    class="edit-application-button"
                    data-id="${application.id}">
                    Edit
                </button>

                <button
                    type="button"
                    class="delete-application-button"
                    data-id="${application.id}">
                    Delete
                </button>

            </div>

        </div>
    `;
}


// =========================
// Attach Card Events
// =========================

function attachApplicationCardEvents() {

    const editButtons =
        document.querySelectorAll(
            ".edit-application-button"
        );


    editButtons.forEach(
        button => {

            button.addEventListener(
                "click",
                () => {

                    const id =
                        Number(
                            button.dataset.id
                        );

                    startEditApplication(id);
                }
            );
        }
    );


    const deleteButtons =
        document.querySelectorAll(
            ".delete-application-button"
        );


    deleteButtons.forEach(
        button => {

            button.addEventListener(
                "click",
                () => {

                    const id =
                        Number(
                            button.dataset.id
                        );

                    deleteApplication(id);
                }
            );
        }
    );
}


// =========================
// Add Application Button
// =========================

if (addApplicationButton) {

    addApplicationButton.addEventListener(
        "click",
        () => {

            resetApplicationForm();

            if (applicationFormContainer) {

                applicationFormContainer.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            }
        }
    );
}


// =========================
// Status Change
// =========================

if (statusInput) {

    statusInput.addEventListener(
        "change",
        updateInterviewDateVisibility
    );
}


// =========================
// Interview Date Visibility
// =========================

function updateInterviewDateVisibility() {

    if (
        !interviewDateGroup ||
        !interviewDateInput ||
        !statusInput
    ) {
        return;
    }


    if (
        statusInput.value ===
        "INTERVIEW"
    ) {

        interviewDateGroup.style.display =
            "block";

        interviewDateInput.required =
            true;

    } else {

        interviewDateGroup.style.display =
            "none";

        interviewDateInput.required =
            false;

        interviewDateInput.value =
            "";
    }
}


// =========================
// Application Form Submit
// =========================

if (applicationForm) {

    applicationForm.addEventListener(
        "submit",
        async event => {

            event.preventDefault();


            await saveApplication();
        }
    );
}


// =========================
// Save Application
// =========================

async function saveApplication() {

    if (!applicationForm) {
        return;
    }


    clearApplicationMessage();


    const companyName =
        companyNameInput.value.trim();


    const jobTitle =
        jobTitleInput.value.trim();


    const status =
        statusInput.value;


    const appliedDate =
        appliedDateInput.value || null;


    const interviewDate =
        status === "INTERVIEW"
            ? interviewDateInput.value || null
            : null;


    const jobLink =
        jobLinkInput.value.trim();


    const notes =
        notesInput.value.trim();


    if (!companyName) {

        showApplicationMessage(
            "Company name is required.",
            true
        );

        return;
    }


    if (!jobTitle) {

        showApplicationMessage(
            "Job title is required.",
            true
        );

        return;
    }


    if (!status) {

        showApplicationMessage(
            "Please select an application status.",
            true
        );

        return;
    }


    if (
        status === "INTERVIEW" &&
        !interviewDate
    ) {

        showApplicationMessage(
            "Please select an interview date.",
            true
        );

        return;
    }


    const applicationData = {

        companyName:
        companyName,

        jobTitle:
        jobTitle,

        status:
        status,

        appliedDate:
        appliedDate,

        interviewDate:
        interviewDate,

        jobLink:
            jobLink || null,

        notes:
            notes || null
    };


    try {

        let url;

        let method;


        if (editingApplicationId) {

            url =
                `${API_BASE_URL}/applications/${editingApplicationId}`;

            method =
                "PUT";

        } else {

            const userId =
                await getCurrentUserId();


            if (!userId) {

                throw new Error(
                    "Unable to identify current user."
                );
            }


            url =
                `${API_BASE_URL}/applications/user/${userId}`;

            method =
                "POST";
        }


        const response =
            await fetch(
                url,
                {
                    method:
                    method,

                    headers:
                        getAuthHeaders(),

                    body:
                        JSON.stringify(
                            applicationData
                        )
                }
            );


        if (response.status === 401) {

            localStorage.removeItem("token");

            window.location.href =
                "/index.html";

            return;
        }


        const responseData =
            await response.json()
                .catch(
                    () => null
                );


        if (!response.ok) {

            let message =
                "Unable to save application.";


            if (
                responseData &&
                responseData.message
            ) {

                message =
                    responseData.message;

            } else if (
                responseData &&
                responseData.error
            ) {

                message =
                    responseData.error;
            }


            throw new Error(message);
        }


        showApplicationMessage(
            editingApplicationId
                ? "Application updated successfully."
                : "Application added successfully.",
            false
        );


        resetApplicationForm();


        await loadDashboard();

    } catch (error) {

        console.error(
            "Save application error:",
            error
        );


        showApplicationMessage(
            error.message ||
            "Unable to save application.",
            true
        );
    }
}


// =========================
// Get Current User ID
// =========================

async function getCurrentUserId() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/users/me`,
                {
                    method: "GET",
                    headers: getAuthHeaders()
                }
            );


        if (!response.ok) {
            return null;
        }


        const user =
            await response.json();


        return user.id;

    } catch (error) {

        console.error(
            "Get user ID error:",
            error
        );

        return null;
    }
}


// =========================
// Start Edit
// =========================

function startEditApplication(id) {

    const application =
        allApplications.find(
            item =>
                Number(item.id) ===
                Number(id)
        );


    if (!application) {

        console.error(
            "Application not found:",
            id
        );

        return;
    }


    editingApplicationId =
        application.id;


    companyNameInput.value =
        application.companyName || "";


    jobTitleInput.value =
        application.jobTitle || "";


    statusInput.value =
        application.status || "";


    appliedDateInput.value =
        application.appliedDate || "";


    interviewDateInput.value =
        application.interviewDate || "";


    jobLinkInput.value =
        application.jobLink || "";


    notesInput.value =
        application.notes || "";


    updateInterviewDateVisibility();


    const formTitle =
        applicationFormContainer
            ? applicationFormContainer.querySelector(
                "h2"
            )
            : null;


    if (formTitle) {

        formTitle.textContent =
            "Edit Job Application";
    }


    const submitButton =
        applicationForm
            ? applicationForm.querySelector(
                ".save-application-button"
            )
            : null;


    if (submitButton) {

        submitButton.textContent =
            "Update Application";
    }


    if (applicationFormContainer) {

        applicationFormContainer.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    }
}


// =========================
// Delete Application
// =========================

async function deleteApplication(id) {

    const confirmed =
        window.confirm(
            "Are you sure you want to delete this application?"
        );


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(
                `${API_BASE_URL}/applications/${id}`,
                {
                    method: "DELETE",
                    headers: getAuthHeaders()
                }
            );


        if (response.status === 401) {

            localStorage.removeItem("token");

            window.location.href =
                "/index.html";

            return;
        }


        if (!response.ok) {

            const responseData =
                await response.json()
                    .catch(
                        () => null
                    );


            throw new Error(
                responseData?.message ||
                "Unable to delete application."
            );
        }


        await loadDashboard();

    } catch (error) {

        console.error(
            "Delete application error:",
            error
        );


        alert(
            error.message ||
            "Unable to delete application."
        );
    }
}


// =========================
// Reset Form
// =========================

function resetApplicationForm() {

    editingApplicationId =
        null;


    if (applicationForm) {

        applicationForm.reset();
    }


    updateInterviewDateVisibility();


    const formTitle =
        applicationFormContainer
            ? applicationFormContainer.querySelector(
                "h2"
            )
            : null;


    if (formTitle) {

        formTitle.textContent =
            "Add Job Application";
    }


    const submitButton =
        applicationForm
            ? applicationForm.querySelector(
                ".save-application-button"
            )
            : null;


    if (submitButton) {

        submitButton.textContent =
            "Save Application";
    }


    clearApplicationMessage();
}


// =========================
// Application Messages
// =========================

function showApplicationMessage(
    message,
    isError
) {

    if (!applicationMessage) {
        return;
    }


    applicationMessage.textContent =
        message;


    applicationMessage.style.color =
        isError
            ? "#dc2626"
            : "#16a34a";
}


function clearApplicationMessage() {

    if (applicationMessage) {

        applicationMessage.textContent =
            "";
    }
}


// =========================
// Search
// =========================

if (searchInput) {

    searchInput.addEventListener(
        "input",
        filterAndSortApplications
    );
}


// =========================
// Filter
// =========================

if (filterStatus) {

    filterStatus.addEventListener(
        "change",
        filterAndSortApplications
    );
}


// =========================
// Sort
// =========================

if (sortApplications) {

    sortApplications.addEventListener(
        "change",
        filterAndSortApplications
    );
}


// =========================
// Upcoming Interviews
// =========================

async function loadUpcomingInterviews() {

    const upcomingList =
        document.getElementById(
            "upcomingInterviewsList"
        );


    const countdown =
        document.getElementById(
            "interviewCountdown"
        );


    if (
        !upcomingList &&
        !countdown
    ) {
        return;
    }


    try {

        const userId =
            await getCurrentUserId();


        if (!userId) {

            if (upcomingList) {

                upcomingList.innerHTML =
                    "<p>Unable to load interviews.</p>";
            }

            return;
        }


        const response =
            await fetch(
                `${API_BASE_URL}/applications/user/${userId}/upcoming-interviews`,
                {
                    method: "GET",
                    headers: getAuthHeaders()
                }
            );


        if (!response.ok) {

            throw new Error(
                "Unable to load upcoming interviews."
            );
        }


        const interviews =
            await response.json();


        renderUpcomingInterviews(
            interviews
        );


        updateInterviewCountdown(
            interviews
        );

    } catch (error) {

        console.error(
            "Upcoming interviews error:",
            error
        );


        if (upcomingList) {

            upcomingList.innerHTML =
                "<p>Unable to load upcoming interviews.</p>";
        }


        if (countdown) {

            countdown.textContent =
                "No upcoming interview available.";
        }
    }
}


// =========================
// Render Upcoming Interviews
// =========================

function renderUpcomingInterviews(
    interviews
) {

    const upcomingList =
        document.getElementById(
            "upcomingInterviewsList"
        );


    if (!upcomingList) {
        return;
    }


    if (
        !Array.isArray(interviews) ||
        interviews.length === 0
    ) {

        upcomingList.innerHTML =
            `
                <div class="empty-state">
                    <p>No upcoming interviews.</p>
                </div>
            `;

        return;
    }


    upcomingList.innerHTML =
        interviews
            .map(
                interview => `
                    <div class="interview-card">

                        <h3>
                            ${escapeHtml(
                    interview.companyName ||
                    "Company"
                )}
                        </h3>

                        <p>
                            ${escapeHtml(
                    interview.jobTitle ||
                    "Job"
                )}
                        </p>

                        <p>
                            <strong>Interview:</strong>
                            ${formatDate(
                    interview.interviewDate
                )}
                        </p>

                    </div>
                `
            )
            .join("");
}


// =========================
// Interview Countdown
// =========================

function updateInterviewCountdown(
    interviews
) {

    const countdown =
        document.getElementById(
            "interviewCountdown"
        );


    if (!countdown) {
        return;
    }


    if (
        !Array.isArray(interviews) ||
        interviews.length === 0
    ) {

        countdown.textContent =
            "No upcoming interview.";

        return;
    }


    const sortedInterviews =
        [...interviews]
            .filter(
                interview =>
                    interview.interviewDate
            )
            .sort(
                (a, b) =>
                    new Date(
                        a.interviewDate
                    ) -
                    new Date(
                        b.interviewDate
                    )
            );


    if (sortedInterviews.length === 0) {

        countdown.textContent =
            "No upcoming interview.";

        return;
    }


    const nextInterview =
        sortedInterviews[0];


    const interviewDate =
        new Date(
            `${nextInterview.interviewDate}T00:00:00`
        );


    const today =
        new Date();


    today.setHours(
        0,
        0,
        0,
        0
    );


    const difference =
        interviewDate -
        today;


    const days =
        Math.ceil(
            difference /
            (
                1000 *
                60 *
                60 *
                24
            )
        );


    if (days < 0) {

        countdown.textContent =
            "Interview date has passed.";

    } else if (days === 0) {

        countdown.textContent =
            "🎯 Interview is today!";

    } else if (days === 1) {

        countdown.textContent =
            "🎯 Interview tomorrow!";

    } else {

        countdown.textContent =
            `🎯 Interview in ${days} days`;
    }
}


// =========================
// Logout
// =========================

if (logoutButton) {

    logoutButton.addEventListener(
        "click",
        () => {

            localStorage.removeItem(
                "token"
            );


            window.location.href =
                "/index.html";
        }
    );
}


// =========================
// Format Status
// =========================

function formatStatus(
    status
) {

    const statusMap = {

        APPLIED:
            "Applied",

        ASSESSMENT:
            "Assessment",

        INTERVIEW:
            "Interview",

        OFFER:
            "Offer",

        REJECTED:
            "Rejected"
    };


    return (
        statusMap[status] ||
        status
    );
}


// =========================
// Format Date
// =========================

function formatDate(
    dateValue
) {

    if (!dateValue) {
        return "-";
    }


    const date =
        new Date(
            `${dateValue}T00:00:00`
        );


    if (
        Number.isNaN(
            date.getTime()
        )
    ) {

        return dateValue;
    }


    return date.toLocaleDateString(
        "en-IN",
        {
            day: "numeric",
            month: "long",
            year: "numeric"
        }
    );
}


// =========================
// Escape HTML
// =========================

function escapeHtml(
    value
) {

    if (value === null ||
        value === undefined) {

        return "";
    }


    return String(value)
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#039;"
        );
}


// =========================
// Initial Dashboard Load
// =========================

updateInterviewDateVisibility();

loadDashboard();