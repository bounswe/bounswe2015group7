var assert = require('assert'),
    path = require('path'),
    request = require('request'),
    http = require('http'),
    lib = require('../');

describe('Registry', function() {
  var port = 5000,
      server,
      views;

  // Create a fresh server and registry before each test.
  beforeEach(function(done) {
    views = lib.createRegistry();
    views.register(path.join(__dirname, 'fixtures/views'));
    views.register('alt', path.join(__dirname, 'fixtures/views-alt'));
    server = http.createServer();
    server.listen(port, done);
  });

  // Close the server after each test.
  afterEach(function(done) {
    server.close(done);
  });

  it('should find and render templates in a registered namespace', function(done) {
    server.on('request', function(req, res) {
      views.render(req, res, 'alt/hello', {name: 'Splinter'});
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      assert.ifError(err);
      assert.equal(res.statusCode, 200);
      assert.equal(body, '<html><body><h1>Hi Splinter</h1></body></html>', 'template was rendered incorrectly');
      done();
    });
  });

  it('should find and render templates with an alternate extension', function(done) {
    // Prime the views registry cache with the .hbs version of alt/hello.
    views.find('alt/hello', {ext: 'hbs'});

    // The callback for this route specifically asks for the .html version.
    server.on('request', function(req, res) {
      views.render(req, res, 'alt/hello', {name: 'Shredder', ext: 'html'});
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      assert.ifError(err);
      assert.equal(res.statusCode, 200);
      assert.equal(body, '<html><body><h1>Hey Shredder</h1></body></html>', 'template was rendered incorrectly');
      done();
    });
  });

});