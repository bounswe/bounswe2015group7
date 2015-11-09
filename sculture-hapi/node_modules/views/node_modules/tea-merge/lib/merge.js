var type = require('tea-type');

/**
 * ### merge (destination, source, ...)
 *
 * For each source, shallow merge its key/values to the
 * destination. Sources are read in order, meaning the same
 * key in a later source will overwrite the key's value set
 * earlier.
 *
 * Also, this tool only supports objects and arrays. Furthermore,
 * the destination and all sources must be of the same type.
 *
 * ```js
 * var merge = require('tea-merge');
 *
 * // sample objects
 * var a = { hello: 'universe', arr: [ { a: 'a' } ] }
 *   , b = { speak: 'loudly', arr: [ { b: 'b' }, { c: 'c' } };
 *
 * merge(a, b);
 * a.should.deep.equal({
 *     hello: 'universe'
 *   , speak: 'loudly'
 *   , arr: [
 *         { a: 'a', b: 'b' }
 *       , { c: 'c' }
 *     ]
 * });
 * ```
 *
 * When merging objects, it is expected that if the
 * key from the source already exists in the destination,
 * the existing value in the source supports the same type of
 * iteration as in the destination. If they cannot, a
 * `Incompatible merge scenario.` error will be thrown.
 *
 * ##### Rules
 *
 * - Non-iterable values can be replaced with other
 * non-iterable values: strings, numbers, etc.
 * - Iterable values cannot replace non-iterable
 * values; objects can't replace string, arrays, can't
 * replace numbers, etc.
 * - Non-iterable values cannot replace iterable
 * values; numbers can't replace objects, strings can't
 * replace arrays, etc.
 *
 * @param {Array|Object} destination
 * @param {Array|Object} sources ...
 * @return {Object} destination merge
 * @api public
 */

module.exports = function () {;
  var args = [].slice.call(arguments, 0)
    , i = 1
    , res = args[0];

  for (; i < args.length; i++) {
    merge(res, args[i]);
  }

  return res;
};

/*!
 * Start merge scenario by detecting if capable
 * and proxying to the appropriate sub-function.
 *
 * @param {Array|Object} destination
 * @param {Array|ObjectArray|Object} source
 * @return {Array|Object} destination merged
 * @api private
 */

function merge (a, b) {
  if (type(a) !== type(b)) {
    throw new Error('Incompatible merge scenario.');
  } else if (type.is(a, 'object')) {
    return mergeObject(a, b);
  } else if (type.is(a, 'array')) {
    return mergeArray(a, b);
  } else {
    throw new Error('Unsupported merge scenario');
  }
};

/*!
 * Start merge scenario for arrays.
 *
 * @param {Array} destination
 * @param {Array} source
 * @return {Array} destination merged
 * @api private
 */

function mergeArray (a, b) {
  var adds = []
    , i = 0
    , ai = 0;

  for (; i < b.length; i++) {
    if (('object' === type(a[i]) && 'object' === type(b[i]))
    ||  ('array' === type(a[i]) && 'array' === type(b[i]))) {
      a[i] = merge(a[i], b[i]);
    } else if ('object' === type(b[i])) {
      adds.push(merge({}, b[i]));
    } else if ('array' === type(b[i])) {
      adds.push(merge([], b[i]));
    } else if (!~a.indexOf(b[i])) {
      adds.push(b[i]);
    }
  }

  for (; ai < adds.length; ai++) {
    a.push(adds[ai]);
  }

  return a;
}

/*!
 * Start merge scenario for objects.
 *
 * @param {Object} destination
 * @param {Object} source
 * @return {Object} destination merged
 * @api private
 */

function mergeObject (a, b) {
  var keys = Object.keys(b)
    , i = 0
    , k;

  for (; i < keys.length; i++) {
    k = keys[i];

    if ('object' !== type(b[k]) && 'array' !== type(b[k])) {
      // TODO: better deref handling of other types
      a[k] = b[k];
    } else {
      a[k] = a.hasOwnProperty(k)
        ? merge(a[k], b[k])
        : merge(type.is(b[k], 'array') ? [] : {}, b[k]);
    }
  }

  return a;
}
