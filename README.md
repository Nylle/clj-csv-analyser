# Analyse a given CSV file

This is just some practising Clojure on an actual real-world problem.

## Usage

Detect quotation mark and separator in a plain csv file:
```clojure
(analyse "myfile.csv")
;=> {:occurrences 10, :quote \", :separator \,}
```

Detect quotation mark and separator in a gzipped csv file:
```clojure
(analyse "myfile.csv.gz")
;=> {:occurrences 10, :quote \", :separator \,}
```

Provide the number of sample lines to be analysed in the file (default 10):
```clojure
(analyse "myfile.csv" 100)
;=> {:occurrences 100, :quote \", :separator \,}
```

## Note to myself
```shell
clj -m nrepl.cmdline
deps -m nrepl.cmdline
```
