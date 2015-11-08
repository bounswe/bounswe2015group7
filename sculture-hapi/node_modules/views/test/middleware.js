var assert = require('assert'),
    path = require('path'),
    request = require('request'),
    http = require('http'),
    middler = require('middler'),
    lib = require('../');

describe('Middleware', function() {
  var port = 5000, server, middleware, views;

  // Create a fresh server and registry before each test.
  beforeEach(function(done) {
    server = http.createServer();
    views = lib.createRegistry(path.join(__dirname, 'fixtures/views'), {silent: true});
    middleware = middler(server).add(lib.middleware(views));
    server.listen(port, done);
  });

  // Close the server after each test.
  afterEach(function(done) {
    server.close(done);
  });

  it('should be able to render data in the layout', function(done) {
    middleware.add(function(req, res) {
      res.render('hello', {name: 'Donatello', optional: 'Greeting:'});
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      assert.ifError(err);
      assert.equal(res.statusCode, 200);
      assert.equal(body, '<html><body>Greeting:<h1>Hello Donatello</h1></body></html>', 'template was rendered incorrectly');
      done();
    });
  });

  it('should be able to render status code templates', function(done) {
    middleware.add(function(req, res) {
      res.renderStatus(404);
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      assert.ifError(err);
      assert.equal(res.statusCode, 404);
      assert.equal(body, '<html><body><h1>404</h1><p>404 - Page not found</p></body></html>');
      done();
    });
  });

  it('should throw an error if the template cannot be found', function(done) {
    middleware.add(function(req, res) {
      try {
        res.render('nothere', {name: 'Donatello', optional: 'Greeting:'});
        assert(false, 'Did not throw');
      }
      catch (e) {
        assert.equal(e.code, 'ENOENT');
        res.renderStatus(500);
      }
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      done();
    });
  });

});
