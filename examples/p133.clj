(defn stack-consuming-fibo [n]
  (cond
   (= n 0) 0
   (= n 1) 1
   :else (+ (stack-consuming-fibo (- n 1))
	    (stack-consuming-fibo (- n 2)))))

(stack-consuming-fibo 3)

(stack-consuming-fibo 9)