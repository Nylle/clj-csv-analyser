(ns merger
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:import (java.util.zip ZipFile)))

(defn padr [n coll & [val]]
  (if (<= n (count coll))
    coll
    (take n (concat coll (repeat (or val ""))))))

(defn padl [n coll & [val]]
  (concat (take (- n (count coll)) (repeat (or val ""))) coll))

(defn chunk-map [records key-col offset size]
  (let [chunk (take size (drop offset records))]
    (into {} (map (fn [[key value]] [key (first value)]) (group-by #(nth % key-col) chunk)))))

(defn real-merge [base-line extension-line size]
  (map #(if (empty? %1) %2 %1) (padr size base-line) (padl size extension-line)))

(defn merge-chunk [base-lines extension-line-map merge-index size]
  (map #(real-merge % (get extension-line-map (nth % merge-index)) size) base-lines)
  )

(defn sorted-streams [zip-file]
  (as-> (enumeration-seq (.entries zip-file)) $
        (sort-by #(.getName %) $)
        (map #(.getInputStream zip-file %) $)))

(defn xxx [base-stream ext-stream out-stream size]
  (with-open [base-rdr (io/reader base-stream)
              ext-rdr (io/reader ext-stream)
              writer (io/writer out-stream)]
    (let [base-lines (csv/read-csv base-rdr)
          ext-lines (csv/read-csv ext-rdr)]
      (write-csv writer)
      (doall
        (merge-chunk base-lines (chunk-map ext-lines 0 0 4) 0 size)))
    )

  )


(defn walkzip [from to]
  (with-open [z (ZipFile. from)
              r (File. to)]
    (let [streams (sorted-streams z)]
      (with-open [base-rdr (io/reader (first streams))
                  ext-rdr (io/reader (nth streams 1))
                  writer (io/writer to)]
        (let [base-lines (csv/read-csv base-rdr)
              ext-lines (csv/read-csv ext-rdr)
              size (+ (count (first base-lines)) (count (first ext-lines)))]
          (doall
            (merge-chunk base-lines (chunk-map ext-lines 0 0 4) 0 size)))
        )
      )))




