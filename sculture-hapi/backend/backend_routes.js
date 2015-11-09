/**
 * Created by Atakan Ar�kan on 09.11.2015.
 */
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
                return reply.view('frontend_homepage', {
                    credentials: request.auth.credentials
                });
            }
        }
    },
    { //handle css,js etc.
        path: "/{path*}",
        method: "GET",
        handler: {
            directory: {
                path: "./views",
                listing: false,
                index: false
            }
        }
    },
    { //handle css,js etc.
        path: "/story/{path*}",
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

                Parse.User.logIn(request.payload["form-username"], request.payload["form-password"], {
                    success: function (user) {
                        request.auth.session.set(user);
                        return reply.redirect('/');
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
            if (pw != pw2) { //error case, passwords dont match!
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
        }
    },
    {
        method: 'GET',
        path: '/story/{id}',
        handler: function (request, reply) {
            var id = request.params["id"];
            Parse.Cloud.run('tempstory_get', {id: id}, {
                success: function (story) {
                    console.log(story);
                    reply.view('view_story', story);
                },
                error: function (error) {
                    return reply.view('frontend_homepage', { //show error
                        error: {
                            message: "Cannot find story!"
                        }
                    });
                }
            });
        }
    },

    {
        method: 'GET',
        path: '/addstory',
        config: {
            handler: function (request, reply) {
                var user = request.auth.credentials;
                if (user == null) {
                    return reply.view('frontend_homepage', { //show error
                        error: {
                            message: "You are not logged in"
                        }
                    });
                }
                reply.view('add_story', request.auth);

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
        path: '/{id}/addstory',
        config: {
            handler: function (request, reply) {
                var id = request.params["id"];
                var content = request.payload["story-content"];
                var tags = request.payload["story-tags"].split(" ");
                var title = request.payload["story-title"];
                console.log("id: " + id);
                console.log("content: " + content);
                console.log("tags: " + tags);
                console.log("title: " + title);
                Parse.Cloud.run('tempstory_create', {
                    userid: id,
                    content: content,
                    tags: tags,
                    title: title
                }, {
                    success: function (story) {
                        var url = "/story/" + story.id;
                        reply.redirect(url, story);
                    },
                    error: function (error) {
                        return reply.view('frontend_homepage', { //show error
                            error: {
                                message: "Cannot add story!"
                            }
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

    }
    // todo STORY EKLEME HANDLER/SAYFA
    // todo STORY GOSTERME HANDLER/SAYFA
    // todo TAGE GORE ARAMA HANDLER/SAYFA
    // TODO
];