package tr.edu.boun.cmpe.sculture;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;


public class BaseApplication extends Application {
    public static BaseApplication baseApplication;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_EMAIL = "email";

    private String TOKEN = null;
    private String USERNAME = "";
    private String EMAIL = "";
    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        autoLogin();
    }

    private void autoLogin() {
        if (TOKEN == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String email = settings.getString(PREF_EMAIL, "");
            String password = settings.getString(PREF_PASSWORD, "");
            if (email.equals("") || password.equals(""))
                Toast.makeText(getApplicationContext(), getString(R.string.loginAdvice), Toast.LENGTH_LONG).show();
            else {
                //TODO Retrieve token and user information update setUserInfo()
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.userLogin) + USERNAME, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUserInfo(String token, String username, String email, String password) {
        this.TOKEN = token;
        this.USERNAME = username;
        this.EMAIL = email;
        this.isLoggedIn = true;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_PASSWORD, password);
        editor.putString(PREF_EMAIL, EMAIL);
        editor.apply();

    }

    public boolean checkLogin() {
        return isLoggedIn;
    }

    public void logOut() {
        this.TOKEN = "";
        this.USERNAME = "";
        this.EMAIL = "";
        this.isLoggedIn = false;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_PASSWORD, "");
        editor.putString(PREF_EMAIL, "");
        editor.apply();
    }


}
