;; http://clojure.org/reader
;; Metadata (#^)
;; The metadata reader macro first reads the metadata and attaches it to the next form read

(defn
  #^{:doc "mymax [xs+] gets the maximum value in xs using > "
     :test (fn [] (assert (= 42 (mymax 2 42 5 4))))
     :user/comment "this is the best fn ever!"}
  mymax
  ([x] x)
  ([x y] (if (> x y) x y))
  ([x y & more]
     (reduce mymax (mymax x y) more)))

;; (test #'mymax)
;; (mymax 3 43 5 4)
;; (doc mymax)



(defn
  #^{:test (fn []
             (assert (= 0 (myadd -2 2)))
             (assert (= -4 (myadd -2 -2)))
             (assert (= 4 (myadd 2 2))))}
  myadd [a b]
  (+ a b)
  )
;; (test #'myadd)
(doc test)
(defn 
  #^{:doc "short line describing the defun"
     :test (fn [] 
             (assert (= 1 (mysubtract 3 2)))
             (assert (= 0 (mysubtract 2 2)))
             (assert (= -1 (mysubtract 2 3)))
             )
     :user/comment "how to get hold of this thing?"}
  mysubtract [a b]
  (- a b))

(test #'mysubtract)
(doc mysubtract)
(doc assert)
