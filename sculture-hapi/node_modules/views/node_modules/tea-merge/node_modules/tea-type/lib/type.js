/*!
 * tea-type
 * Copyright(c) 2012 jake luer <jake@qualiancy.com>
 * MIT Licensed
 */

/*!
 * Primary Exports
 */

var exports = module.exports = getType;

/*!
 * Detectable javascript natives
 */

var natives = {
    '[object Array]': 'array'
  , '[object RegExp]': 'regexp'
  , '[object Function]': 'function'
  , '[object Arguments]': 'arguments'
  , '[object Date]': 'date'
};

/*!
 * Custom tests for `is`
 */

var tests = {};

/**
 * Use several different techniques to determine
 * the type of object being passed.
 *
 * Provided as primary export.
 *
 * @param {Mixed} object
 * @return {String} object type
 * @api public
 */

function getType (obj) {
  var str = Object.prototype.toString.call(obj);
  if (natives[str]) return natives[str];
  if (obj === null) return 'null';
  if (obj === undefined) return 'undefined';
  if (obj === Object(obj)) return 'object';
  return typeof obj;
}

/**
 * Add a test to for the `.is()` assertion.
 *
 * @param {String} type
 * @param {RegExp|Function} test
 * @api public
 */

exports.test = function (type, test) {
  if (arguments.length === 1) return tests[type];
  tests[type] = test;
  return this;
};

/**
 * Assert that an object is of type. Will first
 * check natives, and if that does not pass it will
 * use the user defined custom tests.
 *
 * @param {Mixed} object
 * @param {String} type
 * @return {Boolean} result
 * @api public
 */

exports.is = function (obj, type) {
  if (type === getType(obj)) return true;

  if (tests[type]) {
    var test = tests[type];
    if ('regexp' === getType(test)) return test.test(obj);
    if ('function' === getType(test)) return test(obj);
  }

  return false;
};
