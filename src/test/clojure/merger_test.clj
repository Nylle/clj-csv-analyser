(ns merger-test
  (:require [clojure.test :refer :all]
            [merger :refer :all]))

(deftest append-by-index_appends-matching-line-from-map-to-baseline
  (let [expected ["foo" "key" "bar" "key" "b" "c"]
        baseline ["foo" "key" "bar"]
        input-map {"key" ["key" "b" "c"]}]
    (is (= (append-by-index baseline input-map 1) expected))))

(deftest append-by-index_appends-nothing-without-match
  (let [expected ["foo" "key" "bar"]
        baseline ["foo" "key" "bar"]
        input-map {"no-match" ["key" "b" "c"]}]
    (is (= (append-by-index baseline input-map 1) expected))))