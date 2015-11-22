package sculture.models.response;


import sculture.models.tables.User;

public class LoginResponse {
    private long id;
    private String username;
    private String email;
    private String fullname;
    private long facebook_id;
    private String access_token;
    private boolean is_promoted;
    private int notification_rate;


    public LoginResponse(User user) {
        this.id = user.getUser_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullname = user.getFullname();
        this.facebook_id = user.getFacebook_id();
        this.access_token = user.getAccess_token();
        this.is_promoted = user.is_promoted();
        this.notification_rate = user.getNotification_rate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getNotification_rate() {
        return notification_rate;
    }

    public void setNotification_rate(int notification_rate) {
        this.notification_rate = notification_rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(long facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public boolean is_promoted() {
        return is_promoted;
    }

    public void setIs_promoted(boolean is_promoted) {
        this.is_promoted = is_promoted;
    }
}
