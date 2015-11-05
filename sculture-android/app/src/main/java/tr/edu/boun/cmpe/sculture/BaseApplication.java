package tr.edu.boun.cmpe.sculture;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class BaseApplication extends Application {
    public static final String APPLICATION_ID = "yJboQpdSMbOBNsvjN3KU43SWvsFIDGhyYP1QZVP4";
    public static final String CLIENT_KEY = "PgmbRhX43WhrlNxgvlImrHbmTIMSvccG5FHeLC03";

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_USERNAME= "username";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        autoLogin();
    }

    private void autoLogin() {
        if (ParseUser.getCurrentUser() == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            final String username = settings.getString(PREF_USERNAME, "");
            String password = settings.getString(PREF_PASSWORD, "");
            if (username.equals("") || password.equals(""))
                Toast.makeText(getApplicationContext(), getString(R.string.loginAdvice), Toast.LENGTH_LONG).show();
            else {
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null)
                        Toast.makeText(getApplicationContext(), getString(R.string.userLogin) + username, Toast.LENGTH_SHORT).show();
                    else
                            Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.userLogin) + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
        }
    }
}
