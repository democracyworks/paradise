(ns paradise.edn
  (:require
   [clojure.edn :as edn]
   [clojure.walk :as walk]
   [paradise.defaults]
   [paradise.core :refer :all])
  (:refer-clojure :exclude [read read-string]))

(defn write
  "Takes a value `v` and optionally a `stream` (defaults to *out* if not
  supplied), encodes the value into EDN and writes it to `stream`."
  ([v] (write v *out*))
  ([v stream]
   (binding [*out* stream]
     (pr
      (walk/postwalk
       (fn [f]
         (if (edn-encodable? f)
           f
           (-> f
               edn-encode
               add-metadata)))
       v)))))

(defn write-string
  "Takes a value `v` and encodes into EDN, returing that as a string."
  [v]
  (with-out-str (write v)))

(defn read [stream]
  (edn/read {:default read-edn, :eof nil} stream))

(defn read-string [s]
  (edn/read-string {:default read-edn, :eof nil} s))