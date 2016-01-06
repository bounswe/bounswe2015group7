package tr.edu.boun.cmpe.sculture;


public class TestConstants {

    /**
     * This file stores all constants which will be used by test.
     *
     * Before starting to test, initial status should be validated
     * There should be two user on database TEST_BOT_1 and TEST_BOT_2
     * TEST_BOT_1 should not have any story.
     *
     * TEST_BOT_2 should have a story with tag TEST_BOT_2_STORY_1_TAG
     * and with title TEST_BOT_2_STORY_1_TITLE
     * and with content TEST_BOT_2_STORY_1_CONTENT
     *
     * TEST_BOT_2_STORY_1_TAG should contain only the story of TEST_BOT_2
     *
     * TEST_BOT_1 should unfollow TEST_BOT_2
     *
     *
     * IF ALL TESTS ARE COMPLETED SUCCESSFULLY. THE PROCEDURES ENSURES THAT
     * THE LAST STATE IS SAME WITH INITIAL STATE
     */
    public static final String TEST_BOT_1_EMAIL = "test@sculture.com";
    public static final String TEST_BOT_1_USERNAME = "test-bot";
    public static final String TEST_BOT_1_PASSWORD = "asdfasdf";
    public static final String TEST_BOT_1_ACCESS_TOKEN = "6afe1bf7-d9ed-4d41-93da-8cd95e5ae430";
    public static final long TEST_BOT_1_ID = 60;

    public static final String TEST_BOT_1_STORY_TAG = "test1";

    public static final String TEST_BOT_2_EMAIL = "test2@sculture.com";
    public static final String TEST_BOT_2_USERNAME = "test-bot2";
    public static final String TEST_BOT_2_PASSWORD = "asdfasdf";
    public static final String TEST_BOT_2_ACCESS_TOKEN = "98d8a4b5-6a85-4afb-ba8b-ae202793653f";
    public static final long TEST_BOT_2_ID = 61;

    public static final long TEST_BOT_2_STORY_1_ID = 78;
    public static final String TEST_BOT_2_STORY_1_TITLE = "test";
    public static final String TEST_BOT_2_STORY_1_TAG = "test2";
    public static final String TEST_BOT_2_STORY_1_CONTENT = "test2 story content";
}
