//region STORY
Parse.Cloud.define("story_create", function (request, response) {
    var content = request.params.content;
    var title = request.params.title;
    var tags = request.params.tags;

    var valid_tags = [];
    var isError = false;
    // Check errors
    if (request.user == null) {
        response.error('Authentication error');
        isError = true;
    }
    else if (content == null || isEmpty(content)) {
        response.error("Syntax error");
        isError = true;
    }
    else if (title == null || isEmpty(title)) {
        response.error("Syntax error");
        isError = true;
    }
    else if (tags == null || !(tags instanceof Array)) {
        response.error("Syntax error");
        isError = true;
    }
    else if (tags instanceof Array) {
        var i;
        for (i = 0; i < tags.length; i++) {
            if (!isEmpty(tags[i]))
                valid_tags.push(tags[i].trim());
        }
        if (valid_tags.length < 1) {
            response.error("Syntax error");
            isError = true;
        }
    }

    // There is no errors
    if(!isError) {
        var StoryClass = Parse.Object.extend("Story");
        var story = new StoryClass();
        story.set("content", content.trim());
        story.set("title", title.trim());
        story.set("owner", request.user);
        story.set("lastEditor", request.user);
        story.set("tagsArray", valid_tags);

        story.save(null, {
            success: function (story) {
                response.success(convertStory(story, true));
            },
            error: function (error) {
                response.error(error);
            }
        });
    }

});
//endregion

//region STORY
Parse.Cloud.define("story_get", function (request, response) {
    var id = request.params.id;
    var StoryClass = Parse.Object.extend("Story");
    var query = new Parse.Query(StoryClass);
    query.get(id, {
        success: function (object) {
            response.success(convertStory(object, true));
        },
        error: function (error) {
            response.error(error);
        }
    });
});
//endregion

//region SEARCH
Parse.Cloud.define("search", function (request, response) {
    // This is our main search API.
    // The search will be parsed here.
    // For now, the search is tag based.
    var queryWord = request.params.query;
    var page = request.params.page;
    var size = request.params.size;

    if (size == null)
        size = 100;

    if (page == null)
        page = 0;

    var tags = queryWord.split(" ");

    var StoryClass = Parse.Object.extend("Story");

    var query = new Parse.Query(StoryClass);
    query.containedIn("tagsArray", tags);
    query.limit(size);
    query.skip(page * size);
    query.find({
        success: function (results) {
            var myRes = [];
            for (var i = 0; i < results.length; i++) {
                myRes.push(
                    convertStory(results[i], false)
                );
            }
            response.success(myRes);
        }
    });
});
//endregion

//region Utility Functions
function isEmpty(str) {
    return str.replace(/^\s+|\s+$/g, '').length == 0;
}
//endregion

//region Object Converters
//Parse object to response object.
//The reason is hide to database structure from API user
function convertStory(story, withContent) {
    var obj = {
        "id": story.id,
        "title": story.get("title"),
        "createdAt": story.createdAt.toISOString(),
        "updatedAt": story.updatedAt.toISOString(),
        "ownerId": story.get("owner").id,
        "lastEditorId": story.get("lastEditor").id,
        "tags": story.get("tagsArray")
    };
    if (withContent == true) {
        obj["content"] = story.get("content");
    }

    return obj;
}
//endregion