(ns analyser
  (:import (java.util.zip GZIPInputStream)))

(defn symmetrical? [trigram]
  (= trigram (into '() trigram)))

(defn find-max-freq [data]
  (apply max-key val (or (seq data) {'(nil nil nil) 0})))

(defn strip-spaces [data]
  (filter #(and (not= \space %) (not= \newline %)) (seq data)))

(defn quoted [data]
  (as-> (partition 3 1 data) $
        (filter #(not (Character/isLetterOrDigit (nth % 1))) $)
        (filter symmetrical? $)
        (frequencies $)
        (find-max-freq $)
        (hash-map :separator (nth (first $) 1) :quote (first (first $)) :occurrences (nth $ 1))))

(defn unquoted [data]
  (as-> (seq data) $
        (filter #(not (Character/isLetterOrDigit %)) $)
        (frequencies $)
        (find-max-freq $)
        (hash-map :separator (first $) :occurrences (nth $ 1) :quote nil)))

(defn analyse-lines [extracted-lines]
  (let [line-count (count extracted-lines)
        data       (strip-spaces (clojure.string/join extracted-lines))
        quoted     (quoted data)]
    (if (<= line-count (quoted :occurrences))
      quoted
      (unquoted data))))

(defn analyse [file & [lines]]
  (let [f #(analyse-lines (take (or lines 10) (line-seq %)))]
    (if (clojure.string/ends-with? file ".gz")
      (with-open [rdr (clojure.java.io/reader (GZIPInputStream. (clojure.java.io/input-stream file)))]
        (f rdr))
      (with-open [rdr (clojure.java.io/reader file)]
        (f rdr)))))
