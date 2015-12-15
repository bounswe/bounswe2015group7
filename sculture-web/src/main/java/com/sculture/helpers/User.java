package com.sculture.helpers;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
public class User {
    private long user_id;
    private String username;
    private String email;
    private String fullname;
    private String facebook_id;
    private String access_token;
    private String notification_rate;
    private boolean _promoted = false;
    private boolean _following;

    public boolean is_following() {
        return _following;
    }

    public void set_following(boolean _following) {
        this._following = _following;
    }

    public boolean get_promoted() {
        return _promoted;
    }

    public void set_promoted(boolean _promoted) {
        this._promoted = _promoted;
    }

    public long getId() {
        return user_id;
    }

    public void setId(long id) {
        this.user_id = id;
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

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getNotification_rate() {
        return notification_rate;
    }

    public void setNotification_rate(String notification_rate) {
        this.notification_rate = notification_rate;
    }


}
