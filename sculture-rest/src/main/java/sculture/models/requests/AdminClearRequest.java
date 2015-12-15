package sculture.models.requests;

import java.util.ArrayList;

public class AdminClearRequest {
    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    private ArrayList<String> emails;
}
