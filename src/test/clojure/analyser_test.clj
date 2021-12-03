(ns analyser-test
  (:require [clojure.test :refer :all]
            [analyser :refer :all]))

(deftest symmetrical?-detects-palindromes
  (is (= true (symmetrical? '(\a \b \a))))
  (is (= false (symmetrical? '(\a \b \c)))))

(deftest find-max-freq-returns-kvp-with-highest-value
  (is (= ['(nil nil nil) 0] (find-max-freq {})))
  (is (= ['(\b \b \b) 10] (find-max-freq {'(\a \a \a) 1
                                          '(\b \b \b) 10
                                          '(\c \c \c) 9}))))

(deftest strip-spaces-removes-space-and-newline-returning-a-sequence
  (is (= '(\a \b) (strip-spaces "a b")))
  (is (= '(\f \o \o \b \a \r) (strip-spaces "foo\nbar"))))

(deftest quoted-can-detect-quotes-and-separators
  (is (= {:occurrences 2, :quote \', :separator \,} (quoted "'foo','bar','baz'")))
  (is (= {:occurrences 0, :quote nil, :separator nil} (quoted "foo,bar,baz"))))

(deftest unquoted-can-detect-separators
  (is (= {:occurrences 2, :quote nil, :separator \|} (unquoted "foo|bar|baz"))))

(deftest analyse-lines-can-detect-separators
  (is (= {:occurrences 4, :quote nil, :separator \|} (analyse-lines ["foo| bar |baz"
                                                                     "hello||world"]))))

(deftest analyse-lines-can-detect-quotes-and-separators
  (is (= {:occurrences 2, :quote \', :separator \;} (analyse-lines ["'foo'; 'bar'; 'baz'"
                                                                    "'hello';;'world'"]))))

(deftest analyse-can-read-both-gzipped-and-plain-csv-files
  (is (= {:occurrences 30, :quote nil, :separator \|} (analyse "./src/test/resources/unquoted.csv.gz")))
  (is (= {:occurrences 30, :quote nil, :separator \|} (analyse "./src/test/resources/unquoted.csv")))
  (is (= {:occurrences 9, :quote nil, :separator \|} (analyse "./src/test/resources/unquoted.csv" 3))))

