(ns paradise.edn-test
  (:require
   [clojure.edn]
   [clojure.test :refer :all]
   [paradise.edn :refer :all])
  (:refer-clojure :exclude [read read-string]))

(deftest write-string-test
  (testing "renders basic data structures the same as pr-str"
    (let [data {:foo "bar" :baz #{:qux "quux"}}]
      (is (= (pr-str data)
             (write-string data)))))
  (testing "renders regexes with paradise/regex tags"
    (let [data {:foo "bar" :baz #{:qux "quux"}
                :quuz #"corge"}]
      (is (= "{:foo \"bar\", :baz #{:qux \"quux\"}, :quuz #paradise/regex \"corge\"}"
             (write-string data)))))
  (testing "renders sorted-maps with paradise/sorted-map tags"
    (let [data {:foo "bar" :baz #{:qux "quux"}
                :quuz (sorted-map :first "1" :second "2")}]
      (is (= "{:foo \"bar\", :baz #{:qux \"quux\"}, :quuz #paradise/sorted-map {:first \"1\", :second \"2\"}}"
             (write-string data))))))

(deftest read-string-test
  (testing "reads basic EDN the same as clojure.edn/read-string"
    (let [edn "{:foo \"bar\" :baz #{:qux \"quux\"}}"]
      (is (= (clojure.edn/read-string edn)
             (read-string edn)))))
  (testing "reads #paradise/regex tagged values as regexes"
    (let [edn "#paradise/regex \"quux\""
          result (read-string edn)]
      (is (instance? java.util.regex.Pattern result))
      (is (= (.toString #"quux")
             (.toString result)))))
  (testing "reads #paradise/sorted-map tagged values as sorted-maps"
    (let [edn "#paradise/sorted-map {:second 2, :first 1}"]
      (is (= (sorted-map :first 1 :second 2)
             (read-string edn))))))
