package com.sculture.model;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
public class User {
    private long id;
    private String username;
    private String email;
    private String fullname;
    private String facebook_id;
    private String access_token;
    private String notification_rate;
    private boolean _promoted;

    public boolean get_promoted() {
        return _promoted;
    }

    public void set_promoted(boolean _promoted) {
        this._promoted = _promoted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
