package sculture.models.requests;

/**
 * Created by gmzrmks on 29.11.2015.
 */
public class UserUpdateRequestBody {

    private String accessToken;

    private String old_password;

    private String new_password;

    private String email;

    private String username;

    private String fullname;

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getNew_password() {
        return new_password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getOld_password() {
        return old_password;
    }

    public String getUsername() {
        return username;
    }
}

