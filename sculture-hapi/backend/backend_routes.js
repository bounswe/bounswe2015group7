/**
 * Created by Atakan Arýkan on 09.11.2015.
 */
//var Bcrypt = require('bcrypt');
var User = Parse.Object.extend("User");
module.exports = [
    {
        method: 'GET',
        path: '/',
        handler: function (request, reply) {

            // Render the view with the custom greeting
            console.log(Parse.User.current());
            if(Parse.User.current()){ //logged in user
                console.log("a");
                return reply.view('frontend_homepage');
            }else{ // noob user
                console.log("b");
                return reply.view('frontend_homepage');
            }
            return reply.view('frontend_homepage');
        }
    },
    { //handle css,js etc.
        path: "/{public*}",
        method: "GET",
        handler: {
            directory: {
                path: "./views",
                listing: false,
                index: false
            }
        }
    },
    {
        method: 'POST',
        path: '/login',
        handler: function (request, reply) {
            var username = request.payload["form-email"];
            var pw = request.payload["form-password"];
            Parse.User.logIn(username, pw, {
                success: function(user) {
                    console.log(user);
                    return reply.view('frontend_homepage', user);
                },
                error: function(user, error) {
                    var err = {
                        errmsg: error.message
                    };
                    return reply.view('frontend_homepage', err);
                }
            });
        }
    },
    {
        method: 'POST',
        path: '/signup',
        handler: function (request, reply) {
            var email = request.payload["form-email"];
            var username = request.payload["form-username"];
            var pw = request.payload["form-password"];
            var pw2 = request.payload["form-retypedpassword"];

            Parse.User.signUp(username, pw, {email: email}, {
                success: function (user) {
                    return reply.view('frontend_homepage', user);
                },
                error: function (user, error) {
                    var err = {
                        errmsg: error.message
                    };
                    return reply.view('frontend_homepage', err);
                }
            });
        }
    }
];