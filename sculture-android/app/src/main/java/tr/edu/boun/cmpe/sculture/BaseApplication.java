package tr.edu.boun.cmpe.sculture;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static tr.edu.boun.cmpe.sculture.Constants.PREFS_NAME;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_USERNAME;

public class BaseApplication extends Application {
    public static BaseApplication baseApplication;

    public RequestQueue mRequestQueue;

    private String TOKEN = "";
    private String USERNAME = "";
    private String EMAIL = "";

    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        autoLogin();
    }

    private void autoLogin() {
        if (TOKEN.equals("")) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String email = settings.getString(PREF_EMAIL, "");
            String username = settings.getString(PREF_USERNAME, "");
            String token = settings.getString(PREF_ACCESS_TOKEN, "");
            if (email.equals("") || token.equals("") || username.equals(""))
                Toast.makeText(getApplicationContext(), getString(R.string.loginAdvice), Toast.LENGTH_LONG).show();
            else {
                setUserInfo(token, username, email);
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.userLogin) + USERNAME, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set user session.
     *
     * @param token    Access token of the user.
     * @param username Username of the user.
     * @param email    Email address of the user.
     */
    public void setUserInfo(String token, String username, String email) {
        this.TOKEN = token;
        this.USERNAME = username;
        this.EMAIL = email;
        this.isLoggedIn = true;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, EMAIL);
        editor.putString(PREF_ACCESS_TOKEN, TOKEN);
        editor.putString(PREF_USERNAME, USERNAME);
        editor.apply();

    }

    /**
     * Checks whether there is a user session.
     *
     * @return Whether tare is a user session.
     */
    public boolean checkLogin() {
        return isLoggedIn;
    }

    /**
     * Clears all user information.
     */
    public void logOut() {
        this.TOKEN = "";
        this.USERNAME = "";
        this.EMAIL = "";
        this.isLoggedIn = false;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, "");
        editor.putString(PREF_ACCESS_TOKEN, "");
        editor.putString(PREF_USERNAME, "");
        editor.apply();
    }

    /**
     * Retrieve the access token of current user.
     *
     * @return Access token of current user
     */
    public String getTOKEN() {
        return TOKEN;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }
}
