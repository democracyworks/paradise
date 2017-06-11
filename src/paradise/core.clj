(ns paradise.core
  (:import (java.io Writer)))

(defmulti edn-encodable? class)

(defmulti edn-encode class)

(defmulti edn-read first)

(defmethod print-method ::edn-tagged [[tag value] ^Writer w]
  (.write w (str "#" (-> tag namespace)
                 "/" (-> tag name) " "))
  (print-method value w))

(defmethod print-method ::edn-printer [print-edn ^Writer w]
  (print-edn w))

(defn add-metadata [encoded]
  (cond
    (and (vector? encoded)
         (= 2 (count encoded)))
    (vary-meta encoded assoc :type ::edn-tagged)

    (fn? encoded)
    (vary-meta encoded assoc :type ::edn-printer)

    :else encoded))

(defn read-edn [tag value]
  (try
    (edn-read [(keyword tag) value])
    (catch IllegalArgumentException e
      value)))