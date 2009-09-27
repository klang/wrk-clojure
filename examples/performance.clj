(defn sum-to [n]
  (loop [i 1 sum 0]
    (if (<= i n)
      (recur (inc i) (+ i sum))
      sum)))

(defn integer-sum-to [n]
  (let [n (int n)]
    (loop [i (int 1) sum (int 0)]
      (if (<= i n)
	(recur (inc i) (+ i sum))
	sum))))

(defn unchecked-sum-to [n]
  (let [n (int n)]
    (loop [i (int 1) sum (int 0)]
      (if (<= i n)
	(recur (inc i) (unchecked-add i sum))
	sum))))