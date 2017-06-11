(ns paradise.defaults
  (:require
   [clojure.edn :as edn]
   [paradise.core :refer :all])
  (:import (clojure.lang Symbol Keyword BigInt PersistentList
                         PersistentVector PersistentArrayMap
                         PersistentHashSet PersistentTreeMap Ratio PersistentTreeSet)
           (java.util UUID Date)
           (java.util.regex Pattern)))


;; set a default of not edn-encodable?

(defmethod edn-encodable? Object [_] false)


;; edn-encodable? methods for EDN spec types

(defmethod edn-encodable? nil [_] true)

(defmethod edn-encodable? Boolean [_] true)

(defmethod edn-encodable? String [_] true)

(defmethod edn-encodable? Character [_] true)

(defmethod edn-encodable? Symbol [_] true)

(defmethod edn-encodable? Keyword [_] true)

(defmethod edn-encodable? Long [_] true)

(defmethod edn-encodable? BigInt [_] true)

(defmethod edn-encodable? Double [_] true)

(defmethod edn-encodable? BigDecimal [_] true)

(defmethod edn-encodable? PersistentList [_] true)

(defmethod edn-encodable? PersistentVector [_] true)

(defmethod edn-encodable? PersistentArrayMap [_] true)

(defmethod edn-encodable? PersistentHashSet [_] true)

(defmethod edn-encodable? UUID [_] true)

(defmethod edn-encodable? Date [_] true)


;; non-spec things that Clojure does anyway

(defmethod edn-encodable? Ratio [_] true)


;; edn-encode default of calling `print-method`

(defmethod edn-encode Object [o] (partial print-method o))


;; edn-encode & edn-read methods for common things EDN should handle out of
;; the box (but doesn't)

(defmethod edn-encode Pattern [p] [:paradise/regex (.toString p)])
(defmethod edn-read :paradise/regex [[_ s]] (re-pattern s))

(defmethod edn-encode PersistentTreeMap [ptm] [:paradise/sorted-map ptm])
(defmethod edn-read :paradise/sorted-map [[_ m]] (into (sorted-map) m))

(defmethod edn-encode PersistentTreeSet [pts] [:paradise/sorted-set pts])
(defmethod edn-read :paradise/sorted-set [[_ s]] (into (sorted-set) s))