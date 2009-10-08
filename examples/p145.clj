;; currying and partial application

;; almost Curry

(defn faux-curry [& args] (apply partial partial args))

(def add-3 (partial + 3))
(add-3 7)

(def add-3 ((faux-curry +) 3))
(add-3 7)

;; faux curry
((faux-curry true?) (= 1 1))
;; => function name

;; real curry
;; ((curry true?) (=1 1)) 
;; => true

;; clojure does not know when all arguments are present
;; but the programmer does .. and can add another set 
;; of () to get the form evaluated
(((faux-curry true?) (= 1 1)))
;; => true