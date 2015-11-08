# tea-type [![Build Status](https://secure.travis-ci.org/qualiancy/tea-type.png?branch=master)](https://travis-ci.org/qualiancy/tea-type)

> Better typeof detection and assertions for node.js and the browser.

## Installation

### Node.js

`tea-type` is available on [npm](http://npmjs.org).

    $ npm install tea-type

### Component

`tea-type` is available as a [component](https://github.com/component/component).

    $ component install qualiancy/tea-type

## Usage

Simple usage returns a more accurate `typeof` string. Examples augmented with
[chai](http://chaijs.com) assertions.

```js
var type = requre('tea-type');

type([ 1,2 ]).should.equal('array');
type('hello world').should.equal('string');
type(/abc/g).should.equal('regexp');
type(new Date).should.equal('data');
// etc...
```

Also, simple testing of object types.

```js
type.is([], 'array').should.be.true;
type.is('1', 'number').should.be.false;
// etc
```

But you can also add your own custom type tests using `.test()`. The test
can be a regular expression or function.

```js
// regular expression
type.test('int', /^[0-9]+$/);
type.is('1', 'int').should.be.true;
type.is('a', 'int').should.be.false;


// function
type.test('bln', function (obj) {
  if ('boolean' === type(obj)) return true;
  var blns = [ 'yes', 'no', 'true', 'false', 1, 0 ];
  if ('string' === type(obj)) obj = obj.toLowerCase();
  return !! ~blns.indexOf(obj);
});

type.is(true, 'bln').should.be.true;
type.is(false, 'bln').should.be.true;
type.is('Yes', 'bln').should.be.true;
type.is('no', 'bln').should.be.true;
type.is('true', 'bln').should.be.true;
type.is('False', 'bln').should.be.true;
type.is(1, 'bln').should.be.true;
type.is(0, 'bln').should.be.true;
type.is(2, 'bln').should.be.false;
type.is('nope', 'bln').should.be.false;
```

## License

(The MIT License)

Copyright (c) 2012 Jake Luer <jake@qualiancy.com> (http://qualiancy.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
