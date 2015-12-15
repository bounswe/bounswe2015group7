package sculture.models.response;

public class StoryReportResponse {

    long report_count;

    public StoryReportResponse(long reportCount) {
        report_count = reportCount;
    }

    public long getReport_count() {
        return report_count;
    }

    public void setReport_count(long report_count) {
        this.report_count = report_count;
    }


}