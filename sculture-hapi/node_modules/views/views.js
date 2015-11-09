var path = require('path'),
    fs = require('fs'),
    existsSync = fs.existsSync || path.existsSync,
    url = require('url'),
    util = require('util'),
    async = require('async'),
    clone = require('clone'),
    cons = require('consolidate'),
    glob = require('glob'),
    merge = require('tea-merge');

// Create a views registry to use manually.
exports.createRegistry = function(root, options) {
  return new Views(root, options);
};

// Use a views registry as middleware.  Adds res.render() and res.renderStatus()
exports.middleware = function(views) {
  return function(req, res, next) {
    function renderCallback(err, str) {
      if (err) {
        throw err;
      }
      if (!res.getHeader('content-type')) {
        res.setHeader('content-type', 'text/html');
      }
      res.write(str);
      res.end();
    }
    res.render = function(view, options) {
      options = options || {};
      views.render.call(views, req, res, view, options, renderCallback);
    };
    res.renderStatus = views.renderStatus.bind(views, req, res);
    next();
  };
};

// Export consolidate for hackery.
exports.consolidate = cons;

// Export the Views class.
exports.Views = Views;

/**
 * Views constructor.
 */
function Views(root, options) {
  this.conf = {};

  if (!options && (typeof root !== 'string')) {
    options = root;
    root = null;
  }
  if (options) {
    this.conf = options;
  }
  this.conf = merge({}, {
    layout: 'layout',
    ext: 'hbs',
    engine: 'handlebars'
  }, this.conf);

  this._parsedUrls = {};
  this._helpers = {};
  this._registry = [];
  this._cache = {};

  if (root) {
    this.register(root);
  }
}

/**
 * Log output unless silent.
 */
Views.prototype.log = function() {
  if (!this.conf.silent) {
    console.log.apply(console, arguments);
  }
};

/**
 * Cached url parser.
 */
Views.prototype._parseUrl = function(urlToParse) {
  if (!this._parsedUrls[urlToParse]) {
    this._parsedUrls[urlToParse] = url.parse(urlToParse);
  }
  return this._parsedUrls[urlToParse];
};

/**
 * Stringify a path, preparing it to be converted to new RegExp.
 *
 * @param path {String|RegExp} The string or regular expression to normalize.
 * @return {String} The normalized string representation of the path.
 */
Views.prototype._stringifyPath = function(path) {
  // Convert to string and clean up if its a regex.
  if (util.isRegExp(path)) {
    path = path.toString().replace(/^\//, '').replace(/\/$/, '');
  }
  // Make sure the path will be matched in-full.
  else {
    path = '^' + path + '$';
  }
  return path;
};

/**
 * Render a view.
 *
 * @param view {String} The path to a template, relative to ANY registered
 *   views namespace, excluding the template extension.
 * @param opts {Object} Template data and/or engine-specific options.
 * @param [cb] {Function} (err, str) Callback to handle the rendered text. A
 *   default callback is provided which calls `this.res.html(str)`.
 */
Views.prototype.render = function(req, res, view, options, cb) {
  var views = this,
      defaults = this.conf,
      conf = clone(this.conf, false),
      tasks = [];

  // Support callback function as second argument.
  if (typeof options === 'function') {
    cb = options, options = {};
  }

  // If options is a string, assign it to options.content.
  if (typeof options === 'string') {
    options = {content: options};
  }

  // Merge options into conf.
  if (options) {
    merge(conf, options);
  }

  // Default render callback.
  cb = cb || function(err, str) {
    if (err) throw err;

    // Fallback to writing the content as html.
    res.writeHead(res.statusCode, {"Content-Type": "text/html"});
    res.write(str);
    res.end();
  };

  // Find the full path to the template.
  views.find(view, conf, function(err, template) {
    if (err) return cb(err);
    views._processHelpers(req, res, conf, function(err, conf) {
      if (err) return cb(err);
      cons[conf.engine](template, clone(conf, false), function(err, str) {
        if (err) return cb(err);

        var layout = conf.layout,
            layoutConf = merge({}, conf, defaults, {content: str, layout: layout}),
            template;

        // If we have a layout, and this is not the layout, render this
        // content inside the layout.
        if (layout && view !== layout) {
          try {
            views.render(req, res, layout, layoutConf, cb);
            return;
          }
          catch (err) {
            if (err.code !== 'ENOENT') {
              return cb(err);
            }
          }
        }
        cb(null, str);
      });
    });
  });
};

/**
 * Render a status code page.
 *
 * @param code {Number} Status code
 * @param [message] {String} A custom error message.
 */
Views.prototype.renderStatus = function(req, res, code, message) {
  var messages = {
    403: 'Access denied',
    404: 'Page not found',
    500: 'Server error'
  };

  if (!message && messages[code]) {
    message = code + ' - ' + messages[code];
  }

  res.statusCode = code;

  try {
    this.render(req, res, 'status-' + code, {message: message});
  }
  catch (err) {
    res.end(message);
  }
};

/**
 * Register a views namespace.
 *
 * A views namespace associates a template prefix with a root directory
 * and some default options. A great use-case for multiple views
 * namespaces is when application plugins would like to expose views that
 * the main app can render.
 *
 * @param prefix {String} A template path prefix, no trailing slash.
 * @param root {String} The absolute path to directoy of views being
 *   registered.
 * @param opts {Object} Default options for all the views in this directory.
 *   Typically, this would include a custom templating engine and extension.
 */
Views.prototype.register = function(prefix, root, opts) {
  var views = this,
      reg = this._registry,
      cache = this._cache;

  if (arguments.length < 2) {
    root = prefix;
    prefix = '';
    opts = {};
  }
  if (arguments.length < 3) {
    if (typeof root !== 'string') {
      opts = root;
      root = prefix;
      prefix = '';
    }
    else {
      opts = {};
    }
  }

  // Confirm the root path exists.
  if (existsSync(root)) {
    // If a layout was passed in the options, confirm it exists.
    if (opts.layout) {
      if (!existsSync(path.join(root, opts.layout + '.' + opts.ext))) {
        // Its ok if the default doesn't exist.
        if (opts.layout === 'layout') {
          opts.layout = false;
        }
        else {
          throw new Error('The layout does not exist (' + opts.layout + ').');
        }
      }
    }

    // Add the new namespace.
    reg.push({
      prefix: prefix,
      root: root,
      opts: opts || {}
    });

    // Clear the cache.
    cache = {};
  }
  else {
    throw new Error('Path does not exist for view namespace: ' + prefix);
  }
};

/**
 * Find the real path to a template.
 *
 * Fetch the path for a view, searching through all registered namespaces.
 * Namespaces are searched in reverse order of their registration.
 * Also, merges in the namespace default options.
 *
 * @param target {String} A namespaced (prefix) path to a view.
 * @param opts {Object} An options object that will have the namespace
 *   defaults merged in.
 * @param [cb] {Function} (err, path) Callback to receive the path.
 */
Views.prototype.find = function(target, conf, cb) {
  var key, check, namespace, regex, full, ext, tempOpts;
  var reg = this._registry;
  var cache = this._cache;

  // Create a unique key for this target (vary on extension if supplied).
  key = target;
  if (conf.ext) {
    key = key + ':' + conf.ext;
  }

  // Check if the path exsts in the cache.
  if (!cache[key]) {
    // Loop through registered namespaces and check if our path matches one.
    check = reg.slice(0);
    while (namespace = check.pop()) {
      // Check for existence of the namespace prefix.
      if (target.indexOf(namespace.prefix) === 0) {
        regex = new RegExp(namespace.prefix + '\/?');
        ext = conf.ext || namespace.opts.ext;
        full = path.resolve(namespace.root, target.replace(regex, '')) + '.' + ext;
        if (existsSync(full)) {
          cache[key] = {
            path: full,
            opts: clone(namespace.opts, false)
          };
          break;
        }
      }
    }
  }

  if (cache[key]) {
    if (cb) return cb(null, cache[key].path);
    return cache[key].path;
  }
  else {
    var err = new Error('No registered views matched the path: ' + target);
    err.code = 'ENOENT';
    if (cb) return cb(err);
    throw err;
  }
};

/**
 * Find a views directory.
 *
 * Fetch the full path to a views directory, searching through all registered
 * namespaces.  Namespaces are searched in reverse order of their
 * registration.
 *
 * @param target {String} A namespaced (prefix) path to a views directory.
 */
Views.prototype.findDir = function(target) {
  var namespace, regex, full, stats;
  var reg = this._registry;
  var cache = this._cache;

  // Check if the path exsts in the cache.
  if (!cache[target]) {
    for (var i = reg.length - 1; i >= 0; i--) {
      namespace = reg[i];

      // Check for the existence of the namespace prefix.
      if (target.indexOf(namespace.prefix) === 0) {
        regex = new RegExp(namespace.prefix + '\/?');
        full = path.resolve(namespace.root, target.replace(regex, ''));
        if (existsSync(full)) {
          // Check if it is an actual directory.
          if (fs.statSync(full).isDirectory()) {
            cache[target] = full;
            break;
          }
        }
      }
    }
  }

  return cache[target] || false;
};

/**
 * Register a views helper.
 *
 * A helper can either be an object literal that will be merged with the
 * other template data, or a function. Dynamic helper functions allow
 * you to add template data based on a request.
 *
 * Dynamic helper functions will be called in the router scope (this.req,
 * this.res) during the view rendering phase. Helper funtions must accept a
 * callback to call with the arguments `callback(err, data)`.
 *
 * ####Example dynamic helper function:
 *
 *     function (req, res, callback){
 *       var user = req.user;
 *       return callback(null, { user: user });
 *     }
 *
 * @param [match] {String|RegExp} If specified, the helper will only be
 *   applied for urls that match this pattern.
 * @param helper {Object|Function} Template data or a helper function.
 */
Views.prototype.helper = function(match, helper) {
  if (arguments.length === 1) {
    helper = match;
    match = /.*/;
  }

  match = this._stringifyPath(match);

  // Instantiate path array.
  if (!this._helpers[match]) {
    this._helpers[match] = [];
  }

  this._helpers[match].push(helper);
};

/**
 * Clear all views helpers.
 *
 * @param [match] {String|RegExp} Only clear helpers registerd under this
 *   match pattern.
 */
Views.prototype.clearHelpers = function(match) {
  if (match) {
    match = this._stringifyPath(match);
    if (this._helpers[match]) {
      delete this._helpers[match];
    }
  }
  else {
    this._helpers = {};
  }
};

/**
 * Process views helpers.
 *
 * @param  templateData {Object} Template data to modify and invoke the callback with.
 * @param  [url] {String|RegExp} Limit to helpers that match this url.
 * @param  callback {Function} (err) Callback to be invoked after all registered
 *   helpers have been processed.
 */
Views.prototype._processHelpers = function(req, res, conf, callback) {
  var views = this,
      tasks = [],
      reqPath = this._parseUrl(req.url).pathname,
      helpers = {};

  // Check for cached helpers data for this requests so we don't run
  // them more than once.
  if (req._viewsHelpersData) {
    conf = merge({}, req._viewsHelpersData, conf);
    return callback(null, conf);
  }

  Object.keys(views._helpers).forEach(function(match) {
    if (reqPath.match(new RegExp(match))) {
      views._helpers[match].forEach(function(helper) {
        tasks.push(function(done) {
          if (typeof helper === 'function') {
            helper.call(views, req, res, function(err, data) {
              if (data) {
                merge(helpers, data);
              }
              done(err);
            });
          }
          else {
            merge(helpers, helper);
            done(null);
          }
        });
      });
    }
  });

  async.parallel(tasks, function(err) {
    if (err) return callback(err);
    var processed = views._processHelpersJSON(helpers);
    conf = merge({}, processed, conf);
    req._viewsHelpersData = processed;
    callback(err, conf);
  });
};

/**
 * Process _json_ property in views helpers data.
 *
 * If one or more of the views helpers added a `_json_` object, stringify
 * each key/value pair and expose the values as helpers data.
 *
 * Does not overwrite existing data keys.
 *
 * The common use-case for this is to expose data to the client-side
 * javascript environtment.
 *
 * @param data {Object} The helpers data to process.
 */
Views.prototype._processHelpersJSON = function(data) {
  if (data.hasOwnProperty('_json_')) {
    for (var key in data._json_) {
      if (!data.hasOwnProperty(key)) {
        data[key] = JSON.stringify(data._json_[key])
          // for security, escape script tags.
          .replace(/<(\/?)script/g, '<$1scr"+"ipt');
      }
    }
  }
  return data;
};
