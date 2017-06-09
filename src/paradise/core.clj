(ns paradise.core
  (:import (java.io Writer)))

(defmulti edn-encodable? class)

(defmulti edn-encode class)

(defmulti edn-read first)

(defmethod print-method ::edn [[tag value] ^Writer w]
  (.write w (str "#" (-> tag namespace)
                 "/" (-> tag name) " "))
  (print-method value w))

(defn add-metadata [encoded]
  (if (and (vector? encoded)
           (= 2 (count encoded)))
    (vary-meta encoded assoc :type ::edn)
    encoded))

(defn read-edn [tag value]
  (try
    (edn-read [(keyword tag) value])
    (catch IllegalArgumentException e
      value)))