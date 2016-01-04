package sculture.models.requests;

public class SimilarStoryRequestBody {
    private long story_id;
    private int size;
    private int page;

    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
