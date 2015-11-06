// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function (request, response) {
    response.success("Hello world!");
});


Parse.Cloud.define("story_create", function (request, response) {
    var content = request.params.content;
    var title = request.params.title;
    var tags = request.params.tags;

    var valid_tags = [];

    // Check errors
    if (request.user == null) {
        response.error('Authentication error');
    }
    else if (content == null || isEmpty(content)) {
        response.error("Syntax error");
    }
    else if (title == null || isEmpty(title)) {
        response.error("Syntax error");
    }
    else if (tags == null || !(tags instanceof Array)) {
        response.error("Syntax error");
    }

    else if (tags instanceof Array) {
        var i;
        for (i = 0; i < tags.length; i++) {
            if (!isEmpty(tags[i]))
                valid_tags.push(tags[i].trim());
        }
        if (valid_tags.length < 1) {
            response.error("Syntax error");
        }
    }




    // There is no errors
    else {
        var StoryClass = Parse.Object.extend("Story");
        var story = new StoryClass();
        story.set("content", content.trim());
        story.set("title", title.trim());
        story.set("owner", request.user);
        story.set("lastEditor", request.user);
        story.set("tagsArray", valid_tags);

        story.save(null, {
            success: function (story) {
                response.success(convertStory(story,true));
            },
            error: function (error) {
                response.error(error);
            }
        });
    }

});

Parse.Cloud.define("story_get", function (request, response) {
    var id = request.params.id;
    var StoryClass = Parse.Object.extend("Story");
    var query = new Parse.Query(StoryClass);
    query.get(id, {
        success: function (object) {
            response.success(convertStory(object,true));
        },
        error: function (error) {
            response.error(error);
        }
    });
});

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

    var results = [];

    var query = new Parse.Query(StoryClass);
    query.containedIn("tagsArray", tags);
    query.limit(size);
    query.skip(page * size);
    query.find({
        success: function (results) {
            var myres = [];
            for (var i = 0; i < results.length; i++) {
                myres.push(
                    convertStory(results[i], false)
                );
            }
            response.success(myres);
        }
    });
});



/* 

 Parse.Cloud.beforeSave("Story", function(request, response) {
 var story = request.object;
 var tags = story.get("tagsArray");
 var locTags = story.get("locationTagsArray");

 var TagClass = Parse.Object.extend("Tag");
 tags.forEach(function(item) {
 var query = new Parse.Query(TagClass);
 query.equalTo("title", item);
 query.first().then(function(object) {
 if (object != null) {
 object.relation("stories").add(story);
 } else {
 var newtag = new TagClass();
 newtag.set("title", item);
 newtag.set("lastEditor", request.user);
 newtag.set("isLocation", false);
 newtag.save().then(
 function(newtag) {
 newtag.relation("stories").add(story);
 },
 function(error) {
 // fail
 });

 }
 });
 });

 locTags.forEach(function(item) {
 var query = new Parse.Query(TagClass);
 query.equalTo("title", item);
 query.first().then(function(object) {
 if (object != null) {
 if (!object.get("isLocation")) {
 object.set("isLocation", true);
 object.save().then(
 function(object) {
 object.relation("stories").add(story);
 },
 function(error) {
 // fail
 });

 } else {
 object.relation("stories").add(story);
 }
 } else {
 var newtag = new TagClass();
 newtag.set("title", item);
 newtag.set("lastEditor", request.user);
 newtag.set("isLocation", true);
 newtag.save().then(
 function(newtag) {
 newtag.relation("stories").add(story);
 },
 function(error) {
 // fail
 });

 }
 });
 });
 });
 */
/*  Parse.Cloud.beforeSave("Tag", function(request, response)){
 var query = new Parse.Query("Tag");
 query.equalTo("title", request.object.get("title"));
 query.first({
 success: function(count) {
 if(count > 0)
 response.error("Tag already exists");
 else
 response.success();
 },
 error: function(error){
 response.error(error);
 }
 })
 }; */

function isEmpty(str) {
    return str.replace(/^\s+|\s+$/g, '').length == 0;
}

// Parse object to standart  object
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