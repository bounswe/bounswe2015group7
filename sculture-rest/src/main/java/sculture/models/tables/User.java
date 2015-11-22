package sculture.models.tables;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "USER")
public class User {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long user_id;

    @NotNull
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String password_hash;

    private long facebook_id;

    private String facebook_token;

    private String fullname;

    @Column(name = "is_promoted", nullable = false, columnDefinition = "boolean default false")
    private boolean is_promoted;

    @Column(name = "notification_rate", nullable = false, columnDefinition = "int default 0")
    private int notification_rate;

    @Column(name = "access_token", unique = true)
    private String access_token;

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }


    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public long getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(long facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    public boolean is_promoted() {
        return is_promoted;
    }

    public void setIs_promoted(boolean is_promoted) {
        this.is_promoted = is_promoted;
    }

    public int getNotification_rate() {
        return notification_rate;
    }

    public void setNotification_rate(int notification_rate) {
        this.notification_rate = notification_rate;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
} // class User