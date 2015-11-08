var assert = require('assert'),
    path = require('path'),
    fs = require('fs'),
    request = require('request'),
    http = require('http'),
    lib = require('../'),
    Handlebars = require('handlebars');

describe('Partials', function() {
  var port = 5000,
      server,
      views;

  // Create a fresh server and registry before each test.
  beforeEach(function(done) {
    views = lib.createRegistry();
    views.register(path.join(__dirname, 'fixtures/views'));
    server = http.createServer();
    server.listen(port, done);
  });

  // Close the server after each test.
  afterEach(function(done) {
    server.close(done);
  });

  it('should support using handlebar\'s native partials', function(done) {
    Handlebars.registerPartial('name', fs.readFileSync(path.join(__dirname, 'fixtures/views/partials/name.hbs'), 'utf8'));
    server.on('request', function(req, res) {
      views.render(req, res, 'hello-turtle', {first: 'Casey', last: 'Jones'});
    });
    request('http://localhost:' + port + '/', function(err, res, body) {
      assert.ifError(err);
      assert.equal(res.statusCode, 200);
      assert.equal(body, '<html><body><h1>Hello Casey Jones</h1></body></html>', 'template was rendered incorrectly');
      done();
    });
  });

});