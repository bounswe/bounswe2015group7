package sculture.models.requests;


public class RegisterRequestBody {
    private String email;
    private String username;
    private String password;
    private String fullname;
    private long facebook_id;
    private String facebook_token;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    public long getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(long facebook_id) {
        this.facebook_id = facebook_id;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
