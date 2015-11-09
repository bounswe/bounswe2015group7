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
            return reply.view('frontend_homepage', request.auth);
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
        config: {
            handler: function (request, reply) {
                console.log(request.auth);
                request.auth.session.clear();
                if (request.auth.isAuthenticated) {
                    console.log("c");

                    return reply.redirect('/', request.auth);
                }
                Parse.User.logIn(request.payload["form-email"], request.payload["form-password"], {
                    success: function(user) {
                        console.log("a");
                        request.auth.session.set(user);
                        user.isAuthenticated = true;
                        return reply.redirect('/', request.auth);
                        // Do stuff after successful login.
                    },
                    error: function(user, error) {
                        console.log("b");
                        console.log(error);
                        return reply.redirect('/', error);

                        // The login failed. Check error to see why.
                    }
                });
            },
            auth: {
                mode: 'try',
                strategy: 'session'
            },
            plugins: {
                'hapi-auth-cookie': {
                    redirectTo: false
                }
            }
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