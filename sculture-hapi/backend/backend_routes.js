/**
 * Created by Atakan Ar�kan on 09.11.2015.
 */
var Bcrypt = require('bcrypt');
var User = Parse.Object.extend("User");

module.exports = [
    {
        method: 'GET',
        path: '/',
        config: {
            auth: {
                mode: 'try',
                strategy: 'session'
            },
            plugins: {
                'hapi-auth-cookie': {
                    redirectTo: false
                }
            },
            handler: function (request, reply) {
                console.log(request.auth);
                return reply.view('frontend_homepage', {
                    credentials: request.auth.credentials
                });
            }
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

                if (request.auth.isAuthenticated) {
                    return reply.redirect('/');
                }

                Parse.User.logIn(request.payload["form-email"], request.payload["form-password"], {
                    success: function (user) {
                        request.auth.session.set(user);
                        return reply.redirect('/addstory');
                    },
                    error: function (user, error) {
                        return reply.view('frontend_homepage', {
                            error: error
                        });
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
        method: 'GET',
        path: '/logout',
        handler: function (request, reply) {
            request.auth.session.clear();
            reply.redirect('/');
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
            Bcrypt.genSalt(10, function (err, salt) {
                Bcrypt.hash(pw, salt, function (err, hash) {
                    Bcrypt.compare(pw2, hash, function (err, res) {
                        if (!res) { //error case, passwords dont match!
                            return reply.view('frontend_homepage', {
                                error: {
                                    message: "Your passwords dont match!"
                                }
                            });
                        }
                        else {
                            Parse.User.signUp(username, pw, {email: email}, {
                                success: function (user) {
                                    request.auth.session.set(user); //don't ask for the user to login again
                                    return reply.redirect('/'); //redirect home
                                },
                                error: function (user, error) {
                                    return reply.view('frontend_homepage', { //show error
                                        error: error
                                    });
                                }
                            });
                        }
                    });
                });
            });

        }
    },
    {
        method: 'GET',
        path: '/sex/{id}',
        handler: function (request, reply) {
            var id = request.params["id"];
            Parse.Cloud.run('story_get', {id: id}, {
                success: function (story) {
                    console.log(story);
                    reply.view(html, story);
                },
                error: function (error) {
                }
            });
        }
    },
    {
        method: 'GET',
        path: '/addstory',
        handler: function (request, reply) {
            var user = request.auth.credentials;
            console.log(request.auth);
            if (user == null) {
                return reply.view('frontend_homepage', { //show error
                    error: {
                        message: "You are not logged in"
                    }
                });
            }
            reply.view('add_story', request.auth);

        }
    },
    {
        method: 'POST',
        path: '/{id}/addstory',
        handler: function (request, reply) {
            var id = request.params["user-id"];
            var content = request.params["story-content"];
            var tags = request.params["story-tags"];
            var title = request.params["story-title"];
            Parse.Cloud.run('story_create', {
                id: id,
                content: content,
                tags: tags,
                title: title
            }, {
                success: function (story) {
                    console.log(story);
                    reply.view(html, story);
                },
                error: function (error) {
                }
            });
        }
    }
    // todo STORY EKLEME HANDLER/SAYFA
    // todo STORY GOSTERME HANDLER/SAYFA
    // todo TAGE GORE ARAMA HANDLER/SAYFA
    // TODO
];