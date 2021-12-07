(ns merger)

(defn append-by-index [baseline extension-line-map merge-index]
  (concat baseline (get extension-line-map (nth baseline merge-index))))