(defn stack-consuming-fibo [n]
  (cond
   (= n 0) 0
   (= n 1) 1
   :else (+ (stack-consuming-fibo (- n 1))
	    (stack-consuming-fibo (- n 2)))))

(stack-consuming-fibo 3)
(stack-consuming-fibo 9)

(defn tail-fibo [n]
  (letfn [(fib 
	   [current next n]
	   (if (zero? n)
	     current
	   (fib next (+ current next) (dec n))))]
    (fib 0 1 n)))

(defn recur-fibo [n]
  (letfn [(fib 
	   [current next n]
	   (if (zero? n)
	     current
	   (recur next (+ current next) (dec n))))]
    (fib 0 1 n)))

;; lazy sequences p136
;; (lazy-seq & body)

(defn lazy-seq-fibo 
  ([] 
     (concat [0 1] (lazy-seq-fibo 0 1)))
  ([a b]
     (let [n (+ a b)]
       (lazy-seq
	 (cons n (lazy-seq-fibo b n))))))

(take 10 (lazy-seq-fibo))
;; n=0, F0 = 0
;; n=1, F1 = 1
;; n>1, Fn = Fn-1 + Fn-2
(rem (nth (lazy-seq-fibo) 1000000) 1000)

(take 5 (iterate (fn [[a b]] [b (+ a b)]) [0 1]))
(defn fibo [] (map first (iterate (fn [[a b]] [b (+ a b)]) [0 1])))


(set! *print-length* 10)
(take 100 (fibo))

;; contains a ref to the head of the sequence .. unused elements
;; can not be garbage collected .. so this eats heap fast
(def head-fibo (lazy-cat [0 1] (map + head-fibo (rest head-fibo))))
(take 11 head-fibo)
