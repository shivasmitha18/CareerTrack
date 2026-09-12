package CareerTrack.dto;

public class ApplicationStatsResponse {

    private long totalApplications;
    private long applied;
    private long assessment;
    private long interview;
    private long offer;
    private long rejected;
    private double offerRate;
    private double rejectionRate;
    private double interviewRate;
    private long pendingApplications;
    public ApplicationStatsResponse() {
    }

    public ApplicationStatsResponse(
            long totalApplications,
            long applied,
            long assessment,
            long interview,
            long offer,
            long rejected) {

        this.totalApplications = totalApplications;
        this.applied = applied;
        this.assessment = assessment;
        this.interview = interview;
        this.offer = offer;
        this.rejected = rejected;
        this.offerRate = totalApplications == 0
                ? 0
                : (offer * 100.0) / totalApplications;
        this.rejectionRate = totalApplications == 0
                ? 0
                : (rejected * 100.0) / totalApplications;
        this.interviewRate = totalApplications == 0
                ? 0
                : (interview * 100.0) / totalApplications;

        this.pendingApplications =
                totalApplications - offer - rejected;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public long getApplied() {
        return applied;
    }

    public long getAssessment() {
        return assessment;
    }

    public long getInterview() {
        return interview;
    }

    public long getOffer() {
        return offer;
    }

    public long getRejected() {
        return rejected;

    }
    public double getOfferRate() {
        return offerRate;
    }
    public double getRejectionRate() {
        return rejectionRate;
    }
    public double getInterviewRate() {
        return interviewRate;
    }
    public long getPendingApplications() {
        return pendingApplications;
    }
}
