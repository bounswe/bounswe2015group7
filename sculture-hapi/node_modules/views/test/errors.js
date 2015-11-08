var assert = require('assert'),
    path = require('path'),
    request = require('request'),
    http = require('http'),
    lib = require('../');

describe('Errors', function() {
  var views;

  // Create a fresh server and registry before each test.
  beforeEach(function() {
    views = lib.createRegistry();
  });

  it('should throw an error if the registration root does not exist', function() {
    assert.throws(
      function() {
        views.register('bad');
      },
      /Path does not exist/,
      'The error was not thrown or the message was wrong.'
    );
  });

  it('should throw an error if the layout doesn\'t exist', function() {
    assert.throws(
      function() {
        views.register(path.join(__dirname, 'fixtures/views'), {layout: 'bad'});
      },
      /layout does not exist/,
      'The error was not thrown or the message was wrong.'
    );
  });

  it('should throw an a view cannot be found', function() {
    assert.throws(
      function() {
        views.register(path.join(__dirname, 'fixtures/views'));
        views.render({}, {}, 'bad');
      },
      /No registered views matched/,
      'The error was not thrown or the message was wrong.'
    );
  });

});