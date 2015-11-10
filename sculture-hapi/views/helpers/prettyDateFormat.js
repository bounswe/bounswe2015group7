/**
 * Created by Atakan Arıkan on 10.11.2015.
 */
var Handlebars = require("handlebars");
Handlebars.registerHelper("prettyDateFormat", function(date) {
    var myDate = new Date(date);
    return myDate.toDateString();
});