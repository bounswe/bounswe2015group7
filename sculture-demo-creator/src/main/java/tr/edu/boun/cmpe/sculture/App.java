package tr.edu.boun.cmpe.sculture;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class App {
    private static final String API_BASE_URL = "http://52.28.216.93:9000";
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
    public static final String API_STORY_REPORT = API_BASE_URL + "/story/report";
    public static final String API_STORY_VOTE = API_BASE_URL + "/story/vote";
    public static final String API_TAG_GET = API_BASE_URL + "/tag/get";
    public static final String API_TAG_CREATE = API_BASE_URL + "/tag/create";
    public static final String API_TAG_EDIT = API_BASE_URL + "/tag/edit";
    public static final String API_USER_GET = API_BASE_URL + "/user/get";
    public static final String API_USER_FOLLOW = API_BASE_URL + "/user/follow";
    public static final String API_IMAGE_UPLOAD = API_BASE_URL + "/image/upload";
    public static final String API_ADMIN_CLEAR = API_BASE_URL + "/admin/clear";

    public static void main(String[] args) throws UnirestException, IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("john@sculture.com");
        jsonArray.put("mohan@sculture.com");
        jsonObject.put("emails", jsonArray);


        HttpResponse<JsonNode> response = Unirest.post(API_ADMIN_CLEAR)
                .header("content-type", "application/json")
                .body(jsonObject.toString())
                .asJson();

        response.getBody();

        User user_john = new User("john", "john@sculture.com");
        User user_mohan = new User("mohan", "mohan@sculture.com");

        user_john.register();
        user_mohan.register();


        ArrayList<String> tags_1 = new ArrayList<String>();
        tags_1.add("oktoberfest");
        tags_1.add("beer");
        ArrayList<String> media_1 = new ArrayList<String>();
        Image image_1 = new Image("image_1.jpg");
        image_1.Upload();
        media_1.add(image_1.id);
        Story story_1 = new Story(
                "Beer, pretzels and lederhosen: How to survive Oktoberfest",
                "Prost from Oktoberfest! You can quote me on this: You haven’t been to a party until you’ve been to Oktoberfest. Seriously, this festival can put Las Vegas to shame.\n" +
                        "\n" +
                        "First stop, beer garden. Rows and rows of wooden benches are set out in the massive yard holding around twenty people per bench, filling the garden with endless chatter, laughter and song. The Bavarian waitresses fill their arms with up to fourteen steins to quickly please the thirsty travellers and are welcomed with a cheer as the beers slam down on the benches.  You receive your stein and clash your glass with those around you and yell ‘Prost!’– German for Cheers! As you sip your cold stein, and await your order of that salty German pretzel, you chat to the people next to you, after all, you have something in common – a love of beer!  Soon strangers become friends, and the chatting turns to laughter as you learn more about the people who have travelled across the world for this fantastic festival.\n" +
                        "\n" +
                        "It’s great to see so many people dressed up in traditional Bavarian clothes (lederhosen for guys, dirndls for girls), having a good time, celebrating, and drinking good beer. \n" +
                        "Oktoberfest was one of the best festivals I’ve ever attended. It’s a 16–18 day beer festival held annually in Munich, Germany, running from late September to the first weekend in October. It all began when Crown Prince Ludwig married Princess Therese on October 12, 1810. The citizens of Munich were invited to attend the festivities held on the fields in front of the city, which the locals call “Wies’n” (which means grass, and is why Oktoberfest is nicknamed Wiesn in Germany).\n" +
                        "If you are planning on going to Oktoberfest, I have a few tips for you:\n" +
                        "- Get an outfit! Friends at home laughed when they saw that we bought outfits for Oktoberfest- but I promise that you will stick out like a sore thumb if you DON’T wear one!\n" +
                        "- You don’t have to spend a lot of money to have a great time. Each litre of beer was around €10. \n" +
                        "- Apparently it is illegal to stand ON a table in Germany! I found this out when locals laughed at me when I did it. \n" +
                        "If you ever have the chance to be visit Germany, I highly suggest you try to plan your trip around Oktoberfest. Even just writing this post brings a smile back to my face- I loved the atmosphere that was created by the different generations of Germans all under one roof, and would go back there again if I could!\n",
                tags_1,
                media_1
        );

        story_1.create(user_john);

        ArrayList<String> tags_2 = new ArrayList<String>();
        tags_2.add("tea");
        tags_2.add("tea");
        ArrayList<String> media_2 = new ArrayList<String>();
        Image image_2 = new Image("image_1.jpg");
        image_2.Upload();
        media_2.add(image_2.id);
        Story story_2 = new Story("Kung Fu Tea",
                "Kungfu Tea, I hear you asking? Yes, there really does exist something called Kungfu tea.\n" +
                        "It is a kind of tea ceremony, and actually we have been practicing it for a while with our kungfu students. But what I wanted to share today was the time we went to Huangshan to learn about Huangshan’s tea culture. It was one of my most memorable experiences. I loved being tea farmer for one day, even though it was incredibly difficult.\n" +
                        "A real tea ceremony has a specific procedure and should ideally be carried out in a peaceful and relaxing environment. The place we went to was perfect. They even invited a girl to play on Guzheng 古筝, a traditional Chinese zither.\n" +
                        "\n" +
                        "Preparing the Tea:\n" +
                        "\n" +
                        "The first stage of preparation is known as温壶烫杯 wenhu tangbei, which literally mean “warming the pot and heating the cups”. Usually the cups and the pot are on the table and are then warmed and sterilized with hot water.\n" +
                        "The next step is known as 乌龙入宫 wulong rugong, which means “The black dragon enters the palace”. This term can be traced back to the use of Oolong tea, as it means “Black Dragon”. The teapot can now be filled with tea. Usually for a 150 ml tea pot at least 5 grams of tea leaves are used, but it can depend on the size of the pot and the strength of the tea you want.\n" +
                        "Next the tea leaves are rinsed using hot water poured from some height above the pot, this is known as悬壶高冲 xuanhu gaochong, meaning “rinsing from an elevated pot”. It is important that the water is not too hot as not to burn the tea leaves.\n" +
                        "\n" +
                        "Brewing the Tea\n" +
                        "\n" +
                        "Usually this first brew is poured into the cups but is not drunk, 行雲流水 hangyun liushui, meaning “A row of clouds, running water”. So it’s more or less a first cleansing of the tea leaves.\n" +
                        "回旋低斟 huixuan dizhen, meaning “pouring again from a low height”.  I have learned that it is very important to pure the tea from a height, because the force of water is used to rinse the tea leaves.\n" +
                        " \n" +
                        "Serving the Tea\n" +
                        "\n" +
                        "After the tea has been brewed for 20 to 30 seconds, it’s poured evenly into the teacups, in a circular manner around the guests. A quality tea can be brewed for 4 to 8 times. Some teas can last for 8 or more rounds.\n" +
                        " \n" +
                        "Even though there are a few things you have to pay attention to when doing a tea ceremony, it is not as strict as it sounds. It is possible to have a relaxed gathering with friends, during which you can chat and laugh and enjoy the tea you brew. But it’s usually only the host who prepares and serves the tea to all the guests.\n"
                , tags_2, media_2);
        story_2.create(user_mohan);

        Comment comment_1 = new Comment("I want to drink this tea.");
        comment_1.post(user_john, story_2);

    }

    public static class User {
        String username;
        String email;
        String access_token;
        long id;

        User(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public void register() throws UnirestException {
            JSONObject register_request_body_1 = new JSONObject();
            register_request_body_1.put("username", this.username);
            register_request_body_1.put("email", this.email);
            register_request_body_1.put("password", "asdfasdf");

            HttpResponse<JsonNode> response_register_1 = Unirest.post(API_USER_REGISTER)
                    .header("content-type", "application/json")
                    .body(register_request_body_1.toString())
                    .asJson();

            this.access_token = response_register_1.getBody().getObject().getString("access_token");
            this.id = response_register_1.getBody().getObject().getInt("id");
        }

        public void login() throws UnirestException {
            JSONObject register_request_body_1 = new JSONObject();
            register_request_body_1.put("email", this.email);
            register_request_body_1.put("password", "asdfasdf");

            HttpResponse<JsonNode> response_register_1 = Unirest.post(API_USER_LOGIN)
                    .header("content-type", "application/json")
                    .body(register_request_body_1.toString())
                    .asJson();

            this.access_token = response_register_1.getBody().getObject().getString("access_token");
            this.id = response_register_1.getBody().getObject().getInt("id");
        }
    }

    public static class Story {
        long id;
        String title;
        String content;
        ArrayList<String> tags;
        ArrayList<String> media;

        public Story(String title, String content, ArrayList<String> tags, ArrayList<String> media) {
            this.title = title;
            this.content = content;
            this.tags = tags;
            this.media = media;
        }

        public void create(User user) throws UnirestException {
            JSONObject request_body = new JSONObject();
            request_body.put("title", this.title);
            request_body.put("content", this.content);

            JSONArray tagsArray = new JSONArray();
            for (String tag : this.tags) {
                tagsArray.put(tag);
            }

            JSONArray mediaArray = new JSONArray();
            for (String media : this.media) {
                mediaArray.put(media);
            }

            request_body.put("tags", tagsArray);
            request_body.put("media", mediaArray);


            HttpResponse<JsonNode> response = Unirest.post(API_STORY_CREATE)
                    .header("content-type", "application/json")
                    .header("access-token", user.access_token)
                    .body(request_body.toString())
                    .asJson();

            this.id = response.getBody().getObject().getLong("id");
        }
    }

    public static class Image {
        byte[] bytes;
        String id;

        public Image(String filename) throws IOException {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());

            BufferedImage bufferedImage = ImageIO.read(file);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            bytes = baos.toByteArray();

        }

        public void Upload() throws UnirestException {
            HttpResponse<JsonNode> response = Unirest.post(API_IMAGE_UPLOAD)
                    .body(bytes)
                    .asJson();

            this.id = response.getBody().getObject().getString("id");
        }
    }

    public static class Comment {
        String content;

        public Comment(String content) {
            this.content = content;
        }

        public void post(User user, Story story) throws UnirestException {
            JSONObject request_body = new JSONObject();
            request_body.put("storyId", story.id);
            request_body.put("content", this.content);


            HttpResponse<JsonNode> response = Unirest.post(API_COMMENT_NEW)
                    .header("content-type", "application/json")
                    .header("access-token", user.access_token)
                    .body(request_body.toString())
                    .asJson();


        }
    }
}
