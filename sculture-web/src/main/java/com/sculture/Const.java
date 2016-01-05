package com.sculture;

/**
 * Created by safa on 05/01/16.
 */
public class Const {
    public static final String REST_BASE_URL = "http://52.59.252.52:9000";

    public class Api {
        public static final String USER_REGISTER = "/user/register";
        public static final String USER_UPDATE = "/user/update";
        public static final String USER_GET = "/user/get";
        public static final String USER_STORIES = "/user/stories";
        public static final String USER_LOGIN = "/user/login";
        public static final String USER_FOLLOW = "/user/follow";

        public static final String SEARCH = "/search";
        public static final String SEARCH_ALL = "/search/all";

        public static final String TAG_GET = "/tag/get";
        public static final String TAG_EDIT = "/tag/edit";

        public static final String IMAGE_UPLOAD = "/image/upload";
        public static final String IMAGE_GET = "/image/get/";

        public static final String STORY_CREATE = "/story/create";
        public static final String STORY_EDIT = "/story/edit";
        public static final String STORY_DELETE = "/story/delete";
        public static final String STORY_GET = "/story/get";
        public static final String STORY_REPORT = "/story/report";
        public static final String STORY_VOTE = "/story/vote";
        public static final String STORY_SIMILAR = "/story/similar";

        public static final String COMMENT_LIST = "/comment/list";
        public static final String COMMENT_GET = "/comment/get";
        public static final String COMMENT_NEW = "/comment/new";
        public static final String COMMENT_EDIT = "/comment/edit";

        public static final String RECOMMENDATION_SIMILAR_TO_LIKED = "/recommendation/similarToLiked";
        public static final String RECOMMENDATION_TRENDING = "/recommendation/trending";
        public static final String RECOMMENDATION_FROM_FOLLOWED_USER = "/recommendation/fromFollowedUser";

        public static final String ADMIN_SEARCH_REINDEX = "/admin/search/reindex";
        public static final String ADMIN_CLEAR = "/admin/clear";
        public static final String ADMIN_ORPHAN = "/admin/orphan";

       }
}
