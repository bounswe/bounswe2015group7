package tr.edu.boun.cmpe.sculture;

public class Constants {
    //API URLS
    private static final String API_BASE_URL = BuildConfig.API_BASE_URL;
    public static final String API_USER_LOGIN = API_BASE_URL + "/user/login";
    public static final String API_USER_REGISTER = API_BASE_URL + "/user/register";
    public static final String API_STORY_GET = API_BASE_URL + "/story/get";
    public static final String API_STORY_CREATE = API_BASE_URL + "/story/create";
    public static final String API_SEARCH = API_BASE_URL + "/search";
    public static final String API_COMMENT_LIST = API_BASE_URL + "/comment/list";
    public static final String API_COMMENT_GET = API_BASE_URL + "/comment/get";
    public static final String API_STORY_EDIT = API_BASE_URL + "/story/edit";
    public static final String API_USER_STORIES = API_BASE_URL + "/user/stories";
    public static final String API_COMMENT_NEW = API_BASE_URL + "/comment/new";
    public static final String API_TAG_GET = API_BASE_URL + "/tag/get";
    public static final String API_TAG_CREATE = API_BASE_URL + "/tag/create";
    public static final String API_TAG_EDIT = API_BASE_URL + "/tag/edit";

    //REQUEST TAGS
    public static final String REQUEST_TAG_LOGIN = "request_login";
    public static final String REQUEST_TAG_REGISTER = "request_register";
    public static final String REQUEST_TAG_SEARCH = "request_search";
    public static final String REQUEST_TAG_STORY_CREATE = "request_story_create";
    public static final String REQUEST_TAG_STORY_GET = "request_story_get";
    public static final String REQUEST_TAG_COMMENT_LIST = "request_comment_list";
    public static final String REQUEST_TAG_COMMENT_GET = "request_comment_get";
    public static final String REQUEST_TAG_DESC_CREATE = "request_tag_desc_create";
    public static final String REQUEST_TAG_DESC_GET = "request_tag_desc_get";

    //PREFS
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_ACCESS_TOKEN = "token";
    public static final String PREF_USER_ID = "user_id";

    //JSON FIELDS
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_QUERY = "query";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_PAGE = "page";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_STORY_ID = "story_id";

    //BUNDLE KEYS
    public static final String BUNDLE_STORY_ID = "story_id";
    public static final String BUNDLE_IS_EDIT = "is_edit";
    public static final String BUNDLE_TAG_TITLE = "tag_title";
    public static final String BUNDLE_MEDIA_IDS = "media_ids";
    public static final String BUNDLE_INDEX = "index";

    //HEADER FIELDS
    public static final String HEADER_ACCESS_TOKEN = "access-token";
}
