;; Lazier Than Lazy

[:h :t :t :h :h :h]

(defn count-heads-pairs [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
	       (inc cnt)
	       cnt)
	     (rest coll)))))

(count-heads-pairs [:h :h :h :t :h])
(count-heads-pairs [:h :t :h :t :h])

;; overly complex, better approaches follow ...
(defn by-pairs [coll]
  (let [take-pair (fn [c]
		    (when (next c) (take 2 c)))]
    (lazy-seq
      (when-let [pair (seq (take-pair coll))]
	(cons pair (by-pairs (rest coll)))))))

(by-pairs [:h :t :t :h :h :h])

(defn count-heads-pairs [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
		 (by-pairs coll))))

;; (partition size step? coll)
(partition 2 [:h :t :t :h :h :h])
(partition 2 1 [:h :t :t :h :h :h])

(defn count-heads-pairs [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
		 (partition 2 1 coll))))

(use '[clojure.contrib.def :only (defvar)])
(defvar count-if (comp count filter) "Count items matching a filter")

;; (comp f & fs)
(count-if odd? [1 2 3 4 5 6 7])

(defn count-heads-pairs [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
		 (partition 2 1 coll))))

(count-heads-pairs [:h :t :t :h :h :h])

(defn count-heads-pairs [coll]
  (count-if (every? #(= :h %))
	    (partition 2 1 coll)))

(= 2 (count-heads-pairs [:h :t :t :h :h :h]))

(defn count-runs
  "count runs of length n where pred is true in coll."
  [n pred coll]
  (count-if #(every? pred %)
	    (partition n 1 coll)))

(defn count-heads-pairs [coll]
  (count-runs 2 #(= % :h) coll))

(= 2 (count-heads-pairs [:h :t :t :h :h :h]))

(count-runs 2 #(= % :h) [:h :t :t :h :h :h])
(count-runs 2 #(= % :t) [:h :t :t :h :h :h])
(count-runs 3 #(= % :h) [:h :t :t :h :h :h])

;; (partial f & partial-args)

(defvar count-heads-pairs (partial count-runs 2 #(= % :h))
  "Count runs of length two that are both heads")

(= 2 (count-heads-pairs [:h :t :t :h :h :h]))

;; (partial count-runs 2 #(= % :h))
;; partial is a more expressive way of saying..
;; (fn [coll] (count-runs 2 #(= % :h) coll))