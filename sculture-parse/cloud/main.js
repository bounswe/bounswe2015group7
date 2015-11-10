//region STORY
Parse.Cloud.define("story_create", function (request, response) {
    var content = request.params.content;
    var title = request.params.title;
    var tags = request.params.tags;
    var userid = request.params.userid.user;

    var valid_tags = [];
    var isError = false;
    // Check errors
    if (userid == null) {
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
        story.set("owner", userid);
        story.set("lastEditor", userid);
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

Parse.Cloud.define("tempstory_create", function (request, response) {
    var content = request.params.content;
    var title = request.params.title;
    var tags = request.params.tags;
    var userid = request.params.userid;

    var valid_tags = [];
    var isError = false;
    // Check errors
    if (userid == null) {
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
        var TempStoryClass = Parse.Object.extend("TempStory");
        var story = new TempStoryClass();
        story.set("content", content.trim());
        story.set("title", title.trim());
        story.set("owner", userid);
        story.set("tagsArray", valid_tags);

        story.save(null, {
            success: function (story) {
                response.success(convertTempStory(story, true));
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
Parse.Cloud.define("tempstory_get", function (request, response) {
    var id = request.params.id;
    var TempStoryClass = Parse.Object.extend("TempStory");
    var query = new Parse.Query(TempStoryClass);
    query.get(id, {
        success: function (object) {
            response.success(convertTempStory(object, true));
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
Parse.Cloud.define("tempsearch", function (request, response) {
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

    var TempStoryClass = Parse.Object.extend("TempStory");

    var query = new Parse.Query(TempStoryClass);
    query.containedIn("tagsArray", tags);
    query.limit(size);
    query.skip(page * size);
    query.find({
        success: function (results) {
            var myRes = [];
            for (var i = 0; i < results.length; i++) {
                myRes.push(
                    convertTempStory(results[i], false)
                );
            }
            response.success(myRes);
        }
    });
});
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

function convertTempStory(story, withContent) {
    var obj = {
        "id": story.id,
        "title": story.get("title"),
        "createdAt": story.createdAt.toISOString(),
        "updatedAt": story.updatedAt.toISOString(),
        "ownerId": story.get("owner").id,
        "tags": story.get("tagsArray")
    };
    if (withContent == true) {
        obj["content"] = story.get("content");
    }

    return obj;
}
//endregion
