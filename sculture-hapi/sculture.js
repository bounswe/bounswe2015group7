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
server.register(Inert, function () {
});

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
        isSecure: false,
        clearInvalid: true,
        ttl: 60 * 1000 * 60, // 1 hour session time
        validateFunc: function (request, session, callback) {

            if (!session) {
               // request.auth.session.clear();
                return callback("Session not found", false);
            }
            return callback(null, true, session);
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