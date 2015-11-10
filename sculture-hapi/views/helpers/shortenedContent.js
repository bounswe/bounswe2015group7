/**
 * Created by Atakan Arıkan on 10.11.2015.
 */
var Handlebars = require("handlebars");
Handlebars.registerHelper("shortenedContent", function(content) {
    if(content.length < 500) return content;
    return content.substring(0, 500) + "...";
});