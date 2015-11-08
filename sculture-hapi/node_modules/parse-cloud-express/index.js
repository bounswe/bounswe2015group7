/*
parse-cloud - This module adds the Parse.Cloud namespace to the Parse JS SDK,
  allowing some code previously written for Cloud Code to run in a Node environment.

Database triggers and cloud functions defined with Parse.Cloud methods will be wrapped 
  into an Express app, which can then be mounted on your main Express app.  These
  routes can then be registered with Parse via the Webhooks API.

See our example project

*/
var express = require('express');
var bodyParser = require('body-parser');
var Parse = require('parse/node').Parse;
var Request = require('request');

var Routes = {
  'beforeSave': [],
  'afterSave': [],
  'beforeDelete': [],
  'afterDelete': [],
  'function': []
};

// Make sure to set your Webhook key via heroku config set or local environment variable
var webhookKey = process.env.PARSE_WEBHOOK_KEY;

// Middleware to enforce security using the Webhook Key
function validateWebhookRequest(req, res, next) {
  if (req.get('X-Parse-Webhook-Key') !== webhookKey) return errorResponse(res, 'Unauthorized Request.');
  next();
}

// Middleware to inflate a Parse.Object passed to a webhook route
function inflateParseObject(req, res, next) {
  var object = req.body.object;
  req.object = Parse.Object.fromJSON(object);
  next();
}

// Middleware to create the .success and .error methods expected by a Cloud Code function
function addParseResponseMethods(req, res, next) {
  res.success = function(data) {
    successResponse(res, data);
  };
  res.error = function(data) {
    errorResponse(res, data);
  };
  next();
}

// Middleware to promote the cloud function params to the request object
function updateRequestFunctionParams(req, res, next) {
  req.params = req.body.params;
  next();
}

// Middleware to inflate a Parse.User if provided, and promote the master key option to the request
function inflateParseUser(req, res, next) {
  if (req.body.user) {
    if (req.body.user.className === undefined) {
      req.body.user.className = "_User";
    }
    req.user = Parse.Object.fromJSON(req.body.user);
  }
  req.master = req.body.master;
  next();
}


var successResponse = function(res, data) {
  data = data || true;
  res.status(200).send({ "success" : data });
}

var errorResponse = function(res, message) {
  message = message || true;
  res.status(200).send({ "error" : message });
}

var emptyResponse = function(req, res, next) {
  res.status(200).send({});
  next();
};

var app = express();
var jsonParser = bodyParser.json();

// All requests handled by this app will require the correct webhook key header
app.use(validateWebhookRequest);
app.use(jsonParser);

var beforeSave = function(className, handler) {
  app.post('/beforeSave_' + className, addParseResponseMethods, inflateParseObject, inflateParseUser, handler);
  Routes['beforeSave'].push(className);
};

var afterSave = function(className, handler) {
  app.post('/afterSave_' + className, addParseResponseMethods, inflateParseObject, inflateParseUser, emptyResponse, handler);
  Routes['afterSave'].push(className);
};

var beforeDelete = function(className, handler) {
  app.post('/beforeDelete_' + className, addParseResponseMethods, inflateParseObject, inflateParseUser, handler);
  Routes['beforeDelete'].push(className);
}

var afterDelete = function(className, handler) {
  app.post('/afterDelete_' + className, addParseResponseMethods, inflateParseObject, inflateParseUser, emptyResponse, handler);
  Routes['afterDelete'].push(className);
}

var define = function(functionName, handler) {
  app.post('/function_' + functionName, updateRequestFunctionParams, addParseResponseMethods, inflateParseUser, handler);
  Routes['function'].push(functionName);
};

var httpRequest = function(options) {
  var promise = new Parse.Promise();
  Request(options, function(error, response, body) {
    if (error) {
      console.log('Parse.Cloud.httpRequest failed with error:');
      console.log(error);
      promise.reject(response);
    } else {
      response.text = body;
      promise.resolve(response);
    }
  });
  return promise;  
}

Parse.Cloud.beforeSave = beforeSave;
Parse.Cloud.afterSave = afterSave;
Parse.Cloud.beforeDelete = beforeDelete;
Parse.Cloud.afterDelete = afterDelete;
Parse.Cloud.define = define;
Parse.Cloud.httpRequest = httpRequest;

module.exports = {
  Parse: Parse,
  successResponse: successResponse,
  errorResponse: errorResponse,
  app: app,
  Routes: Routes
};
