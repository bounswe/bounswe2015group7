package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sculture.Utils;
import sculture.dao.CommentDao;
import sculture.dao.StoryDao;
import sculture.dao.TagDao;
import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.exceptions.InvalidAccessTokenException;
import sculture.exceptions.InvalidEmailException;
import sculture.exceptions.InvalidPasswordException;
import sculture.exceptions.InvalidUsernameException;
import sculture.exceptions.UserAlreadyExistsException;
import sculture.exceptions.UserNotExistException;
import sculture.exceptions.WrongPasswordException;
import sculture.models.requests.CommentGetRequestBody;
import sculture.models.requests.CommentListRequestBody;
import sculture.models.requests.LoginRequestBody;
import sculture.models.requests.RegisterRequestBody;
import sculture.models.requests.SearchRequestBody;
import sculture.models.requests.StoryCreateRequestBody;
import sculture.models.requests.StoryGetRequestBody;
import sculture.models.requests.StoryReportRequestBody;
import sculture.models.requests.UserFollowRequestBody;
import sculture.models.requests.UserGetRequestBody;
import sculture.models.response.BaseStoryResponse;
import sculture.models.response.CommentResponse;
import sculture.models.response.FullStoryResponse;
import sculture.models.response.LoginResponse;
import sculture.models.response.SearchResponse;
import sculture.models.tables.Comment;
import sculture.models.tables.Story;
import sculture.models.tables.User;
import sculture.models.tables.relations.TagStory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static sculture.Utils.checkEmailSyntax;
import static sculture.Utils.checkPasswordSyntax;
import static sculture.Utils.checkUsernameSyntax;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagStoryDao tagStoryDao;

    @RequestMapping(method = RequestMethod.POST, value = "/user/register")
    public LoginResponse user_register(@RequestBody RegisterRequestBody requestBody) {
        String email = requestBody.getEmail();
        String username = requestBody.getUsername();
        String password = requestBody.getPassword();
        String fullname = requestBody.getFullname();
        long facebook_id = requestBody.getFacebook_id();
        String facebook_token = requestBody.getFacebook_token();

        if (!checkEmailSyntax(email))
            throw new InvalidEmailException();

        if (!checkUsernameSyntax(username))
            throw new InvalidUsernameException();

        if (!checkPasswordSyntax(password))
            throw new InvalidPasswordException();


        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword_hash(Utils.password_hash(password));
        if (requestBody.getFullname() != null) u.setFullname(fullname);
        if (requestBody.getFacebook_id() != 0) u.setFacebook_id(facebook_id);
        if (requestBody.getFacebook_token() != null) u.setFacebook_token(facebook_token);
        u.setAccess_token(Utils.access_token_generate());

        try {
            userDao.create(u);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
        return new LoginResponse(u);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/get")
    public LoginResponse user_get(@RequestBody UserGetRequestBody requestBody) {
        long id = requestBody.getUserId();
        User u;
        try {
            u = userDao.getById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }
        return new LoginResponse(u);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/login")
    public LoginResponse user_login(@RequestBody LoginRequestBody requestBody) {
        String email = requestBody.getEmail();
        String password = requestBody.getPassword();
        String username = requestBody.getUsername();

        if (!checkEmailSyntax(email) && username == null)
            throw new InvalidEmailException();


        if (!checkPasswordSyntax(password))
            throw new InvalidPasswordException();


        User u;
        try {
            if (email != null) {
                u = userDao.getByEmail(email);
            } else u = userDao.getByUsername(username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }


        if (u.getPassword_hash().equals(Utils.password_hash(password))) {
            return new LoginResponse(u);

        } else
            throw new WrongPasswordException();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/user/follow")
    public LoginResponse user_follow(@RequestBody UserFollowRequestBody requestBody) {
        long id = requestBody.getUser_id();
        String accessToken = requestBody.getAccessToken();
        User u;
        try {
            u = userDao.getByAccessToken(accessToken);
            userDao.follow(u, id, requestBody.isFollow());
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }
        return new LoginResponse(u);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/create")
    public BaseStoryResponse story_create(@RequestBody StoryCreateRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            current_user = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException e) {
            throw new InvalidAccessTokenException();
        }

        //TODO Exception handling

        Date date = new Date();
        Story story = new Story();

        story.setTitle(requestBody.getTitle());
        story.setContent(requestBody.getContent());
        story.setOwner_id(current_user.getUser_id());
        story.setCreate_date(date);
        story.setLast_edit_date(date);
        story.setLast_editor_id(current_user.getUser_id());

        storyDao.create(story);

        List<String> tags = requestBody.getTags();

        for (String tag : tags) {
            TagStory tagStory = new TagStory();
            tagStory.setTag_title(tag);
            tagStory.setStory_id(story.getStory_id());
            tagStoryDao.update(tagStory);
        }

        List<String> tag_titles = tagStoryDao.getTagTitlesByStoryId(story.getStory_id());
        return new BaseStoryResponse(story, tag_titles, current_user.getUsername(), current_user.getUsername());
    }

    // TODO
    @RequestMapping("/story/get")
    public FullStoryResponse storyGet(@RequestBody StoryGetRequestBody requestBody) {
        //TODO Exception handling
        Story story = storyDao.getById(requestBody.getId());
        List<String> tag_titles = tagStoryDao.getTagTitlesByStoryId(story.getStory_id());

        User owner = userDao.getById(story.getOwner_id());
        User editor = userDao.getById(story.getLast_editor_id());

        return new FullStoryResponse(story, tag_titles, owner.getUsername(), editor.getUsername());
    }

    @RequestMapping("/search")
    public SearchResponse search(@RequestBody SearchRequestBody requestBody) {
        //TODO Exception handling
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;

        List<Long> story_ids = tagStoryDao.getStoryIdsByTag(requestBody.getQuery(), page, size);

        List<BaseStoryResponse> responses = new LinkedList<>();
        for (long id : story_ids) {
            List<String> tags = tagStoryDao.getTagTitlesByStoryId(id);
            Story story = storyDao.getById(id);
            User owner = userDao.getById(story.getOwner_id());
            User editor = userDao.getById(story.getLast_editor_id());

            responses.add(new BaseStoryResponse(story, tags, owner.getUsername(), editor.getUsername()));
        }
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setResult(responses);
        return searchResponse;
    }


    @RequestMapping("/comment/get")
    public CommentResponse commentGet(@RequestBody CommentGetRequestBody requestBody) {
        Comment comment = commentDao.getById(requestBody.getCommentId());
        CommentResponse commentResponse = new CommentResponse(comment);
        commentResponse.setOwner_username(userDao.getById(comment.getOwner_id()).getUsername());
        return new CommentResponse(comment);
    }

    @RequestMapping("/story/report")
    public boolean storyReport(@RequestBody StoryReportRequestBody requestBody) {

        storyDao.reportStory(requestBody.getUser_id(), requestBody.getStory_id());
        return true;
    }

    @RequestMapping("/comment/list")
    public List<CommentResponse> commentList(@RequestBody CommentListRequestBody requestBody) {
        List<Comment> comments = commentDao.retrieveByStory(requestBody.getStory_id());
        List<CommentResponse> responses = new LinkedList<CommentResponse>();

        for (int i = 0; i < comments.size(); i++) {
            responses.add(new CommentResponse(comments.get(i)));
        }
        return responses;


    }

}
