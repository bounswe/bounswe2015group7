// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
    response.success("Hello world!");
});


Parse.Cloud.define("story_create", function(request, response) {
    var content = request.params.content;
    var title = request.params.title;
    var tags = request.params.tags;
    var locTags = request.params.locationTags;

    var valid_tags = [];
    //var valid_locTags = [];

    var isError = false;
    var errors = [];

    // Check errors
    if (request.user == null) {
        errors.push("You should be logged in");
        isError = true;
    }
    if (content == null || isEmpty(content)) {
        errors.push("Content cannot be empty");
        isError = true;
    }
    if (title == null || isEmpty(title)) {
        errors.push("Title cannot be empty");
        isError = true;
    }
    if (tags == null || !(tags instanceof Array)) {
        errors.push("Tags should be an array");
        isError = true;
    }
/*     if (locTags == null || !(locTags instanceof Array)) {
        errors.push("Location tags should be an array");
        isError = true;
    } */

    if (tags instanceof Array) {
        var i;
        for (i = 0; i < tags.length; i++) {
            if (!isEmpty(tags[i]))
                valid_tags.push(tags[i].trim());
        }
        if (valid_tags.length < 1) {
            errors.push("There should be at least one tag");
        }
    }

   /*  if (locTags instanceof Array) {
        var i;
        for (i = 0; i < locTags.length; i++) {
            if (!isEmpty(locTags[i]))
                valid_locTags.push(locTags[i].trim());
        }
        if (valid_locTags.length < 1) {
            errors.push("There should be at least one tag");
        }
    } */


    if (isError) {
        response.error(errors);
    }

    // There is no errors
    if (!isError) {
        var StoryClass = Parse.Object.extend("Story");
        var story = new StoryClass();
        story.set("content", content.trim());
        story.set("title", title.trim());
        story.set("owner", request.user);
        story.set("lastEditor", request.user);
       /*  story.set("locationTagsArray", valid_locTags); */
        story.set("tagsArray", valid_tags);

        story.save(null, {
            success: function(story) {
                response.success(story);
            },
            error: function(error) {
                response.error(error);
            }
        });
    }

});

Parse.Cloud.define("search_story_withTag", function(request, response) {
	var tag = request.params.tag;
	var tags;
	var querry = new Parse.Query(Parse.Object.extend("Story"));
	querry.equalTo("tagsArray", tag);
	querry.find({
		success: function(story) {
			response.success(story);
		}
	});
	
/* 	var query2 = new Parse.Query(Parse.Object.extend("Story"));
	query2.equalTo("locationTagsArray", tag);
	query2.find({
		success: function(story){
			response.success(story);
		}
	}) */
	
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