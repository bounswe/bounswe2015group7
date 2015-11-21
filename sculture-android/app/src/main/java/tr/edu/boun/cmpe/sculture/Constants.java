package tr.edu.boun.cmpe.sculture;

public class Constants {
    //API URLS
    private static final String API_BASE_URL = "http://192.168.137.1:8080";
    public static final String API_USER_LOGIN = API_BASE_URL + "/user/login";
    public static final String API_USER_REGISTER = API_BASE_URL + "/user/register";

    //REQUEST TAGS
    public static final String REQUEST_TAG_LOGIN = "request_login";
    public static final String REQUEST_TAG_REGISTER = "request_register";

    //PREFS
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_ACCESS_TOKEN = "token";

    //JSON/REQUEST FIELDS
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_ACCESS_TOKEN = "access_token";
}
