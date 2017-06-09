(ns paradise.core-test
  (:require [clojure.test :refer :all]
            [paradise.core :refer :all]))

(deftest print-method-test
  (testing "writes the EDN tag and value"
    (is (= "#paradise/regex \"qux\""
           (with-out-str
            (print-method
             ^{:type :paradise.core/edn} [:paradise/regex "qux"]
             *out*))))))

(deftest add-metadata-test
  (testing "sets the :type :paradise.core/edn metadata on 2-element vectors"
    (is (= :paradise.core/edn
           (-> [:foo/bar "baz"]
               add-metadata
               meta
               :type))))
  (testing "leaves other values' metadata alone"
    (is (= :thingy
           (-> ^{:type :thingy} [:wobble]
               add-metadata
               meta
               :type)))))

(deftest read-edn-test
  (testing "calls edn-read multi-method on keywordized tag and value"
    (defmethod edn-read ::test [x] x)
    (is (= [::test "hi!"]
           (read-edn 'paradise.core-test/test "hi!"))))
  (testing "returns bare value when no edn-read method defined for tag"
    (is (= ::hello
           (read-edn 'paradise.core-test/foo ::hello)))))