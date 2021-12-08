(ns merger-test
  (:require [clojure.test :refer :all]
            [merger :refer :all]))

(deftest padr_pads-right
  (is (= '(1 2 3 0) (padr 4 [1 2 3] 0)))
  (is (= '(1 2 3 4) (padr 4 [1 2 3 4] 0)))
  (is (= '(1 2 3 "") (padr 4 [1 2 3])))
  (is (= '(1 2 3) (padr 1 [1 2 3]))))

(deftest padl_pads-left
  (is (= '(0 1 2 3) (padl 4 [1 2 3] 0)))
  (is (= '(1 2 3 4) (padl 4 [1 2 3 4] 0)))
  (is (= '("" 1 2 3) (padl 4 [1 2 3])))
  (is (= '(1 2 3) (padl 1 [1 2 3])))
  )

(deftest chunk-map_takes-specified-number-of-lines-at-specified-index-and-returns-a-map-with-the-specified-index-as-key
  (let [source   [["1" "2" "3"] ["a" "b" "c"] ["4" "5" "6"] ["d" "e" "f"]]
        group-index 1]
    (is (= {"2" ["1" "2" "3"]
            "b" ["a" "b" "c"]} (chunk-map source group-index 0 2)))
    (is (= {"b" ["a" "b" "c"]
            "5" ["4" "5" "6"]
            "e" ["d" "e" "f"]} (chunk-map source group-index 1 3)))))

(deftest merge-chunk_appends-matching-line-from-map-to-baseline
  (let [expected [["foo" "key" "bar" "key" "b" "c"]]
        base-lines [["foo" "key" "bar"]]
        input-map {"key" ["key" "b" "c"]}]
    (is (= expected (merge-chunk base-lines input-map 1 6)))))

(deftest merge-chunk_appends-empty-without-match
  (let [expected [["foo" "key" "bar" "" "" ""]]
        base-lines [["foo" "key" "bar"]]
        input-map {"no-match" ["no-match" "b" "c"]}]
    (is (= expected (merge-chunk base-lines input-map 1 6)))))

