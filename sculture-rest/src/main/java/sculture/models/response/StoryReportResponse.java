package sculture.models.response;

public class StoryReportResponse {
    
    int report_count;

    public StoryReportResponse(int reportCount) {
        report_count = reportCount;
    }

    public int getReport_count() {
        return report_count;
    }

    public void setReport_count(int report_count) {
        this.report_count = report_count;
    }


}