# tea-merge

> Deep object merge utility.

## Installation

### Node.js

`tea-merge` is available on [npm](http://npmjs.org).

    $ npm install tea-merge

### Component

`tea-merge` is available as a [component](https://github.com/component/component).

    $ component install qualiancy/tea-merge

## Usage

### merge (destination, source, ...)

* **@param** _{Array|Object}_ destination 
* **@param** _{Array|Object}_ sources ...
* **@return** _{Object}_  destination merge

For each source, shallow merge its key/values to the
destination. Sources are read in order, meaning the same
key in a later source will overwrite the key's value set
earlier.

Also, this tool only supports objects and arrays. Furthermore,
the destination and all sources must be of the same type.

```js
var merge = require('tea-merge');

// sample objects
var a = { hello: 'universe', arr: [ { a: 'a' } ] }
  , b = { speak: 'loudly', arr: [ { b: 'b' }, { c: 'c' } };

merge(a, b);
a.should.deep.equal({
    hello: 'universe'
  , speak: 'loudly'
  , arr: [
        { a: 'a', b: 'b' }
      , { c: 'c' }
    ]
});
```

When merging objects, it is expected that if the
key from the source already exists in the destination,
the existing value in the source supports the same type of
iteration as in the destination. If they cannot, a
`Incompatible merge scenario.` error will be thrown.

##### Rules

- Non-iterable values can be replaced with other
non-iterable values: strings, numbers, etc.
- Iterable values cannot replace non-iterable
values; objects can't replace string, arrays, can't
replace numbers, etc.
- Non-iterable values cannot replace iterable
values; numbers can't replace objects, strings can't
replace arrays, etc.


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
