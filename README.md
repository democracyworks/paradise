# paradise

[![Build Status](https://travis-ci.org/democracyworks/paradise.svg?branch=master)](https://travis-ci.org/democracyworks/paradise)

EDN in Clojure should be paradise. But all too often it isn't.

Examples of frustrating behavior:

* `pr-str` outputs invalid EDN when there are regexen in your data.
* There is no `clojure.edn/write` nor `clojure.edn/write-string`.
* Defining new data readers is cumbersome and disconnected from defining the
  counterpart tagged encoding.
* `clojure.edn/read` & `cloure.edn/read-string` throw exceptions on unknown
  tags.

Paradise is different. In contrast, it...

* Defines all of the following to do what you'd expect:
    * `paradise.edn/read`
    * `paradise.edn/read-string`
    * `paradise.edn/write`
    * `paradise.edn/write-string`
* Allows easily defining custom data types that will round-trip through these
  fns (without modifying how they normally print at the REPL via
  `print-method`).
* Round-trips regexen like a champ (again assuming you're using its read &
  write fns).
* Just ignores unknown tags, placing the raw value in the returned data
  structure.

## Usage

In your project dependencies, add:

`[paradise "0.1.0"]`

Then in your code:

```clojure
(ns my.app
  (:require
   [paradise.edn :as edn]))
   
(def some-edn (edn/write-string {:regex #"itworks"}))

(def original-data (edn/read-string some-edn))
```

To allow custom data types to round-trip through EDN:

```clojure
(ns my.app
  (:require
   [paradise.edn :as edn]))

(defrecord MyRecord [foo bar])

(defmethod edn-encode MyRecord [r] [:my/record (into {} r)])
(defmethod edn-read :my/record [[tag m]] (map->MyRecord m))
```

`edn-encode` is a multi-method that dispatches on `class`. Its purpose is to
prepare custom data types for EDN encoding by defining a tag and an EDN
representation to follow the tag. To define new methods, just specify a
`defmethod` form with the class you want to encode as the dispatch value, take
one arg (an instance of that class), and return a vector whose first element is
a namespaced keyword that will become the tag in the resulting EDN and whose
second element is the EDN-encodable value you want to follow the tag.

`edn-encode` can also return a fn that takes one argument: an instance of
`java.io.Writer`. This fn will be called with the appropriate Writer when
generating EDN via paradise. This was built to support deferring to
`print-method` (which takes a Writer) by default so that EDN printing defined
via the traditional mechanism (vs. via paradise's method) would still work with
paradise's `edn/write` & `edn/write-string` fns.

`edn-read` is a multi-method that dispatches on `first`. Its purpose is to
associate reader fns with EDN tags so that they can be turned back into their
original values. To define new methods, just specify a `defmethod` form with
the namespaced keyword representing the EDN tag as the dispatch value, take one
arg (a 2-element vector of \[tag value]; feel encouraged to destructure that),
and return the EDN representation converted back into an instance of the
original class. 

### data readers

Data readers defined outside paradise (by you or by libraries you use) should
Just Work in paradise. The `paradise.edn/read` & `read-string` fns call their
`clojure.edn` counterparts under the hood.

### print-methods

`print-method` methods defined by you or libraries you use should Just Work in
paradise. This is because the default `edn-encode` method just defers to
`print-method`.

## License

Copyright © 2017 Democracy Works, Inc.

Distributed under the Mozilla Public License either version 2.0 or (at your
option) any later version.
