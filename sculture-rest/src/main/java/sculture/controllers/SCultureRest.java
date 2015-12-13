package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import sculture.Utils;
import sculture.dao.*;
import sculture.exceptions.*;
import sculture.models.requests.*;
import sculture.models.response.*;
import sculture.models.tables.Comment;
import sculture.models.tables.Story;
import sculture.models.tables.Tag;
import sculture.models.tables.User;
import sculture.models.tables.relations.TagStory;

import java.io.FileOutputStream;
import java.util.*;

import static sculture.Utils.*;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private VoteStoryDao voteStoryDao;

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

    @RequestMapping(method = RequestMethod.POST, value = "/user/update")
    public LoginResponse user_update(@RequestBody UserUpdateRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User u;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            u = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException e) {
            throw new InvalidAccessTokenException();
        }

        String email = requestBody.getEmail();
        String username = requestBody.getUsername();
        String old_password = requestBody.getOld_password();
        String new_password = requestBody.getNew_password();
        String fullname = requestBody.getFullname();

        if (!checkEmailSyntax(email))
            throw new InvalidEmailException();

        if (!checkUsernameSyntax(username))
            throw new InvalidUsernameException();

        if (!checkPasswordSyntax(old_password))
            throw new InvalidPasswordException();

        if (!checkPasswordSyntax(new_password))
            throw new InvalidPasswordException();

        if (u.getPassword_hash().equals(Utils.password_hash(old_password))) {
            u.setEmail(email);
            u.setUsername(username);
            u.setPassword_hash(Utils.password_hash(new_password));

            if (fullname != null) u.setFullname(fullname);
            userDao.update(u);
            return new LoginResponse(u);
        } else
            throw new WrongPasswordException();

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

    @RequestMapping(method = RequestMethod.POST, value = "/search/all")
    public List<Story> user_get() {
        return storyDao.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tag/get")
    public TagResponse user_get(@RequestBody TagGetRequestBody requestBody) {
        String title = requestBody.getTag_title();
        Tag tag;
        try {
            tag = tagDao.getByTitle(title);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }
        return new TagResponse(tag);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/stories")
    public SearchResponse user_get(@RequestBody StoriesGetRequestBody requestBody) {
        //TODO Exception handling
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;

        long id = requestBody.getId();

        List<Story> storyList = storyDao.getByOwner(id, page, size);

        SearchResponse searchResponse = new SearchResponse();
        List<BaseStoryResponse> responses = new ArrayList<>();
        for (Story story : storyList) {
            responses.add(new BaseStoryResponse(story, tagStoryDao, userDao));
        }
        searchResponse.setResult(responses);
        return searchResponse;
    }

    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not 
    final Set<String> identifiers = new HashSet<String>();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if (identifiers.contains(builder.toString()))
                builder = new StringBuilder();
        }
        return builder.toString();
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ImageResponse handleFileUpload(
            @RequestBody byte[] file) throws Exception {
        String randomIdentifier = randomIdentifier();
        FileOutputStream fos = new FileOutputStream("/image/" + randomIdentifier + ".jpg");
        fos.write(file);
        fos.close();
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(randomIdentifier);
        return imageResponse;
    }

    private FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();

    @RequestMapping(method = RequestMethod.GET, value = "/image/get/{id}", produces = "image/jpg")
    public Resource image_get(@PathVariable String id) {
        return resourceLoader.getResource("file:/image/" + id + ".jpg");
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
    public LoginResponse user_follow(@RequestBody UserFollowRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        long id = requestBody.getUser_id();
        String accessToken = headers.get("access-token").get(0);
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
        if (requestBody.getMedia() != null) {
            String str = "";
            for (String media : requestBody.getMedia()) {
                str += media;
                str += ",";
            }
            story.setMedia(str.substring(0, str.length() - 1));
        }
        storyDao.create(story);

        if (requestBody.getTags() != null) {
            List<String> tags = requestBody.getTags();

            for (String tag : tags) {
                TagStory tagStory = new TagStory();
                tagStory.setTag_title(tag);
                tagStory.setStory_id(story.getStory_id());
                tagStoryDao.update(tagStory);
            }
        }
        return new BaseStoryResponse(story, tagStoryDao, userDao);
    }

    @RequestMapping("/story/get")
    public FullStoryResponse storyGet(@RequestBody StoryGetRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);

        Story story = storyDao.getById(requestBody.getId());

        return new FullStoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao);
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
            Story story = storyDao.getById(id);
            responses.add(new BaseStoryResponse(story, tagStoryDao, userDao));
        }
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setResult(responses);
        return searchResponse;
    }


    @RequestMapping("/comment/get")
    public CommentResponse commentGet(@RequestBody CommentGetRequestBody requestBody) {
        Comment comment = commentDao.getById(requestBody.getCommentId());
        return new CommentResponse(comment, userDao);
    }

    @RequestMapping("/comment/new")
    public CommentResponse commentGet(@RequestBody CommentNewRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            current_user = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException e) {
            throw new InvalidAccessTokenException();
        }

        Comment comment = new Comment();
        Date date = new Date();
        comment.setContent(requestBody.getContent());
        comment.setCreate_date(date);
        comment.setOwner_id(current_user.getUser_id());
        comment.setStory_id(requestBody.getStoryId());
        comment.setLast_edit_date(date);
        commentDao.create(comment);
        return new CommentResponse(comment, userDao);
    }

    @RequestMapping("/story/report")
    public boolean storyReport(@RequestBody StoryReportRequestBody requestBody) {
        storyDao.reportStory(requestBody.getUser_id(), requestBody.getStory_id());
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/vote")
    public VoteResponse storyVote(@RequestBody StoryVoteRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        Story story = voteStoryDao.vote(current_user.getUser_id(), requestBody.getStory_id(), requestBody.getVote());
        return new VoteResponse(requestBody.getVote(), story);
    }

    @RequestMapping("/comment/list")
    public CommentListResponse commentList(@RequestBody CommentListRequestBody requestBody) {
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;

        List<Comment> comments = commentDao.retrieveByStory(requestBody.getStory_id(), page, size);
        List<CommentResponse> responses = new LinkedList<>();
        Collections.sort(comments);
        for (int i = comments.size() - 1; i >= 0; i--) {
            responses.add(new CommentResponse(comments.get(i), userDao));
        }
        /*for (Comment comment : comments) {
            responses.add(new CommentResponse(comment, userDao));
        }*/


        CommentListResponse commentListResponse = new CommentListResponse();
        commentListResponse.setResult(responses);
        return commentListResponse;
    }

    /**
     * Returns current user by using access-token
     *
     * @param headers The headers which contains access-token
     * @param notnull Whether the current_user can be null or not, if true it will not return null instead throw an exception
     * @return Current user
     */
    private User getCurrentUser(HttpHeaders headers, boolean notnull) {
        User current_user = null;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            current_user = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException ignored) {
            if (notnull)
                throw new InvalidAccessTokenException();
        }
        return current_user;
    }
}
