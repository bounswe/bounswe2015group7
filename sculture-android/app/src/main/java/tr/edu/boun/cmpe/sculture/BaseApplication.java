package tr.edu.boun.cmpe.sculture;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import static tr.edu.boun.cmpe.sculture.Constants.PREFS_NAME;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_USER_ID;

public class BaseApplication extends Application {
    public static BaseApplication baseApplication;

    public RequestQueue mRequestQueue;
    public ImageLoader mImageLoader;

    private String TOKEN = "";
    private String USERNAME = "";
    private String EMAIL = "";
    private long USER_ID = -1;
    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        autoLogin();
    }

    /**
     * Automatically logs in according to Preferences data
     */
    private void autoLogin() {
        if (TOKEN.equals("")) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String email = settings.getString(PREF_EMAIL, "");
            String username = settings.getString(PREF_USERNAME, "");
            String token = settings.getString(PREF_ACCESS_TOKEN, "");
            long user_id = settings.getLong(PREF_USER_ID, -1);
            if (email.equals("") || token.equals("") || username.equals("") || user_id == -1)
                Toast.makeText(getApplicationContext(), getString(R.string.loginAdvice), Toast.LENGTH_LONG).show();
            else {
                setUserInfo(token, username, email, user_id);
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
    public void setUserInfo(String token, String username, String email, long user_id) {
        this.TOKEN = token;
        this.USERNAME = username;
        this.EMAIL = email;
        this.isLoggedIn = true;
        this.USER_ID = user_id;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, EMAIL);
        editor.putString(PREF_ACCESS_TOKEN, TOKEN);
        editor.putString(PREF_USERNAME, USERNAME);
        editor.putLong(PREF_USER_ID, USER_ID);
        editor.apply();

    }

    /**
     * Checks whether there is a user session.
     *
     * @return True:user logged in, False: user not logged in
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
        this.USER_ID = -1;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, "");
        editor.putString(PREF_ACCESS_TOKEN, "");
        editor.putString(PREF_USERNAME, "");
        editor.putLong(PREF_USER_ID, -1);
        editor.apply();
    }

    /**
     * Retrieves the access token of current user.
     *
     * @return Access token of current user
     */
    public String getTOKEN() {
        return TOKEN;
    }

    /**
     * Retrieves the email address of current user.
     *
     * @return Email address of current user
     */
    public String getEMAIL() {
        return EMAIL;
    }

    /**
     * Retrieves the username of current user
     *
     * @return Username of current user
     */
    public String getUSERNAME() {
        return USERNAME;
    }

    /**
     * Retrieves the user id of current user
     *
     * @return User id of current user
     */
    public long getUSER_ID() {
        return USER_ID;
    }
}
