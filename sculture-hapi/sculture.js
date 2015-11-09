var hapi = require('hapi');
GLOBAL.Parse = require('parse/node').Parse; // add parse
var Inert = require('inert');
var CloudParse = require('parse-cloud-express').Parse;
Parse.initialize("yJboQpdSMbOBNsvjN3KU43SWvsFIDGhyYP1QZVP4", "Uc65Wz5kLYIvUpRdspNTRbI5vy1MLSvOmsNAmZbu");
// Create hapi server instance
var server = new hapi.Server();

// add connection parameters
server.connection({
    host: 'localhost',
    port: 3000
});
server.register(Inert, function () {});

server.register(require('vision'), function (err) {
    if (err) {
        throw err;
    }
    server.views({
        engines: {
            html: require('handlebars')
        },
        path: 'views',
        helpersPath: 'views/helpers',
        partialsPath: 'views/partials'
    });
});


// create your routes, currently it's just one
var routes = require("./backend/backend_routes.js");

server.register(require('hapi-auth-cookie'), function (err) {

    server.auth.strategy('session', 'cookie', {
        password: 'secret',
        cookie: 'sid-example',
        redirectTo: '/login',
        isSecure: false,
        validateFunc: function (request, username, password, callback) {
            var user = request.auth.credentials;
            if (!user) {
                return callback(null, false);
            }
            var currentUser = Parse.User.current();
            if (currentUser) {
                callback(null, true, { id: user.id, name: user.name });
            } else {
                callback(null, false, { id: user.id, name: user.name });
            }
        }
    });
});

// tell your server about the defined routes
server.route(routes);
// Start the server
server.start(function () {
    // Log to the console the host and port info
    console.log('Server started at: ' + server.info.uri);
});