/**
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 *
 * 
 */

'use strict';

var _interopRequireDefault = require('babel-runtime/helpers/interop-require-default')['default'];

Object.defineProperty(exports, '__esModule', {
  value: true
});
exports.getState = getState;
exports.initializeState = initializeState;
exports.removeState = removeState;
exports.getServerData = getServerData;
exports.setServerData = setServerData;
exports.getPendingOps = getPendingOps;
exports.setPendingOp = setPendingOp;
exports.pushPendingState = pushPendingState;
exports.popPendingState = popPendingState;
exports.mergeFirstPendingState = mergeFirstPendingState;
exports.getObjectCache = getObjectCache;
exports.estimateAttribute = estimateAttribute;
exports.estimateAttributes = estimateAttributes;
exports.commitServerChanges = commitServerChanges;
exports.enqueueTask = enqueueTask;
exports._clearAllState = _clearAllState;

var _encode = require('./encode');

var _encode2 = _interopRequireDefault(_encode);

var _ParseFile = require('./ParseFile');

var _ParseFile2 = _interopRequireDefault(_ParseFile);

var _ParseObject = require('./ParseObject');

var _ParseObject2 = _interopRequireDefault(_ParseObject);

var _ParsePromise = require('./ParsePromise');

var _ParsePromise2 = _interopRequireDefault(_ParsePromise);

var _ParseRelation = require('./ParseRelation');

var _ParseRelation2 = _interopRequireDefault(_ParseRelation);

var _TaskQueue = require('./TaskQueue');

var _TaskQueue2 = _interopRequireDefault(_TaskQueue);

var _ParseOp = require('./ParseOp');

var objectState = {};

function getState(className, id) {
  var classData = objectState[className];
  if (classData) {
    return classData[id] || null;
  }
  return null;
}

function initializeState(className, id, initial) {
  var state = getState(className, id);
  if (state) {
    return state;
  }
  if (!objectState[className]) {
    objectState[className] = {};
  }
  if (!initial) {
    initial = {
      serverData: {},
      pendingOps: [{}],
      objectCache: {},
      tasks: new _TaskQueue2['default'](),
      existed: false
    };
  }
  state = objectState[className][id] = initial;
  return state;
}

function removeState(className, id) {
  var state = getState(className, id);
  if (state === null) {
    return null;
  }
  delete objectState[className][id];
  return state;
}

function getServerData(className, id) {
  var state = getState(className, id);
  if (state) {
    return state.serverData;
  }
  return {};
}

function setServerData(className, id, attributes) {
  var data = initializeState(className, id).serverData;
  for (var attr in attributes) {
    if (typeof attributes[attr] !== 'undefined') {
      data[attr] = attributes[attr];
    } else {
      delete data[attr];
    }
  }
}

function getPendingOps(className, id) {
  var state = getState(className, id);
  if (state) {
    return state.pendingOps;
  }
  return [{}];
}

function setPendingOp(className, id, attr, op) {
  var pending = initializeState(className, id).pendingOps;
  var last = pending.length - 1;
  if (op) {
    pending[last][attr] = op;
  } else {
    delete pending[last][attr];
  }
}

function pushPendingState(className, id) {
  var pending = initializeState(className, id).pendingOps;
  pending.push({});
}

function popPendingState(className, id) {
  var pending = initializeState(className, id).pendingOps;
  var first = pending.shift();
  if (!pending.length) {
    pending[0] = {};
  }
  return first;
}

function mergeFirstPendingState(className, id) {
  var first = popPendingState(className, id);
  var pending = getPendingOps(className, id);
  var next = pending[0];
  for (var attr in first) {
    if (next[attr] && first[attr]) {
      var merged = next[attr].mergeWith(first[attr]);
      if (merged) {
        next[attr] = merged;
      }
    } else {
      next[attr] = first[attr];
    }
  }
}

function getObjectCache(className, id) {
  var state = getState(className, id);
  if (state) {
    return state.objectCache;
  }
  return {};
}

function estimateAttribute(className, id, attr) {
  var serverData = getServerData(className, id);
  var value = serverData[attr];
  var pending = getPendingOps(className, id);
  for (var i = 0; i < pending.length; i++) {
    if (pending[i][attr]) {
      if (pending[i][attr] instanceof _ParseOp.RelationOp) {
        value = pending[i][attr].applyTo(value, { className: className, id: id }, attr);
      } else {
        value = pending[i][attr].applyTo(value);
      }
    }
  }
  return value;
}

function estimateAttributes(className, id) {
  var data = {};
  var attr;
  var serverData = getServerData(className, id);
  for (attr in serverData) {
    data[attr] = serverData[attr];
  }
  var pending = getPendingOps(className, id);
  for (var i = 0; i < pending.length; i++) {
    for (attr in pending[i]) {
      if (pending[i][attr] instanceof _ParseOp.RelationOp) {
        data[attr] = pending[i][attr].applyTo(data[attr], { className: className, id: id }, attr);
      } else {
        data[attr] = pending[i][attr].applyTo(data[attr]);
      }
    }
  }
  return data;
}

function commitServerChanges(className, id, changes) {
  var state = initializeState(className, id);
  for (var attr in changes) {
    var val = changes[attr];
    state.serverData[attr] = val;
    if (val && typeof val === 'object' && !(val instanceof _ParseObject2['default']) && !(val instanceof _ParseFile2['default']) && !(val instanceof _ParseRelation2['default'])) {
      var json = (0, _encode2['default'])(val, false, true);
      state.objectCache[attr] = JSON.stringify(json);
    }
  }
}

function enqueueTask(className, id, task) {
  var state = initializeState(className, id);
  return state.tasks.enqueue(task);
}

function _clearAllState() {
  objectState = {};
}